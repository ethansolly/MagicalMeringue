package com.ethylol.magical_meringue.capabilities.join;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

public interface IJoinHandler extends INBTSerializable<NBTBase> {
    boolean hasJoined();
    void setJoined(boolean joined);
}
