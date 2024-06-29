package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Scanner;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow public SoundManager soundManager;

    @Unique private static boolean starting = true;

    @Inject(
            method = "loadResource",
            at = @At("HEAD"),
            cancellable = true
    )
    public void soundSelection_loadResource(String path, File file, CallbackInfo ci) {
       if (starting) {
           starting = false;

           try {
               File packResourcesDir = new File(Minecraft.getRunDirectory(), ModHelper.resourcesString);
               File soundPackDir = new File(Minecraft.getRunDirectory(), "soundpacks");
               File selectedSoundData = new File(Paths.get(packResourcesDir.getPath(), ModHelper.ModHelperFields.soundPack + ".dat").toString());

               /** - Read sound pack data if file exists */
               Instant instant = Instant.MIN;
               if (selectedSoundData.exists()) {
                   Scanner myReader = new Scanner(selectedSoundData);
                   while (myReader.hasNextLine()) {
                       String data = myReader.nextLine();
                       instant = Instant.parse(data);
                       break;
                   }
                   myReader.close();
               }
               FileTime fileTime = FileTime.from(instant);

               /** - Compare stored sound pack data with sound pack in soundpacks folder */
               BasicFileAttributes attr = Files.readAttributes(Paths.get(soundPackDir.getPath(), ModHelper.ModHelperFields.soundPack + ".zip"), BasicFileAttributes.class);
               if (!selectedSoundData.exists() || (0 != attr.lastModifiedTime().compareTo(fileTime))) {
                   /** - Load sound pack */
                   ModHelper.loadSoundPack(true);

                    /** - Store some data about loaded sound pack */
                   selectedSoundData.createNewFile();
                   FileWriter myWriter = new FileWriter(selectedSoundData);
                   myWriter.write(attr.lastModifiedTime().toString());
                   myWriter.close();
               }
           } catch (IOException ioException) {
               /** - An error occured use default Minecraft sounds instead */
               ModHelper.ModHelperFields.soundPack = "";
               ModHelper.loadSoundPack(true);
               System.out.println("An error occurred when trying to load selected sound pack. Switching to Minecraft default sounds.");
               ioException.printStackTrace();
           }
       }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadSound(Ljava/lang/String;Ljava/io/File;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void soundSelection_loadSoundFromDirSound(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File soundFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "sound", path).toString());
            if (soundFile.exists()) {
                //System.out.println("Adding sound: " + soundFile);
                //System.out.println("Loaded: " + path);
                soundManager.loadSound(path, soundFile);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadSound(Ljava/lang/String;Ljava/io/File;)V",
                    ordinal = 1
            ),
            cancellable = true
    )
    public void soundSelection_loadSoundFromDirNewSound(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File soundFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "newsound", path).toString());
            if (soundFile.exists()) {
                //System.out.println("Adding sound: " + soundFile);
                //System.out.println("Loaded: " + path);
                soundManager.loadSound(path, soundFile);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadStreaming(Ljava/lang/String;Ljava/io/File;)V"
            ),
            cancellable = true
    )
    public void soundSelection_loadSoundFromDirStreaming(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File streamingFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "streaming", path).toString());
            if (streamingFile.exists()) {
                //System.out.println("Adding streaming: " + streamingFile);
                //System.out.println("Loaded: " + path);
                soundManager.loadStreaming(path, streamingFile);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadMusic(Ljava/lang/String;Ljava/io/File;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void soundSelection_loadSoundFromDirMusic(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File musicFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "music", path).toString());
            if (musicFile.exists()) {
                //System.out.println("Adding music: " + musicFile);
                //System.out.println("Loaded: " + path);
                soundManager.loadMusic(path, musicFile);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadMusic(Ljava/lang/String;Ljava/io/File;)V",
                    ordinal = 1
            ),
            cancellable = true
    )
    public void soundSelection_loadSoundFromDirNewMusic(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File musicFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "newmusic", path).toString());
            if (musicFile.exists()) {
                //System.out.println("Adding music: " + musicFile);
                //System.out.println("Loaded: " + path);
                soundManager.loadMusic(path, musicFile);
                ci.cancel();
            }
        }
    }
}
