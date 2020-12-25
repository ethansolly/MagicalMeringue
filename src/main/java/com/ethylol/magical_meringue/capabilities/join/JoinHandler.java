package com.ethylol.magical_meringue.capabilities.join;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class JoinHandler implements IJoinHandler {

    boolean joined;

    @Override
    public boolean hasJoined() {
        return joined;
    }

    @Override
    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    @Override
    public NBTBase serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("joined", joined);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        joined = ((NBTTagCompound) nbt).getBoolean("joined");
    }
}
