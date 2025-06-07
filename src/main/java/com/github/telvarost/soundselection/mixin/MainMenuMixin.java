package com.github.telvarost.soundselection.mixin;

import com.github.telvarost.soundselection.GuiButtonCustom;
import com.github.telvarost.soundselection.GuiSoundPacks;
import com.github.telvarost.soundselection.ModHelper;
import com.github.telvarost.soundselection.ModHelperCryonicConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class MainMenuMixin extends Screen {

    public MainMenuMixin() { }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init_return(CallbackInfo ci) {
        int i = this.height / 4 + 48;

        if (FabricLoader.getInstance().isModLoaded("cryonicconfig")) {
            ModHelper.ModHelperFields.displayGui = ModHelperCryonicConfig.loadDisplayGui();
        } else {
            ModHelper.ModHelperFields.displayGui = true;
        }

        if (ModHelper.ModHelperFields.displayGui) {
            buttons.add(new GuiButtonCustom(49, width / 2 - 124, i + 48, 20, 20, I18n.getTranslation(""), true, 2));
        }
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(ButtonWidget arg, CallbackInfo ci) {
        if (ModHelper.ModHelperFields.displayGui) {
            if (arg.id == 49) {
                this.minecraft.setScreen(new GuiSoundPacks(this));
            }
        }
    }
}
