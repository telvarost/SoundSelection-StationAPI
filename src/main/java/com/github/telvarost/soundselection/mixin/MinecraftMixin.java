package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.nio.file.Paths;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow public SoundManager soundManager;

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadSound(Ljava/lang/String;Ljava/io/File;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void loadSoundFromDirSound(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File soundFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "sound", path).toString());
            //System.out.println("Removing sound: " + soundFile);
            if (soundFile.exists()) {
                soundManager.loadStreaming(path, soundFile);
                //System.out.println("Loaded: " + path);
            }
            ci.cancel();
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
    public void loadSoundFromDirNewSound(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File soundFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "newsound", path).toString());
            //System.out.println("Removing sound: " + soundFile);
            if (soundFile.exists()) {
                soundManager.loadStreaming(path, soundFile);
                //System.out.println("Loaded: " + path);
            }
            ci.cancel();
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
    public void loadSoundFromDirStreaming(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File streamingFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "streaming", path).toString());
            //System.out.println("Removing streaming sound: " + streamingFile);
            if (streamingFile.exists()) {
                soundManager.loadStreaming(path, streamingFile);
                //System.out.println("Loaded: " + path);
            }
            ci.cancel();
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
    public void loadSoundFromDirMusic(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File musicFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "music", path).toString());
            //System.out.println("Removing music: " + musicFile);
            if (musicFile.exists()) {
                soundManager.loadStreaming(path, musicFile);
                //System.out.println("Loaded: " + path);
            }
            ci.cancel();
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
    public void loadSoundFromDirNewMusic(String path, File file, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            File musicFile = new File(Paths.get(Minecraft.getRunDirectory().getPath(), ModHelper.resourcesString, "newmusic", path).toString());
            //System.out.println("Removing music: " + musicFile);
            if (musicFile.exists()) {
                soundManager.loadStreaming(path, musicFile);
                //System.out.println("Loaded: " + path);
            }
            ci.cancel();
        }
    }
}
