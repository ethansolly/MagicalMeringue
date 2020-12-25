package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    public static final CreativeTabs TAB = new CreativeTab();

    public CreativeTab() {
        super(MagicalMeringueCore.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.spellbook);
    }
}
