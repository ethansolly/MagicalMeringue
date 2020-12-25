package com.ethylol.magical_meringue.capabilities.mana;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ManaMessageHandler implements IMessageHandler<ManaMessage, IMessage> {
    @Override
    public IMessage onMessage(ManaMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
            manaHandler.setLvl(message.lvl);
            for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
                manaHandler.setMana(i, message.mana[i]);
            }
        });
        return null;
    }
}
