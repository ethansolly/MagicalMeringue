package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.*;
import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinProvider;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaProvider;
import com.ethylol.magical_meringue.item.ModItems;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod.EventBusSubscriber
public class EventHandler {

    //Registration

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        registerItem(new Spellbook(), event);
    }

    private void registerItemBlock(Block block, RegistryEvent.Register<Item> event) {
        Item itemBlock = new ItemBlock(block).setRegistryName(block.getRegistryName());
        event.getRegistry().registerAll(itemBlock);
        registerItemModel(itemBlock);
    }

    private void registerItem(Item item, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(item);
        registerItemModel(item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item parItem)
    {
        registerItemModel(parItem, 0);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    //Capabilities


    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "mana"), new ManaProvider());
            event.addCapability(new ResourceLocation(MagicalMeringueCore.MODID, "join"), new JoinProvider());
        }
    }


    //Other Events

    @SubscribeEvent
    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getWorld().isRemote) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            //Add spellbook
            IJoinHandler joinHandler = player.getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            if (joinHandler != null && !joinHandler.hasJoined()) {
                ItemStack stack = new ItemStack(ModItems.spellbook);
                NBTTagCompound stackCompound;
                if (stack.hasTagCompound())
                    stackCompound = stack.getTagCompound();
                else
                    stackCompound = new NBTTagCompound();
                stackCompound.setUniqueId("boundTo", player.getUniqueID());
                stack.setTagCompound(stackCompound);
                player.addItemStackToInventory(stack);
                joinHandler.setJoined(true);
                MagicalMeringueCore.network.sendTo(new JoinMessage(joinHandler), (EntityPlayerMP) player);
            }

            /*
            IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandler != null) {
                MagicalMeringueCore.getLogger().debug("Testing mana:");
                MagicalMeringueCore.getLogger().debug("\tLevel: " + manaHandler.getLvl());
                for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                    MagicalMeringueCore.getLogger().debug("\tMana_" + i + ": " + manaHandler.getMana(i));
                }
                MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), (EntityPlayerMP) player);
            }
            */

        }
    }

    @SubscribeEvent
    public static void cloneEvent(PlayerEvent.Clone event) {
        /*
        NBTBase mana = event.getOriginal().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).serializeNBT();
        event.getEntityPlayer().getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null).deserializeNBT(mana);
         */

        NBTBase join = event.getOriginal().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null).serializeNBT();
        event.getEntityPlayer().getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null).deserializeNBT(join);
    }

    @SubscribeEvent
    public static void playerWakeUp(PlayerWakeUpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        if (manaHandler != null) {
            int level = manaHandler.getLvl();
            for(int i = 0; i < manaHandler.getLvl(); i++) {
                manaHandler.setMana(i, Utils.maxMana(i, level));
            }
        }
    }
}
