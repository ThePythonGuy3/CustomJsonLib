# Custom JSON Lib for Mindustry

Allows [Mindustry](https://github.com/Anuken/Mindustry) Mods to add and read custom JSON tags to act as libraries for other Mindustry Mods.

## How do I use this library?

<details>
  <summary>I want to implement new JSON tags</summary>
  
  ## My mod is written in Java
  If your mod is written in Java (and is, hence, a Jar mod), use this method.
  
  1. **Add the library as a dependency in your** `mod.[h]json` **file:**
  
  > You can SKIP this step if your mod supports this library but does not *require* it to function.
  
  &nbsp;&nbsp;&nbsp;JSON:
  ```json
  "dependencies": [
    "pyguy.jsonlib"
  ]
  ```
  &nbsp;&nbsp;&nbsp;HJSON:
  ```
  dependencies: [
    pyguy.jsonlib
  ]
  ```
  
  2. **Add the library Jar file into your project as a Java dependency:**
  
  This is a necessary step before you can compile your mod, since the library Jar contains the methods (functions) used to access custom JSON tags.\
  To get the library file, either download the latest release of `CustomJsonLib.jar` (NOT `CustomJsonLibDesktop.jar`) from Releases, or compile your own (See [Building](#building) below).
  
  
  Copy the file into a directory called `lib/` you must create on your mod's root directory:
  ```
  - YourAwesomeMod/
    - src/
    - assets/
    - ...
    - lib/
      - CustomJsonLib.jar
  ```
  
  Assuming you're using Gradle as your build system, add the Jar file as a dependency in your mod's `build.gradle.kts`:
  ```kotlin
  project(":"){
    // ...
  
    dependencies{
      // ...
      compileOnly(files(layout.projectDirectory.dir("lib").file("CustomJsonLib.jar")))
    }
  
    // ...
  }
  ```
  If you are using other build systems, ensure that you are adding the library as a Compile Only dependency. This is VERY important and your mod will not work properly otherwise.
  
  
  Do note that most IDEs will not immediately detect the library after this step. Please restart your IDE or reload the Gradle script (ask your favorite Search Engine how to do this) for it to take effect.
  
  3. **Use the library**
  
  Now the library is part of your project. This does NOT mean it will be shipped with your Jar files, and it makes the files no larger, it just allows for compilation and usage of the library methods.
  
  
  The content table for JSON tags is created and ready to be used from your mod's `init()` method onward. If you plan on checking all content for custom JSON tags, it is recommended to do so after the client loads, like so:
  ```java
  @Override
  public void init()
  {
    Events.on(EventType.ClientLoadEvent.class, event -> {
      // Your code here
    });
  }
  ```
  
  To know what methods this library supports, see [Using the Library](#using-the-library) below.
  
  <br/>
  
  ## My mod is written in JavaScript and [H]JSON
  If your mod is written in JavaScript and [H]JSON (and is, hence, a standard Mindustry mod), use this method.
  
  1. **Add the library as a dependency in your** `mod.[h]json` **file:**
  
  > You can SKIP this step if your mod supports this library but does not *require* it to function.
  
  &nbsp;&nbsp;&nbsp;JSON:
  ```json
  "dependencies": [
    "pyguy.jsonlib"
  ]
  ```
  &nbsp;&nbsp;&nbsp;HJSON:
  ```
  dependencies: [
    pyguy.jsonlib
  ]
  ```
  
  2. **Create a reference to JsonLibWrapper for your mod:**
  
  Modded classpaths are not included into Rhino JS by default (this means that you cannot directly access the library from JS, you need some work for it).\
  For this very reason, you need to create a reference that you can use within your mod. To do this, append this to the end of your `main.js` script:
  ```javascript
  var JsonLibWrapper = null;
  Events.on(ClientLoadEvent, event => {
    let jsonLibMod = Vars.mods.getMod("pyguy.jsonlib");
  
    if (jsonLibMod)
    {
      if (jsonLibMod.enabled()) JsonLibWrapper = jsonLibMod.loader.loadClass("pyguy.jsonlib.JsonLibWrapper").newInstance();
    }
  
    if (JsonLibWrapper)
    {
      // Your code here
    }
  });
  ```
  
  After this is executed, JsonLibWrapper will have one of two values: `null` if the CustomJsonLib is not currently installed in Mindustry, or the API object that you can use to work with the library otherwise.
  Do note that only AFTER the client has loaded will JsonLibWrapper have a value, and if CustomJsonLib is not installed in Mindustry, it will not throw an error but rather it will not execute your code at all.
  
  3. **Use the library**
  
  Now the library is part of your project.\
  To know what methods this library supports, see [Using the Library](#using-the-library) below
</details>

<br/>

<details>
  <summary>I want to use JSON tags implemented by other Mods</summary>
  
  <br/>
  
  To add custom tags implemented by other Mods, follow this structure:

  
  Let's say you want to add to a block named `weigthedBomb` a tag called `weight` from a mod whose internal name is `physics-mod`, and a tag called `explosionSize` from a mod whose internal name is `super-explosions`.

  In your content's [h]json file you'd add the following:
  
  &nbsp;&nbsp;&nbsp;JSON (weightedBomb.json):
  ```json
  {
    "type": "Block",

    ...

    "customJson": [
      "physics-mod-weight": 45,
      "super-explosions-explosionSize": "huge"
    ]
  }
  ```
  &nbsp;&nbsp;&nbsp;HJSON (weightedBomb.hjson):
  ```
  type: Block

  ...

  customJson: [
    physics-mod-weight: 45,
    super-explosions-explosionSize: huge
  ]
  ```

  > IMPORTANT: `type` here is added for illustration purposes only. The only part that matters is the `customJson` array.

</details>

## Naming of Custom Tags

Even though there are no technical restrictions on the naming of tags, for the sake of everyone using this library (both users and developers) tags should be named in the following scheme:


`modname-tagname`

This makes it harder for there to be naming clashes between Mods that implement custom tags, or those that use it.


If you are developing a mod that adds custom tags, PLEASE disclose them and their functionality somewhere within the README of your own mod. People that want to use your tags do not want (and should not have) to scour your codebase to find the tags and see what they do. Use something like the following:

```markdown
## Custom JSON Tags

This mod supports Custom JSON Tags, implemented via the [CustomJsonLib](https://github.com/ThePythonGuy3/CustomJsonLib) by ThePythonGuy3.


If you want to support these in your own Mods, these are the tags implemented:

- `physics-mod-weight`: Indicates the weight in kg of your Block. (number)
- `physics-mod-volume`: Indicates the volume in L of your Block. (number)
- `physics-mod-objectType`: Indicates the type of physicsObject your Block is. Options: [`sphere`, `staticMesh`, `breakableObject`] (string)
```

## Building

This mod is, in theory, cross-platform, but only Windows has been tested. The only platform not supported is iOS, just because it does not allow for Jar mods to be installed.


### Support:
| Windows         | Linux                 | MacOS                 | Android               | iOS        |
|-----------------|-----------------------|-----------------------|-----------------------|------------|
| Fully supported | Untested, should work | Untested, should work | Untested, should work | No support |

### Building as a Library

If you want to include this mod as a library for your own Java mods (explained in [How do I use this library?](#how-do-i-use-this-library) above) you're going to need a library `jar` file.

The latest release can be found in Releases under the name `CustomJsonLib.jar`, but if you want to build your own, here are the steps:
1. Open your terminal, and `cd` to your local copy of the mod.
2. Ensure your internet connection is stable on the first or clean builds, as the project will try to fetch prerequisites from the internet.
3. Run `gradlew lib` *(replace `gradlew` with `./gradlew` on Mac/Linux)*. This should create a JAR inside `build/libs/` that you can copy over to your mod's `lib/` folder to use it.

### Desktop Build

Desktop builds are convenient for testing, but will obviously **not** work on Android, so never include this in your releases. Desktop JARs have `Desktop` suffixed to their name (`CustomJsonLibDesktop.jar`). Here's how you can build the mod:

1. Open your terminal, and `cd` to your local copy of the mod.
2. Ensure your internet connection is stable on the first or clean builds, as the project will try to fetch prerequisites from the internet.
3. Run `gradlew jar` *(replace `gradlew` with `./gradlew` on Mac/Linux)*. This should create a JAR inside `build/libs/` that you can copy over to the Mindustry mods folder to install it.
4. You can also then run `gradlew install` to automatically install the mod JAR, or even `gradlew jar install` to do both compiling and installing at once.

### Android Build

Android builds are automated on the CI hosted by GitHub Actions, so you should be able to just push a commit and wait for the CI to provide your build. If you still want to build locally, though, follow these steps.

#### Installing Android SDK
1. Install [Android SDK](https://developer.android.com/studio#command-line-tools-only), specifically the "**Command line tools only**" section. Download the tools that match your platform.
2. Unzip the Android SDK command line tools inside a folder; let's call it `AndroidSDK/` for now.
3. Inside this folder is a folder named `cmdline-tools/`. Put everything inside `cmdline-tools/` to a new folder named `latest/`, so that the folder structure looks like `AndroidSDK/cmdline-tools/latest/`.
4. Open your terminal, `cd` to the `latest/` folder.
5. Run `sdkmanager --install "platforms;android-35" "build-tools;35.0.0"`. These versions correspond to the `androidSdkVersion` and `androidBuildVersion` properties inside `gradle.properties`, which default to `35` and `35.0.0`, respectively.
6. Set environment variable `ANDROID_SDK_ROOT` as the full path to the `AndroidSDK/` folder you created, and restart your terminal to update the environments.

#### Building
1. Open your terminal, and `cd` to your local copy of the mod.
2. Ensure your internet connection is stable on the first or clean builds, as the project will try to fetch prerequisites from the internet.
3. Run `gradlew dex`. This should create a cross-platform JAR inside `build/libs/`, suffixed by `CrossPlatform` (`CustomJsonLibCrossPlatform.jar`) that you can copy over to the Mindustry mods folder to install it.
4. You can then copy the resulting artifact to your phone's Mindustry mods folder in its data directory, or use it in desktop.

## License

The project is licensed under [GNU GPL v3](/LICENSE).

## Credits

Thanks to [Glenn Folker](https://github.com/GlennFolker) for the Mindustry Mod Template and part of this README's Building instructions.
