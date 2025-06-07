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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ScreenScaler;

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
//            if (FabricLoader.getInstance().isModLoaded("stationapi")) {
//                ModHelper.ModHelperFields.reloadingSounds = true;
//                ModHelper.ModHelperFields.reloadingSounds = false;
//            }
            ModHelper.loadSoundPack(false);
            mc.options.save();
            ScreenScaler scaledresolution = new ScreenScaler(mc.options, mc.displayWidth, mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
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

            String[] stringList = new String[]{"Default", ""};

            try {
                JsonObject packObject = Jankson
                        .builder()
                        .build()
                        .load(new File("resources", "pack.mcmeta"));

                if (null != packObject.getObject("pack")) {
                    JsonElement jsonElement = packObject.getObject("pack").getOrDefault("description", new JsonObject());
                    if (2 <= jsonElement.toString().length()) {
                        stringList[1] = jsonElement.toString().substring(1, jsonElement.toString().length() - 1);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Couldn't read the config file" + ex.toString());
            } catch (SyntaxError error) {
                System.out.println("Couldn't read the config file" + error.getMessage());
                System.out.println(error.getLineMessage());
            }

            titleAndAuthorMap.clear();
            arraylist.add("");
            titleAndAuthorMap.put("", stringList);

            File afile[] = soundPackDir.listFiles();
            for (int fileIndex = 0; fileIndex < afile.length; fileIndex++) {
                if (afile[fileIndex].getName().contains(".zip")) {
                    stringList = ModHelper.readZip(Paths.get(soundPackDir.getPath(), afile[fileIndex].getName()).toString());
                    String fileName = afile[fileIndex].getName().substring(0, afile[fileIndex].getName().length() - 4);
                    arraylist.add(fileName);
                    if (stringList[0].isBlank()) {
                        stringList[0] = fileName;
                    }
                    titleAndAuthorMap.put(fileName, stringList);
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
