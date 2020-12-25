package com.ethylol.magical_meringue.capabilities.join;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class JoinMessageHandler implements IMessageHandler<JoinMessage, IMessage> {
    @Override
    public IMessage onMessage(JoinMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            IJoinHandler manaHandler = player.getCapability(Capabilities.JOIN_HANDLER_CAPABILITY, null);
            manaHandler.setJoined(message.joined);
        });
        return null;
    }
}
