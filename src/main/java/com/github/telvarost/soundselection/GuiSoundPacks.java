package com.github.telvarost.soundselection;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import net.minecraft.client.resource.language.TranslationStorage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;

public class GuiSoundPacks extends Screen {

    protected Screen guiScreen;
    private int field_6454_o;
    public SoundPackList packlist;
    private GuiSoundPackSlot guiSoundPackSlot;

    public GuiSoundPacks(Screen guiscreen) {
        field_6454_o = -1;
        guiScreen = guiscreen;
    }

    public void init() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();
        String openFolder = translationStorage.get("soundPack.openFolder");
        buttons.add(new OptionButtonWidget(5, width / 2 - 154, height - 48,
                (openFolder.equals("soundPack.openFolder")) ? "Open sound pack folder" : openFolder
                ));
        buttons.add(new OptionButtonWidget(6, width / 2 + 4, height - 48,
                translationStorage.get("gui.done")));
        packlist = new SoundPackList(minecraft, Minecraft.getRunDirectory());
        packlist.updateAvailableSoundPacks();
        guiSoundPackSlot = new GuiSoundPackSlot(this);
        guiSoundPackSlot.registerButtons(buttons, 7, 8);
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 5) {
            //Sys.openURL((new StringBuilder()).append("file://").append(mc.getMinecraftDir()).append("/soundpacks").toString());
            try {
                Desktop.getDesktop().open(new File("soundpacks"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (guibutton.id == 6) {
            //minecraft.textureManager.method_1096();
            minecraft.setScreen(guiScreen);
        } else {
            guiSoundPackSlot.buttonClicked(guibutton);
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }

    protected void mouseReleased(int i, int j, int k) {
        super.mouseReleased(i, j, k);
    }

    public void render(int i, int j, float f) {
        guiSoundPackSlot.render(i, j, f);
        TranslationStorage translationStorage = TranslationStorage.getInstance();
        String title = translationStorage.get("soundPack.title");
        drawCenteredTextWithShadow(textRenderer,
                (title.equals("soundPack.title")) ? "Select Sound Pack" : title,
                width / 2, 10, 0xffffff);
        String subtitle = translationStorage.get("soundPack.subtitle");
        drawCenteredTextWithShadow(textRenderer,
                (subtitle.equals("soundPack.subtitle")) ? "Restart required for changes to take effect" : subtitle,
                width / 2, 21, 0x808080);
        String folderInfo = translationStorage.get("soundPack.folderInfo");
        drawCenteredTextWithShadow(textRenderer,
                (folderInfo.equals("soundPack.folderInfo")) ? "(Place sound pack files here)" : folderInfo,
                width / 2 - 77, height - 26, 0x808080);
        super.render(i, j, f);
//		guiTexturePackSlot.registerScrollButtons(controlList, 7, 8);
    }

    protected void keyPressed(char c, int i)
    {
        if(i == 1 && guiScreen instanceof TitleScreen)
        {
            minecraft.setScreen(guiScreen);
        } else {
            super.keyPressed(c, i);
        }
    }

    public void tick() {
        super.tick();
        field_6454_o--;
    }

    static Minecraft func_22124_a(GuiSoundPacks guisoundpacks) {
        return guisoundpacks.minecraft;
    }

    static TextRenderer func_22127_j(GuiSoundPacks guisoundpacks) {
        return guisoundpacks.textRenderer;
    }
}
