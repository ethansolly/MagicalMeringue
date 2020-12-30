package com.ethylol.magical_meringue.network;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class RayTraceMessageHandler implements IMessageHandler<RayTraceMessage, IMessage> {

    @Override
    public IMessage onMessage(RayTraceMessage message, MessageContext ctx) {

        EntityPlayerMP playerMP = ctx.getServerHandler().player;
        IManaHandler manaHandler = playerMP.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        if (manaHandler != null) {
            World w = playerMP.world;
            BlockPos pos = new BlockPos(message.getVec3d());

            if (message.getMessageType() == RayTraceMessage.MessageType.BREAK_BLOCK) {

                //Break Block
                IBlockState blockState = w.getBlockState(pos);
                if (blockState.getBlockHardness(w, pos) != -1.0f) {
                    float cost = (float) Math.ceil(1.0f / (1.0f / blockState.getBlockHardness(w, pos) / 30.0f)) / 5.0f;
                    if (manaHandler.getMana(0) >= cost) {
                        playerMP.getServerWorld().addScheduledTask(() -> w.destroyBlock(pos, true));
                        manaHandler.useMana(0, cost);
                        MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), playerMP);
                    }
                }
            } else if (message.getMessageType() == RayTraceMessage.MessageType.LEVITATE) {

                //Levitate
                List<Entity> list = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
                for (Entity e : list) {
                    if (manaHandler.getMana(0) >= 1) {
                        if (e instanceof EntityLiving) {
                            playerMP.getServerWorld().addScheduledTask(() -> ((EntityLiving) e).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200)));
                            manaHandler.useMana(0, 1);
                            MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), playerMP);
                        }
                    }
                    else break;
                }
            }
        }
        return null;
    }

}
