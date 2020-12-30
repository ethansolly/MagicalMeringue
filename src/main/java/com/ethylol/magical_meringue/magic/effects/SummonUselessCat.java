package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonUselessCat implements ISpellEffect {
    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos pos) {
        if (!world.isRemote) {
            IManaHandler manaHandler = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandler != null && manaHandler.getMana(0) >= 4) {
                EntityOcelot ocelot = new EntityOcelot(world);
                ocelot.setPosition(caster.posX, caster.posY, caster.posZ);
                world.spawnEntity(ocelot);

                manaHandler.useMana(0, 4);
                MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), (EntityPlayerMP) caster);
            }
        }
    }

    @Override
    public String name() {
        return "Summon Useless Cat";
    }

    @Override
    public int tier() {
        return 0;
    }
}
