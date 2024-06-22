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
        } catch (IOException ex) {
            System.out.println("Failed to make resource warning file letting users know resources-soundpack is not a directory that users should modify the contents of");
        }

        unzip( Paths.get(soundPackDir.getPath(), ModHelperFields.soundPack).toString()
             , Paths.get(Minecraft.getRunDirectory().getPath(), resourcesString).toString()
             );

        ModHelperFields.reloadingSounds = false;
    }

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ModHelperFields {
        public static String soundPack = "";
        public static Boolean reloadingSounds = true;
    }
}
