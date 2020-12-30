package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class Levitate implements ISpellEffect {

    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos pos) {
        if (world.isRemote) {
            RayTraceResult lookingAt = Minecraft.getMinecraft().objectMouseOver;
            if (lookingAt != null && lookingAt.typeOfHit == RayTraceResult.Type.ENTITY) {
                MagicalMeringueCore.network.sendToServer(new RayTraceMessage(lookingAt, RayTraceMessage.MessageType.LEVITATE));
            }
        }
    }

    @Override
    public String name() {
        return "Levitate";
    }

    @Override
    public int tier() {
        return 0;
    }
}
