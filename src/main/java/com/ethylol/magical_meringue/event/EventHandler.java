package com.ethylol.magical_meringue.event;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.*;
import com.ethylol.magical_meringue.capabilities.join.IJoinHandler;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinProvider;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.capabilities.mana.ManaProvider;
import com.ethylol.magical_meringue.item.ModItems;
import com.ethylol.magical_meringue.item.Spellbook;
import com.ethylol.magical_meringue.proxy.ClientProxy;
import com.ethylol.magical_meringue.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Map;


@Mod.EventBusSubscriber(modid = MagicalMeringueCore.MODID)
public class EventHandler {

    //Registration

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Register blocks

        //Register tileentities
    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Register items
        registerItem(new Spellbook(), event);

        //Register itemblocks

    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {

        //Register item models
        registerItemModel(ModItems.spellbook);

        //Register itemblock models
    }

    private static void registerItemBlock(Block block, RegistryEvent.Register<Item> event) {
        Item itemBlock = new ItemBlock(block).setRegistryName(block.getRegistryName());
        event.getRegistry().registerAll(itemBlock);
    }

    private static void registerItem(Item item, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(item);
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
                EntityItem entityItem = new EntityItem(event.getWorld(), player.posX, player.posY, player.posZ, stack);
                entityItem.setNoPickupDelay();
                event.getWorld().spawnEntity(entityItem);

                joinHandler.setJoined(true);
                MagicalMeringueCore.network.sendTo(new JoinMessage(joinHandler), (EntityPlayerMP) player);
            }

            IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            if (manaHandler != null) {
                MagicalMeringueCore.network.sendTo(new ManaMessage(manaHandler), (EntityPlayerMP) player);
            }

        }
    }

    /*
    @SubscribeEvent
    public static void onItemPickup(net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent event) {
        ItemStack stack = event.getStack();
        EntityPlayer player = event.player;
        if (stack.getItem() == ModItems.spellbook) {
            NBTTagCompound compound;
            if (stack.hasTagCompound() && !stack.getTagCompound().hasKey("boundTo")) {
                //MagicalMeringueCore.getLogger().debug("----------Has compound----------");
                compound = stack.getTagCompound();
                compound.setUniqueId("boundTo", player.getUniqueID());
                stack.setTagCompound(compound);
            }
            else if (!stack.hasTagCompound()) {
                //MagicalMeringueCore.getLogger().debug("----------Has NO compound----------");
                compound = new NBTTagCompound();
                compound.setUniqueId("boundTo", player.getUniqueID());
                stack.setTagCompound(compound);
            }
            else {
                //MagicalMeringueCore.getLogger().debug("----------Has compound and is bound???----------");
            }
        }
        else {
            //MagicalMeringueCore.getLogger().debug("----------ISN'T A SPELLBOOK???----------");
        }
    }
     */

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
