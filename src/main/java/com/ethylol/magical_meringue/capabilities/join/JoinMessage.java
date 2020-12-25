package com.ethylol.magical_meringue.capabilities.join;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class JoinMessage implements IMessage {

    boolean joined;

    public JoinMessage(IJoinHandler handler) {
        this.joined = handler.hasJoined();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        joined = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(joined);
    }
}
