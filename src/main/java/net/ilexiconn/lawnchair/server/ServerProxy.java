package net.ilexiconn.lawnchair.server;

import net.ilexiconn.lawnchair.Lawnchair;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy {
    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(Lawnchair.INSTANCE, ServerEventHandler.INSTANCE);
    }

    public void onInit() {

    }
}
