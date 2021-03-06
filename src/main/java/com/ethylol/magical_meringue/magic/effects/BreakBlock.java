package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BreakBlock implements ISpellEffect {

    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos playerPos) {
        if (world.isRemote) {
            RayTraceResult lookingAt = Minecraft.getMinecraft().objectMouseOver;
            if (lookingAt != null && lookingAt.typeOfHit == RayTraceResult.Type.BLOCK) {
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(lookingAt, RayTraceMessage.MessageType.BREAK_BLOCK));
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
