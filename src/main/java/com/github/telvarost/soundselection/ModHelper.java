package com.github.telvarost.soundselection;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModHelper {

    private static final String resourcesString = "resources-soundpack";

    public static void loadSoundPack() {
        ModHelperFields.reloadingSounds = true;
        File soundPackDir = new File(Minecraft.getRunDirectory(), "soundpacks");

        File resourceSoundPackDir = new File(resourcesString);
        deleteDirectory(resourceSoundPackDir);
        if (!resourceSoundPackDir.exists()) {
            resourceSoundPackDir.mkdirs();
        }

        File resourceSoundPackFile = new File(Paths.get(resourcesString, "READ_ME_I_AM_VERY_IMPORTANT").toString());
        if (resourceSoundPackFile.exists()) {
            resourceSoundPackFile.delete();
        }


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



            unzip( Paths.get(soundPackDir.getPath(), ModHelperFields.soundPack).toString()
                 , Paths.get(Minecraft.getRunDirectory().getPath(), resourcesString).toString()
                 );
        } catch (IOException ex) {
            System.out.println("Failed to make resource warning file letting users know resources-soundpack is not a directory that users should modify the contents of");
        }

        ModHelperFields.reloadingSounds = false;
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
    }
}
