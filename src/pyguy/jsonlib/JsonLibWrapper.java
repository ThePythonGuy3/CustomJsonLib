package pyguy.jsonlib;

import arc.util.serialization.JsonValue;

/**
 * The public API to interface with the {@link pyguy.jsonlib.JsonLib} library.
 * <p>
 * All the methods in this class MUST be called within your mod's {@code init()} method or after.
 * Failing to do so will result in unexpected behavior.
 */
public class JsonLibWrapper
{
    private JsonLibWrapper(){}

    /**
     * Get a {@link String} field from a custom JSON definition.
     *
     * @param internalContentName The internal name of the piece of content that implements the custom JSON definition.
     * @param fieldName           The name of the custom JSON field.
     * @return The value of the custom JSON field, or {@code null} if it does not exist or is not a {@link String}.
     */
    public static String GetStringField(String internalContentName, String fieldName)
    {
        JsonValue value = JsonLib.Get(internalContentName, fieldName);

        if (value != null && value.isString())
        {
            return value.asString();
        }

        return null;
    }

    /**
     * Get a {@link Long} field from a custom JSON definition.
     *
     * @param internalContentName The internal name of the piece of content that implements the custom JSON definition.
     * @param fieldName           The name of the custom JSON field.
     * @return The value of the custom JSON field, or {@code null} if it does not exist or is not a {@link Long}.
     */
    public static Long GetLongField(String internalContentName, String fieldName)
    {
        JsonValue value = JsonLib.Get(internalContentName, fieldName);

        if (value != null && value.isLong())
        {
            return value.asLong();
        }

        return null;
    }

    /**
     * Get a {@link Double} field from a custom JSON definition.
     *
     * @param internalContentName The internal name of the piece of content that implements the custom JSON definition.
     * @param fieldName           The name of the custom JSON field.
     * @return The value of the custom JSON field, or {@code null} if it does not exist or is not a {@link Double}.
     */
    public static Double GetDoubleField(String internalContentName, String fieldName)
    {
        JsonValue value = JsonLib.Get(internalContentName, fieldName);

        if (value != null && value.isDouble())
        {
            return value.asDouble();
        }

        return null;
    }

    /**
     * Get a {@link Boolean} field from a custom JSON definition.
     *
     * @param internalContentName The internal name of the piece of content that implements the custom JSON definition.
     * @param fieldName           The name of the custom JSON field.
     * @return The value of the custom JSON field, or {@code null} if it does not exist or is not a {@link Boolean}.
     */
    public static Boolean GetBooleanField(String internalContentName, String fieldName)
    {
        JsonValue value = JsonLib.Get(internalContentName, fieldName);

        if (value != null && value.isBoolean())
        {
            return value.asBoolean();
        }

        return null;
    }

    /**
     * Get a raw {@link arc.util.serialization.JsonValue} field from a custom JSON definition.
     * <p>
     * This must be used to get fields that are objects or arrays.
     *
     * @param internalContentName The internal name of the piece of content that implements the custom JSON definition.
     * @param fieldName           The name of the custom JSON field.
     * @return The value of the custom JSON field as a {@link JsonValue}, or {@code null} if it does not exist.
     */
    public static JsonValue GetRawField(String internalContentName, String fieldName)
    {
        return JsonLib.Get(internalContentName, fieldName);
    }
}
