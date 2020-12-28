package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BreakBlock implements ISpellEffect {

    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos playerPos) {
        if (!world.isRemote) {
            IManaHandler manaHandler = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            RayTraceResult lookingAt = Minecraft.getMinecraft().objectMouseOver;
            if (lookingAt != null && lookingAt.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = lookingAt.getBlockPos();
                IBlockState blockState = world.getBlockState(pos);
                float cost = (float) Math.ceil(1.0f / (1.0f / blockState.getBlockHardness(world, pos) / 30.0f)) / 5.0f;
                MagicalMeringueCore.getLogger().debug("Cost = " + cost);
                if (manaHandler != null) {
                    MagicalMeringueCore.getLogger().debug("Mana: " + manaHandler.getMana(0));
                    if(manaHandler.getMana(0) >= cost) {
                        world.destroyBlock(pos, true);
                        manaHandler.useMana(0, cost);
                        MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), (EntityPlayerMP) caster);
                    }
                }
            }
        }
    }

    @Override
    public String name() {
        return "Break Block";
    }

    @Override
    public int tier() {
        return 0;
    }
}
