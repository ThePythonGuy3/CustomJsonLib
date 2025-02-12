# Custom JSON Lib for Mindustry

Allows [Mindustry](https://github.com/Anuken/Mindustry) Mods to add and read custom JSON tags to act as libraries for other Mindustry Mods.

## How do I use this library?

<details>
<summary>I want to implement new JSON tags</summary>


</details>

<br/>

<details>
<summary>I want to use JSON tags implemented by other Mods</summary>
</details>

## Building

Mindustry Java mods are cross-platform, supporting PC (Windows, Mac, Linux) and Android. This section describes how to build the JARs for both PC and Android. Building these JARs are done through the usage of terminals: `cmd.exe` in Windows, Terminal in Mac, and if you're either on Linux or using a terminal emulator on Android such as Termux, you should already know what you're doing anyway. Following these steps should require basic terminal functionality such as `cd`.

### Desktop Build

Desktop builds are convenient for testing, but will obviously **not** work on Android, so never include this in your releases. Desktop JARs have `Desktop` suffixed to their name, e.g. `ModTemplateDesktop.jar`. Here's how you can build the mod:
1. Open your terminal, and `cd` to your local copy of the mod.
2. Ensure your internet connection on first or clean builds, as the project will try to fetch prerequisites from the internet.
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
2. Run `gradlew dex`. This should create a cross-platform JAR inside `build/libs/` that isn't suffixed with `Desktop` that you can copy over to the Mindustry mods folder to install it.
3. You can then copy the resulting artifact to your phone's Mindustry mods folder in its data directory.

## Adding Dependencies

**Never** use `implementation` for Mindustry/Arc groups and their submodules. There's a reason they're `compileOnly`; they're only present in compilation and excluded from the final JARs, as on runtime they're resolved from the game instance itself. Other JAR-mod dependencies must also use `compileOnly`. Only ever use `implementation` for external Java libraries that must be bundled with your mod.

## License

The project is licensed under [GNU GPL v3](/LICENSE).
