package com.ethylol.magical_meringue.magic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpellEffect {
    void onCast(EntityPlayer caster, World world, BlockPos pos);
    String name();
    int tier();
}
