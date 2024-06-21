package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.GuiButtonCustom;
import com.github.telvarost.soundselection.GuiSoundPacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameMenuScreen.class)
public class PauseMixin extends Screen {
    public PauseMixin() { }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init_return(CallbackInfo ci) {
        byte byte0 = -16;
        if (!FabricLoader.getInstance().isModLoaded("modmenu") && !FabricLoader.getInstance().isModLoaded("legacytranslations")) {
            buttons.add(new ButtonWidget(7, width / 2 - 100, height / 4 + 72 + byte0, 200, 20, I18n.getTranslation(("menu.texturepacks"))));
        }
        buttons.add(new GuiButtonCustom(8, width / 2 - 124, height / 4 + 72 + byte0, 20, 20, "", true, 2));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(ButtonWidget arg, CallbackInfo ci) {
        if (arg.id == 7) {
            if (!FabricLoader.getInstance().isModLoaded("modmenu") && !FabricLoader.getInstance().isModLoaded("legacytranslations")) {
                this.minecraft.setScreen(new PackScreen(this));
            }
        }

        if (arg.id == 8) {
            this.minecraft.setScreen(new GuiSoundPacks(this));
        }
    }
}
