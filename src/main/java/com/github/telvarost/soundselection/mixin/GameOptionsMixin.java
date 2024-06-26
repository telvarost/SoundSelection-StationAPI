package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;

@Environment(value= EnvType.CLIENT)
@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    protected void buttonClicked(CallbackInfo ci) {
        ModHelper.loadSoundPack(false);
    }

    @Redirect(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/PrintWriter;close()V"
            )
    )
    public void saveOptions(PrintWriter instance) {
        instance.println("soundPack:" + ModHelper.ModHelperFields.soundPack);
        instance.close();
    }
}
