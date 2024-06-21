package com.github.telvarost.soundselection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String defaultSoundPacks[] = new String[] {"en_US"};

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
        updateAvailableSoundPacks();
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

        if (soundPackDir.exists() && soundPackDir.isDirectory())
        {
            File afile[] = soundPackDir.listFiles();
            File afile1[] = new File[afile.length + defaultSoundPacks.length];
            for(int i = 0; i < afile.length + defaultSoundPacks.length; i++) {
                afile1[i] = i < afile.length ? afile[i] : new File((SoundPackList.class).getResource("/assets/soundselection/lang/" + defaultSoundPacks[i - afile.length] + ".lang").getFile());
            }
            int i = afile1.length;

            titleAndAuthorMap.clear();
            for (int j = 0; j < i; j++)
            {
                File file = afile1[j];
                if(file == null) continue;

                if (file.getName().toLowerCase().endsWith(".lang"))
                {
                    try
                    {
                        InputStream in;
                        if(file.isFile()) {
                            in = new FileInputStream(file);
                        } else {
                            in = (SoundPackList.class).getResourceAsStream("/assets/soundselection/lang/" + file.getName());
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String title = reader.readLine();
                        String author = reader.readLine();
                        reader.close();
                        boolean hasHeading = title.contains("title|") && author.contains("author|");
                        if(hasHeading) {
                            title = title.replace("title|", "");
                            author = author.replace("author|", "");
                        } else {
                            System.out.println("File " + file.getName() + " is missing a title and/or author, or isn't a valid lang file.");
                            continue;
                        }
                        arraylist.add(file.getName().replace(".lang", ""));
                        titleAndAuthorMap.put(file.getName().replace(".lang", ""), new String[] {title, author});
                    }
                    catch (IOException ioexception)
                    {
                        ioexception.printStackTrace();
                    }
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
