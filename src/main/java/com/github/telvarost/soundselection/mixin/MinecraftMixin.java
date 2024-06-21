package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadSound(Ljava/lang/String;Ljava/io/File;)V"
            ),
            cancellable = true
    )
    public void loadSoundFromDirSound(String file, File par2, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            System.out.println("Removing sound: " + file);
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
    public void loadSoundFromDirStreaming(String file, File par2, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            System.out.println("Removing streaming sound: " + file);
            ci.cancel();
        }
    }

    @Inject(
            method = "loadResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundManager;loadMusic(Ljava/lang/String;Ljava/io/File;)V"
            ),
            cancellable = true
    )
    public void loadSoundFromDirMusic(String file, File par2, CallbackInfo ci) {
        if (!ModHelper.ModHelperFields.soundPack.isBlank()) {
            System.out.println("Removing default Minecraft background music.");
            ci.cancel();
        }
    }
}
