package com.ethylol.magical_meringue.item;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.client.gui.CreativeTab;
import com.ethylol.magical_meringue.client.gui.GuiSpellbook;
import com.ethylol.magical_meringue.magic.Spell;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Spellbook extends Item {



    public Spellbook() {
        setUnlocalizedName(MagicalMeringueCore.MODID + ":spellbook");
        setRegistryName(new ResourceLocation(MagicalMeringueCore.MODID, "spellbook"));
        setMaxStackSize(1);
        setCreativeTab(CreativeTab.TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        ItemStack stack = player.getHeldItemMainhand();

        //Check if book is bound to player
        if (player.isSneaking()) {
            if (!stack.hasTagCompound()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("boundTo", player.getName());
                stack.setTagCompound(compound);
            } else {
                NBTTagCompound compound = stack.getTagCompound();
                String name = compound.getString("boundTo");
                if (player.getName().equals(name)) {
                    if (worldIn.isRemote) {
                        Minecraft mc = Minecraft.getMinecraft();
                        mc.displayGuiScreen(new GuiSpellbook(player, stack));
                    }
                }
            }
        }
        else {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("spell")) {
                String name = stack.getTagCompound().getString("spell");
                List<Spell> spells = Spell.list;
                for (Spell spell : spells) {
                    if (spell.getEffect().name().equals(name)) {
                        spell.getEffect().onCast(player, worldIn, new BlockPos(player.posX, player.posY, player.posZ));
                    }
                }
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flags) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            String s = nbt.getString("boundTo");
            if (!StringUtils.isNullOrEmpty(s)) {
               //MagicalMeringueCore.getLogger().debug("------------BOUND TO???----------------");
                tooltip.add("Bound to " + s);
            }

            String spellName = nbt.getString("spell");
            if (!StringUtils.isNullOrEmpty(s)) {
                //MagicalMeringueCore.getLogger().debug("------------BOUND TO???----------------");
                tooltip.add("Current spell: " + spellName);
            }
        }
    }
}
