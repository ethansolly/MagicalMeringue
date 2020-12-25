package com.ethylol.magical_meringue.capabilities.mana;

import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class ManaHandler implements IManaHandler {

    int lvl;
    float[] mana;

    public ManaHandler() {
        lvl = 1;
        mana = new float[IManaHandler.MAX_TIER];
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = Utils.maxMana(i, lvl);
        }
    }

    @Override
    public void addMana(int tier, float amt) {
        mana[tier] += amt;
    }

    @Override
    public void useMana(int tier, float amt) {
        mana[tier] -= amt;
    }

    @Override
    public float getMana(int tier) {
        return mana[tier];
    }

    @Override
    public void setMana(int tier, float amt) {
        mana[tier] = amt;
    }

    @Override
    public int getLvl() {
        return lvl;
    }

    @Override
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    //The tag mana_i and its corresponding entry mana[i] correspond to the (i+1)-th tier.

    @Override
    public NBTBase serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("lvl", lvl);
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            compound.setFloat("mana_" + i, mana[i]);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        lvl = compound.getInteger("lvl");
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = compound.getFloat("mana_" + i);
        }
    }
}
