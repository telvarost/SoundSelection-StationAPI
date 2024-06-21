package com.github.telvarost.soundselection.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(SoundManager.class)
public abstract class SoundHelperMixin {

    @Shadow private int timeUntilNextSong;

    @Shadow private Random random;

    @Shadow public abstract void loadMusic(String string, File file);

    @Shadow public abstract void loadStreaming(String string, File file);

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void SoundHelper(CallbackInfo ci) {
        File mainMenuThemeDir = new File("main-menu-theme");
        if (mainMenuThemeDir.exists()) {
            if (!mainMenuThemeDir.isDirectory()) {
                mainMenuThemeDir.delete();
                mainMenuThemeDir.mkdirs();
            } else {
                System.out.println("Looking for mainmenu song in: main-menu-theme/");

                File [] oggFiles = mainMenuThemeDir.listFiles(f-> f.toString().endsWith(".ogg"));
                if (null != oggFiles) {
                    for (int fileIndex = 0; fileIndex < oggFiles.length; fileIndex++) {
                        System.out.println("Added: " + oggFiles[fileIndex].getName());
                        if (oggFiles[fileIndex].getName().contains("mainmenu")) {
                            loadStreaming(oggFiles[fileIndex].getName(), oggFiles[fileIndex]);
                        }
                    }
                }

                File [] musFiles = mainMenuThemeDir.listFiles(f-> f.toString().endsWith(".mus"));
                if (null != musFiles) {
                    for (int fileIndex = 0; fileIndex < musFiles.length; fileIndex++) {
                        System.out.println("Added: " + musFiles[fileIndex].getName());
                        if (musFiles[fileIndex].getName().contains("mainmenu")) {
                            loadStreaming(musFiles[fileIndex].getName(), musFiles[fileIndex]);
                        }
                    }
                }

                File [] wavFiles = mainMenuThemeDir.listFiles(f-> f.toString().endsWith(".wav"));
                if (null != wavFiles) {
                    for (int fileIndex = 0; fileIndex < wavFiles.length; fileIndex++) {
                        System.out.println("Added: " + wavFiles[fileIndex].getName());
                        if (wavFiles[fileIndex].getName().contains("mainmenu")) {
                            loadStreaming(wavFiles[fileIndex].getName(), wavFiles[fileIndex]);
                        }
                    }
                }
            }
        } else {
            mainMenuThemeDir.mkdirs();
        }

        File customMusicDir = new File("custom-music");
        if (customMusicDir.exists()) {
            if (!customMusicDir.isDirectory()) {
                customMusicDir.delete();
                customMusicDir.mkdirs();
            } else {
                System.out.println("Looking for songs in: custom-music/");

                File [] oggFiles = customMusicDir.listFiles(f-> f.toString().endsWith(".ogg"));
                if (null != oggFiles) {
                    for (int fileIndex = 0; fileIndex < oggFiles.length; fileIndex++) {
                        System.out.println("Added: " + oggFiles[fileIndex].getName());
                        loadMusic(oggFiles[fileIndex].getName(), oggFiles[fileIndex]);
                    }
                }

                File [] musFiles = customMusicDir.listFiles(f-> f.toString().endsWith(".mus"));
                if (null != musFiles) {
                    for (int fileIndex = 0; fileIndex < musFiles.length; fileIndex++) {
                        System.out.println("Added: " + musFiles[fileIndex].getName());
                        loadMusic(musFiles[fileIndex].getName(), musFiles[fileIndex]);
                    }
                }

                File [] wavFiles = customMusicDir.listFiles(f-> f.toString().endsWith(".wav"));
                if (null != wavFiles) {
                    for (int fileIndex = 0; fileIndex < wavFiles.length; fileIndex++) {
                        System.out.println("Added: " + wavFiles[fileIndex].getName());
                        loadMusic(wavFiles[fileIndex].getName(), wavFiles[fileIndex]);
                    }
                }
            }
        } else {
            customMusicDir.mkdirs();
        }

        this.timeUntilNextSong = this.random.nextInt(12000);//Config.config.musicCoundownRandomIntervalMax);
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(
                    intValue = 12000,
                    ordinal = 0
            )
    )
    private int quickAdditions_handleBackgroundMusicRandomIntervalMax(int constant) {
        return 12000;//Config.config.musicCoundownRandomIntervalMax;
    }


    @ModifyConstant(
            method = "tick",
            constant = @Constant(
                    intValue = 12000,
                    ordinal = 1
            )
    )
    private int quickAdditions_handleBackgroundMusicRandomIntervalMin(int constant) {
        return 12000;//Config.config.musicCoundownRandomIntervalMin;
    }
}
