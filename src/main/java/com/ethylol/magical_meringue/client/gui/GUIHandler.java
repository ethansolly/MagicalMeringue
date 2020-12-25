package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MagicalMeringueCore.MODID)
public class GUIHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {

    }

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == ElementType.ALL) {
            EntityPlayer player = mc.player;
            if (!player.isSpectator()) {
                IManaHandler manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);

                ScaledResolution scaled = new ScaledResolution(mc);
                //int width = scaled.getScaledWidth();
                int height = scaled.getScaledHeight();

                mc.fontRenderer.drawStringWithShadow("Available Mana:", 10, 10, Integer.parseInt("FFAA00", 16));
                for (int i = 0; i < manaHandler.getLvl(); i++) {
                    mc.fontRenderer.drawStringWithShadow("Tier " + (i+1) + ": " + manaHandler.getMana(i), 20, 10+(height-20)*(i+1)/10, Integer.parseInt("FFAA00", 16));
                }

            }
        }
    }

}
