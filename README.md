# SoundSelection for Minecraft Beta 1.7.3

A babric mod for Minecraft Beta 1.7.3 that adds custom sound loading capabilities to the game similar to texture packs. Special thanks to Spring Spring for the sound icon and to NFC team for the pack list GUI.

# Sound Selection
Mod works on Multiplayer as it is completely clientside!

* GUI icon can be disabled using [CryonicConfig](https://modrinth.com/mod/cryonicconfig).
  * After loading the mod and selecting your desired soundpack, go to the config folder and change `displayGUI` in the `soundselection.json` file to `false`.
* Adds ability to add sound packs to the game.
  * On first run of the mod a folder named `soundpacks` will be created.
  * To make a sound pack copy the structure of the `resources` folder and edit whatever you want, once finished zip the pack and remove sounds you did not change.
    * Example: [https://github.com/telvarost/SoundSelection-StationAPI/tree/main/example]
  * Default Minecraft sounds will be used if a sound is not found in the sound pack.
  * `pack.mcmeta` is used to get the pack `description` and `title`, if no title is provided the zip name will be used as the title.
  * When the mod is run, click the sound icon to go to the sound pack selection screen.
  * The sounds will be extracted to `resources-soundpack` when a sound pack is selected.
  * Unfortunately after you select your desired sound pack you must restart the game for the new sounds to load.

## Installation using Prism Launcher

1. Download an instance of Babric for Prism Launcher: https://github.com/Glass-Series/babric-prism-instance
2. Install Java 17 and set the instance to use it: https://adoptium.net/temurin/releases/
3. (Optional) Add CryonicConfig to the mod folder for the instance: https://modrinth.com/mod/cryonicconfig
4. (Optional) Add StationAPI to the mod folder for the instance: https://modrinth.com/mod/stationapi
5. (Optional) Add Mod Menu to the mod folder for the instance: https://modrinth.com/mod/modmenu-beta
6. Add this mod to the mod folder for the instance: https://github.com/telvarost/BetaTweaks-StationAPI/releases
7. Run and enjoy! 👍

## Feedback

Got any suggestions on what should be added next? Feel free to share it by [creating an issue](https://github.com/telvarost/SoundSelection-StationAPI/issues/new). Know how to code and want to do it yourself? Then look below on how to get started.

## Contributing

Thanks for considering contributing! To get started fork this repository, make your changes, and create a PR.

If you are new to StationAPI consider watching the following videos on Babric/StationAPI Minecraft modding: https://www.youtube.com/watch?v=9-sVGjnGJ5s&list=PLa2JWzyvH63wGcj5-i0P12VkJG7PDyo9T
