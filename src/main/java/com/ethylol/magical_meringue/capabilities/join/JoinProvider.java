package com.ethylol.magical_meringue.capabilities.join;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JoinProvider implements ICapabilitySerializable<NBTBase> {

    private final IJoinHandler cap = new JoinHandler();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Capabilities.JOIN_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.JOIN_HANDLER_CAPABILITY)
            return Capabilities.JOIN_HANDLER_CAPABILITY.cast(cap);
        else return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        cap.deserializeNBT(nbt);
    }
}
