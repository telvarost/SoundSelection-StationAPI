package com.github.telvarost.soundselection;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ModHelper {

    private static boolean starting = true;
    public static final String resourcesString = "resources-soundpack";
    public static final String MOD_ID = "soundselection";

    public static void loadSoundPack(boolean unzipPack) {
        ModHelperFields.reloadingSounds = true;

        File settings = new File("options.txt");
        if(starting) {
            System.out.println("Getting sound pack selection...");
            if(!settings.exists())
            {
                if(ModHelperFields.soundPack == null || !ModHelper.ModHelperFields.soundPack.equals("")) {
                    ModHelper.ModHelperFields.soundPack = "";
                    System.err.println("Had trouble finding options.txt, using default sounds instead.");
                }
            } else {
                try {
                    BufferedReader bufferedreader = new BufferedReader(new FileReader(settings));
                    boolean closed = false;
                    for(String s = ""; (s = bufferedreader.readLine()) != null;)
                    {
                        String as[] = s.split(":");
                        if (1 < as.length) {
                            if(as[0].equals("soundPack"))
                            {
                                ModHelper.ModHelperFields.soundPack = as[1];
                                bufferedreader.close();
                                closed = true;
                                break;
                            }
                        }
                    }
                    if(!closed) {
                        ModHelperFields.soundPack = "";
                        bufferedreader.close();
                        System.out.println("\"soundPack:\" option is missing from options.txt. It will be created on the next save.");
                    }
                } catch(IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }
        starting = false;

        File soundPackDir = new File(Minecraft.getRunDirectory(), "soundpacks");
        File resourceSoundPackDir = new File(resourcesString);

        if (unzipPack) {
            System.out.println("Loading sound pack files...");
            deleteDirectory(resourceSoundPackDir);
        }

        if (!resourceSoundPackDir.exists()) {
            resourceSoundPackDir.mkdirs();
        }

        File resourceSoundPackFile = new File(Paths.get(resourcesString, "READ_ME_I_AM_VERY_IMPORTANT").toString());

        try {
            resourceSoundPackFile.createNewFile();
            FileWriter myWriter = new FileWriter(resourceSoundPackFile);
            myWriter.write(" _    _  ___  ______ _   _ _____ _   _ _____ \n" +
                    "| |  | |/ _ \\ | ___ \\ \\ | |_   _| \\ | |  __ \\\n" +
                    "| |  | / /_\\ \\| |_/ /  \\| | | | |  \\| | |  \\/\n" +
                    "| |/\\| |  _  ||    /| . ` | | | | . ` | | __ \n" +
                    "\\  /\\  / | | || |\\ \\| |\\  |_| |_| |\\  | |_\\ \\\n" +
                    " \\/  \\/\\_| |_/\\_| \\_\\_| \\_/\\___/\\_| \\_/\\____/\n" +
                    "\n" +
                    "(Sorry about the cheesy 90s ASCII art.)\n" +
                    "\n" +
                    "Everything in this folder that does not belong here will be deleted.\n" +
                    "This folder will be kept sync with the Launcher at every run.\n" +
                    "If you wish to modify assets/resources in any way, use Resource Packs.\n" +
                    "\n" +
                    "\n" +
                    "Ta,\n" +
                    "Dinnerbone of Mojang");
            myWriter.close();
        } catch (IOException ex) {
            System.out.println("Failed to make resource warning file letting users know resources-soundpack is not a directory that users should modify the contents of");
        }

        try {
            if (!ModHelperFields.soundPack.equals("")) {
                if (unzipPack) {
                    unzip( Paths.get(soundPackDir.getPath(), ModHelperFields.soundPack + ".zip").toString()
                         , Paths.get(Minecraft.getRunDirectory().getPath(), resourcesString).toString()
                    );
                }
            }
        } catch (IOException ex) {
            System.out.println("Failed to copy sounds");
        }

        ModHelperFields.reloadingSounds = false;
    }

    public static String[] readZip(String zipFilePath) {
        String[] stringList = new String[2];
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!entry.isDirectory() && name.contains(".mcmeta")) {
                    try {
                        JsonObject packObject = Jankson
                                .builder()
                                .build()
                                .load(zipFile.getInputStream(entry));

                        if (null != packObject.getObject("pack")) {

                            JsonElement jsonElementTitle = packObject.getObject("pack").getOrDefault("title", new JsonObject());
                            if (2 <= jsonElementTitle.toString().length()) {
                                stringList[0] = jsonElementTitle.toString().substring(1, jsonElementTitle.toString().length() - 1);
                            }

                            JsonElement jsonElementDescription = packObject.getObject("pack").getOrDefault("description", new JsonObject());
                            if (2 <= jsonElementDescription.toString().length()) {
                                stringList[1] = jsonElementDescription.toString().substring(1, jsonElementDescription.toString().length() - 1);
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Couldn't read the config file" + ex.toString());
                    } catch (SyntaxError error) {
                        System.out.println("Couldn't read the config file" + error.getMessage());
                        System.out.println(error.getLineMessage());
                    }
                }
            }

            zipFile.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return stringList;
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }


    // Method to unzip files
    public static void unzip(String zipFile, String destFolder) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destFolder + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    public static class ModHelperFields {
        public static String soundPack = "";
        public static Boolean reloadingSounds = true;
        public static Boolean displayGui = true;
    }
}
