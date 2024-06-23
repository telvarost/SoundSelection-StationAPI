package com.github.telvarost.soundselection;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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

    public static void readZip(String zipFilePath) {
        System.out.println("Hey whats the idea");
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                long compressedSize = entry.getCompressedSize();
                long normalSize = entry.getSize();
                if (!entry.isDirectory() && name.contains(".mcmeta")) {
//                    try (;
//                         Scanner scanner = new Scanner(inputStream);) {
//
//                        while (scanner.hasNextLine()) {
//                            String line = scanner.nextLine();
//                            System.out.println(line);
//                        }
//                    }
                    System.out.println(name);
                    System.out.format("\t %d - %d\n", compressedSize, normalSize);


                    try {
                        JsonObject packObject = Jankson
                                .builder()
                                .build()
                                .load(zipFile.getInputStream(entry));

                        System.out.println("Parsing");
                        if (null != packObject.getObject("pack")) {
                            JsonElement jsonElement = packObject.getObject("pack").getOrDefault("description", new JsonObject());
                            System.out.println(jsonElement.toString());
                            if (2 <= jsonElement.toString().length()) {
                                String description = jsonElement.toString().substring(1, jsonElement.toString().length() - 1);
                                System.out.println("From JSON: " + description);
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
