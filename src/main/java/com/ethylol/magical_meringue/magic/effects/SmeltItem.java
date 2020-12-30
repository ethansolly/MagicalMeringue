package com.ethylol.magical_meringue.magic.effects;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.magic.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SmeltItem implements ISpellEffect {
    @Override
    public void onCast(EntityPlayer caster, World world, BlockPos pos) {
        if (world.isRemote) {
            IManaHandler manaHandler = caster.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandler != null && manaHandler.getMana(0) >= 4) {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < caster.inventory.mainInventory.size(); i++) {
                    ItemStack stack = caster.inventory.mainInventory.get(i);
                    if (!FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty()) {
                        list.add(i);
                    }
                }
                if (!list.isEmpty()) {
                    int randIndex = list.get((int) (Math.random() * list.size()));
                    ItemStack smeltMe = caster.inventory.mainInventory.get(randIndex);
                    caster.inventory.mainInventory.remove(randIndex);
                    caster.inventory.mainInventory.add(randIndex, FurnaceRecipes.instance().getSmeltingResult(smeltMe));

                    manaHandler.useMana(0, 4);
                    MagicalMeringueCore.network.sendToServer(new ManaMessage(manaHandler));
                }
            }
        }
    }

    @Override
    public String name() {
        return "Smelt Item";
    }

    @Override
    public int tier() {
        return 0;
    }
}
