package pyguy.jsonlib;

import arc.Events;
import arc.struct.*;
import arc.util.Log;
import arc.util.serialization.*;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.ctype.MappableContent;
import mindustry.game.EventType;
import mindustry.mod.*;

import java.lang.reflect.Field;

// This class is the internal implementation of the library. You do not need to understand any of the code here to use the library.
// The public API is in the JsonLibWrapper class.
public class JsonLib extends Mod
{
    private static final ObjectMap<String, ObjectMap<String, JsonValue>> customJson = new ObjectMap<>();
    private static final Seq<JsonValue> latestValues = new Seq<>();
    private static boolean shouldProxy = false;
    private static String modName = "";

    public JsonLib()
    {
        try
        {
            Log.info("[CustomJsonLib] Retrieving custom JSON fields.");

            // Get access to the global ContentParser instance
            Field contentParserField = Mods.class.getDeclaredField("parser");
            contentParserField.setAccessible(true);

            ContentParser contentParser = (ContentParser) contentParserField.get(Vars.mods);

            // Get access to the global ContentParser instance's Json instance
            Field jsonParserField = ContentParser.class.getDeclaredField("parser");
            jsonParserField.setAccessible(true);

            Json jsonParser = (Json) jsonParserField.get(contentParser);

            // Get the mod name
            Field currentModField = ContentParser.class.getDeclaredField("currentMod");
            currentModField.setAccessible(true);

            // Replace the contentNameMap to retrieve the name of the content being parsed, if you find a better way to do this PLEASE let me know
            Field contentNameMapField = ContentLoader.class.getDeclaredField("contentNameMap");
            contentNameMapField.setAccessible(true);

            @SuppressWarnings("unchecked")
            ObjectMap<String, MappableContent>[] contentNameMap = (ObjectMap<String, MappableContent>[]) contentNameMapField.get(Vars.content);

            @SuppressWarnings("unchecked")
            ObjectMap<String, MappableContent>[] replacementContentNameMapArray = new ObjectMap[contentNameMap.length];
            int i = 0;
            for (ObjectMap<String, MappableContent> map : contentNameMap)
            {
                ProxyObjectMap replacementMap = new ProxyObjectMap();

                for (String key : map.keys())
                {
                    replacementMap.put(key, map.get(key));
                }

                replacementContentNameMapArray[i] = replacementMap;
                i++;
            }

            contentNameMapField.set(Vars.content, replacementContentNameMapArray);

            // Replace the ContentParser json parser with a custom one that can read custom JSON fields before passing it on to the original parser
            Json newParser = new Json()
            {
                @Override
                public <T> T readValue(Class<T> type, Class elementType, JsonValue jsonData, Class keyType)
                {
                    if (jsonData.has("customJson"))
                    {
                        try
                        {
                            modName = ((Mods.LoadedMod) currentModField.get(contentParser)).name;
                        } catch (IllegalAccessException e)
                        {
                            throw new RuntimeException(e);
                        }

                        latestValues.clear();
                        shouldProxy = true;

                        for (JsonValue value : jsonData.get("customJson").iterator())
                        {
                            latestValues.add(value);
                        }
                    }

                    return jsonParser.readValue(type, elementType, jsonData, keyType);
                }
            };

            jsonParserField.set(contentParser, newParser);
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static JsonValue Get(String internalContentName, String fieldName)
    {
        if (customJson.containsKey(internalContentName))
        {
            ObjectMap<String, JsonValue> map = customJson.get(internalContentName);
            if (map != null) return map.get(fieldName);
        }

        return null;
    }

    @Override
    public void init()
    {
        Events.on(EventType.ClientLoadEvent.class, event -> {
            for (String key : customJson.keys())
            {
                Log.info("[CustomJsonLib] Custom JSON for key: " + key + " with values: " + customJson.get(key).toString());
            }
        });
    }

    private static class ProxyObjectMap extends ObjectMap<String, MappableContent>
    {
        @Override
        public MappableContent get(String key)
        {
            if (shouldProxy)
            {
                String fullKey = modName + "-" + key;
                shouldProxy = false;

                if (!customJson.containsKey(fullKey))
                {
                    customJson.put(fullKey, new ObjectMap<>());
                }

                ObjectMap<String, JsonValue> objectMap = customJson.get(fullKey);
                for (JsonValue value : latestValues)
                {
                    objectMap.put(value.name, value);
                }
            }

            return super.get(key);
        }
    }
}
