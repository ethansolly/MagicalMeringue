package com.ethylol.magical_meringue.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.SerializationUtils;

public class RayTraceMessage implements IMessage {

    private Vec3d pos;
    private RayTraceResult.Type hitType;
    private MessageType messageType;

    public RayTraceMessage() {}

    public RayTraceMessage(RayTraceResult result, MessageType messageType) {
        hitType = result.typeOfHit;
        pos = result.hitVec;
        this.messageType = messageType;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        hitType = RayTraceResult.Type.values()[buf.readInt()];
        messageType = MessageType.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
        buf.writeInt(hitType.ordinal());
        buf.writeInt(messageType.ordinal());
    }


    public Vec3d getVec3d() {
        return pos;
    }

    public RayTraceResult.Type getHitType() {
        return hitType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {
        BREAK_BLOCK,
        LEVITATE
    }

}
