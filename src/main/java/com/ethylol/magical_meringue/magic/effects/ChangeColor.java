package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChangeColor implements ISpellEffect {
    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos pos) {
        if (world.isRemote) {
            IManaHandler manaHandler = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandler != null && manaHandler.getMana(0) >= 2) {
                for (ItemStack stack : caster.inventory.armorInventory) {
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
                        ((ItemArmor) stack.getItem()).setColor(stack, (int)(Math.random()*16777215));
                    }
                }

                manaHandler.useMana(0, 2);
                MagicalMeringueCore.network.sendToServer(new ManaMessage(manaHandler));
            }
        }
    }

    @Override
    public String name() {
        return "Change Color";
    }

    @Override
    public int tier() {
        return 0;
    }
}
