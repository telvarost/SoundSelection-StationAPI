package com.github.telvarost.soundselection;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;

public class GuiSoundPackSlot extends EntryListWidget {

    public GuiSoundPackSlot(GuiSoundPacks guiSoundPacks)
    {
        super(GuiSoundPacks.func_22124_a(guiSoundPacks), guiSoundPacks.width, guiSoundPacks.height, 32, (guiSoundPacks.height - 55) + 4, 26);
        parentSoundPackGui = guiSoundPacks;
    }

    @Override
    protected int getEntryCount()
    {
        List list = parentSoundPackGui.packlist.availableSoundPacks();
        return list.size();
    }

    @Override
    protected void entryClicked(int i, boolean flag)
    {
        try {
            parentSoundPackGui.packlist.setSoundPack(parentSoundPackGui, i);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isSelectedEntry(int i) {
        return parentSoundPackGui.packlist.availableSoundPacks().get(i).equals(ModHelper.ModHelperFields.soundPack);
    }

    @Override
    protected void renderBackground() {
        parentSoundPackGui.renderBackground();
    }

    @Override
    protected void renderEntry(int i, int j, int k, int l, Tessellator tessellator) {
        TextRenderer textRenderer = GuiSoundPacks.func_22127_j(parentSoundPackGui);
        List<String> list = parentSoundPackGui.packlist.availableSoundPacks();
        Map<String, String[]> map = parentSoundPackGui.packlist.packMetas();
        String title = map.get(list.get(i))[0];
        String author = map.get(list.get(i))[1];
        parentSoundPackGui.drawCenteredTextWithShadow(textRenderer, title, parentSoundPackGui.width / 2, k + 1, 0xffffff);
        parentSoundPackGui.drawCenteredTextWithShadow(textRenderer, author, parentSoundPackGui.width / 2, k + 12, 0x808080);
    }

    protected int getContentHeight()
    {
        return getEntryCount() * 26;
    }

    final GuiSoundPacks parentSoundPackGui; /* synthetic field */

}
