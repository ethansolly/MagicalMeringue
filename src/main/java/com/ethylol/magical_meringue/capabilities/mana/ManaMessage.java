package com.ethylol.magical_meringue.capabilities.mana;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ManaMessage implements IMessage {

    int lvl;
    float[] mana;

    public ManaMessage() {
        lvl = 1;
        mana = new float[IManaHandler.MAX_TIER];
    }

    public ManaMessage(IManaHandler manaHandler) {
        lvl = manaHandler.getLvl();
        mana = new float[IManaHandler.MAX_TIER];
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = manaHandler.getMana(i);
        }
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        lvl = buf.readInt();
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            mana[i] = buf.readFloat();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(lvl);
        for (int i = 0; i < IManaHandler.MAX_TIER; i++) {
            buf.writeFloat(mana[i]);
        }
    }
}
