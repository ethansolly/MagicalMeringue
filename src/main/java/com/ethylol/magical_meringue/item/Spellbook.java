package com.ethylol.magical_meringue.item;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.client.gui.CreativeTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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

    /** TODO add GUI functionality
     *
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            Block block = worldIn.getBlockState(pos).getBlock();
            return null;
        }
        return null;
    }
   */



    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flags) {
        NBTTagCompound nbt;
        if (itemStack.hasTagCompound())
            nbt = itemStack.getTagCompound();
        else
            nbt = new NBTTagCompound();
        if (nbt.hasKey("boundTo")) {
            EntityPlayer playerBoundTo = world.getPlayerEntityByUUID(nbt.getUniqueId("boundTo"));
            tooltip.add(("" + I18n.format("magical_meringue.bound_to") + " " + playerBoundTo.getName()));
        }
    }
}
