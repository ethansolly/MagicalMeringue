package com.ethylol.magical_meringue.network;

import com.ethylol.magical_meringue.item.ModItems;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class UpdateBookMessage implements IMessage {

    private ItemStack bookStack;


    public UpdateBookMessage() {
        bookStack = new ItemStack(ModItems.spellbook);
    }

    public UpdateBookMessage(ItemStack bookStack) {
        this.bookStack = bookStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        //Copied from PacketBuffer#readCompoundTag
        bookStack = new ItemStack(ModItems.spellbook);
        int i = buf.readerIndex();
        if (buf.readByte() != 0) {
            buf.readerIndex(i); //reset the index
            try {
                bookStack.setTagCompound(CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L)));
            } catch (IOException e) {
                throw new EncoderException(e);
            }
        }
        else {
            bookStack.setTagCompound(null);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        //Copied from PacketBuffer#writeCompoundTag
        if (bookStack.hasTagCompound()) {
            try {
                CompressedStreamTools.write(bookStack.getTagCompound(), new ByteBufOutputStream(buf));
            } catch (IOException e) {
                throw new EncoderException(e);
            }
        }
        else {
            buf.writeByte(0);
        }

    }

    public static class UpdateBookMessageHandler implements IMessageHandler<UpdateBookMessage, IMessage> {

        @Override
        public IMessage onMessage(UpdateBookMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack clientBook = message.bookStack;
            ItemStack serverBook = player.getHeldItemMainhand();
            if (serverBook.getItem() == ModItems.spellbook && clientBook.getItem() == ModItems.spellbook)
                player.getServerWorld().addScheduledTask(() -> serverBook.setTagCompound(clientBook.getTagCompound()));

            return null;
        }
    }
}
