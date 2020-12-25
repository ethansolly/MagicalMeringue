package com.ethylol.magical_meringue.capabilities.mana;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

public interface IManaHandler extends INBTSerializable<NBTBase> {

    int MAX_TIER = 10;

    void addMana(int tier, float amt);
    void useMana(int tier, float amt);

    float getMana(int tier);
    void setMana(int tier, float amt);

    int getLvl();
    void setLvl(int lvl);
}
