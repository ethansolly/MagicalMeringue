package com.ethylol.magical_meringue;

import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.join.JoinMessage;
import com.ethylol.magical_meringue.capabilities.join.JoinMessageHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessage;
import com.ethylol.magical_meringue.capabilities.mana.ManaMessageHandler;
import com.ethylol.magical_meringue.network.RayTraceMessage;
import com.ethylol.magical_meringue.network.RayTraceMessageHandler;
import com.ethylol.magical_meringue.network.UpdateBookMessage;
import com.ethylol.magical_meringue.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = "magical_meringue", name = "Magical Meringue", version = "0.0.1")
public class MagicalMeringueCore {

    public static final String MODID = "magical_meringue";

    @Mod.Instance(value = "magical_meringue")
    public static MagicalMeringueCore instance;

    @SidedProxy(clientSide = "com.ethylol.magical_meringue.proxy.ClientProxy", serverSide = "com.ethylol.magical_meringue.proxy.IProxy")
    public static IProxy proxy;

    //Add config

    public static SimpleNetworkWrapper network;
    int discriminator = 0;

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        network = NetworkRegistry.INSTANCE.newSimpleChannel("magical_meringue_channel");
        network.registerMessage(ManaMessageHandler.class, ManaMessage.class, discriminator++, Side.CLIENT);
        network.registerMessage(ManaMessageHandler.class, ManaMessage.class, discriminator++, Side.SERVER);
        network.registerMessage(JoinMessageHandler.class, JoinMessage.class, discriminator++, Side.CLIENT);
        network.registerMessage(RayTraceMessageHandler.class, RayTraceMessage.class, discriminator++, Side.SERVER);
        network.registerMessage(UpdateBookMessage.UpdateBookMessageHandler.class, UpdateBookMessage.class, discriminator++,Side.SERVER);

        Capabilities.register();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static Logger getLogger() {
        return logger;
    }

}
