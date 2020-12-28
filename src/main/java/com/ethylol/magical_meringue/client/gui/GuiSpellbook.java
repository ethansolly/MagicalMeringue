package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.magic.Spell;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSpellbook extends GuiScreen {

    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(MagicalMeringueCore.MODID, "textures/gui/spellbook.png");
    private EntityPlayer player;
    private ItemStack bookStack;
    private int level;
    private int currPage;

    private GuiButton buttonExit;
    private GuiButton buttonLeft;
    private GuiButton buttonRight;

    private List<GuiButton> spells;
    private List<Spell> list;

    private GuiButton selectedButton;

    public GuiSpellbook(EntityPlayer player, ItemStack bookStack) {
        this.player = player;
        this.bookStack = bookStack;

        IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        if (manaHandler != null) {
            level = manaHandler.getLvl();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        this.buttonLeft = this.addButton(new GuiButton(0, this.width/2 - 100, height-50, 90, 20, "Left"));
        this.buttonRight = this.addButton(new GuiButton(1, this.width/2 + 100, height-50, 90, 20, "Right"));
        this.buttonExit = this.addButton(new GuiButton(2, this.width-100, 50, 90, 20, "Exit"));

        currPage = 0;

        refreshSpells();

        this.buttonLeft.visible = false;
        if (level == 1) {
            this.buttonRight.visible = false;
        }
    }

    private void refreshSpells() {
        spells = new ArrayList<>();
        list = Spell.list;
        list.removeIf(s -> s.getEffect().tier() != currPage);
        for (int i = 0; i < list.size(); i++) {
            Spell s = list.get(i);
            this.spells.add(this.addButton(new GuiButton(3+i, width/2-150+36, 34+i, 90, 20, s.getEffect().name())));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                //left
                if (currPage != 0) {
                    currPage--;
                    refreshSpells();
                }
            }
            if (button.id == 1) {
                //right
                if (currPage != level-1) {
                    currPage++;
                    refreshSpells();
                }
            }
            if (button.id == 2) {
                //exit
                this.mc.displayGuiScreen((GuiScreen) null);
            }
            if (button.id >= 3) {
                if (button != selectedButton) {
                    //select new button
                    Spell selected = list.get(button.id - 3);
                    NBTTagCompound compound = bookStack.getTagCompound();
                    compound.setString("spell", selected.getEffect().name());
                    bookStack.setTagCompound(compound);
                    button.packedFGColour = Utils.colorFromHexString("0000FF");
                    if (selectedButton != null) {
                        selectedButton.packedFGColour = 0;
                    }
                    selectedButton = button;
                }
                else {
                    //deselect current button
                    NBTTagCompound compound = bookStack.getTagCompound();
                    compound.removeTag("spell");
                    bookStack.setTagCompound(compound);
                    selectedButton.packedFGColour = 0;
                    selectedButton = null;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        this.drawTexturedModalRect(width/2-150, 2, 0, 0, 300, 192);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
