package com.github.telvarost.soundselection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public class SoundPackList
{
    private final String defaultSoundPack = "";
    /** The list of the available texture packs. */
    private List<String> availableSoundPacks;
    private Map<String, String[]> titleAndAuthorMap = new HashMap<String, String[]>();

    /** The TexturePack that will be used. */
    public String selectedSoundPack;

    /** The Minecraft instance used by this TexturePackList */
    private Minecraft mc;

    /** The directory the texture packs will be loaded from. */
    private File soundPackDir;

    public SoundPackList(Minecraft par1Minecraft, File par2File)
    {
        availableSoundPacks = new ArrayList<String>();
        mc = par1Minecraft;
        soundPackDir = new File(par2File, "soundpacks");

        if (soundPackDir.exists())
        {
            if (!soundPackDir.isDirectory())
            {
                soundPackDir.delete();
                soundPackDir.mkdirs();
            }
        }
        else
        {
            soundPackDir.mkdirs();
        }
        //updateAvailableSoundPacks();
    }

    /**
     * Sets the new TexturePack to be used, returning true if it has actually changed, false if nothing changed.
     * @throws FileNotFoundException
     */
    public boolean setSoundPack(Screen screen, int k) throws FileNotFoundException
    {
        if (ModHelper.ModHelperFields.soundPack == availableSoundPacks.get(k)) {
            return false;
        } else {
            selectedSoundPack = ModHelper.ModHelperFields.soundPack;
            ModHelper.ModHelperFields.soundPack = availableSoundPacks.get(k);
            if (FabricLoader.getInstance().isModLoaded("stationapi")) {
                ModHelper.ModHelperFields.reloadingSounds = true;
                ModHelper.ModHelperFields.reloadingSounds = false;
            }
            ModHelper.loadSoundPack();
            mc.options.save();
            class_564 scaledresolution = new class_564(mc.options, mc.displayWidth, mc.displayHeight);
            int i = scaledresolution.method_1857();
            int j = scaledresolution.method_1858();
            screen.init(mc, i, j);
            return true;
        }
    }

    /**
     * check the sound packs the client has installed
     */
    public void updateAvailableSoundPacks()
    {
        ArrayList<String> arraylist = new ArrayList<String>();
        selectedSoundPack = ModHelper.ModHelperFields.soundPack;

        if (soundPackDir.exists() && soundPackDir.isDirectory()) {

            String description = "";

            try {
                JsonObject packObject = Jankson
                        .builder()
                        .build()
                        .load(new File("resources", "pack.mcmeta"));

                System.out.println("Parsing");
                if (null != packObject.getObject("pack")) {
                    JsonElement jsonElement = packObject.getObject("pack").getOrDefault("description", new JsonObject());
                    System.out.println(jsonElement.toString());
                    if (2 <= jsonElement.toString().length()) {
                        description = jsonElement.toString().substring(1, jsonElement.toString().length() - 1);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Couldn't read the config file" + ex.toString());
            } catch (SyntaxError error) {
                System.out.println("Couldn't read the config file" + error.getMessage());
                System.out.println(error.getLineMessage());
            }

            titleAndAuthorMap.clear();
            arraylist.add("Default");
            titleAndAuthorMap.put("Default", new String[]{"Default", description});

            File afile[] = soundPackDir.listFiles();
            for (int fileIndex = 0; fileIndex < afile.length; fileIndex++) {
                if (afile[fileIndex].getName().contains(".zip")) {
                    description = ModHelper.readZip(Paths.get(soundPackDir.getPath(), afile[fileIndex].getName()).toString());
                    arraylist.add("Pack" + fileIndex);
                    titleAndAuthorMap.put("Pack" + fileIndex, new String[]{"Pack" + fileIndex, description});
                }
            }
        }


        if (!arraylist.contains(selectedSoundPack))
        {
            selectedSoundPack = defaultSoundPack;
        }

        availableSoundPacks.clear();
        Collections.sort(arraylist, String.CASE_INSENSITIVE_ORDER);
        availableSoundPacks = arraylist;
    }

    /**
     * Returns a list of the available texture packs.
     */
    public List<String> availableSoundPacks()
    {
        return availableSoundPacks;
    }

    /**
     * Returns a map of the pack descriptions and authors.
     */
    public Map<String, String[]> packMetas()
    {
        return titleAndAuthorMap;
    }
}
