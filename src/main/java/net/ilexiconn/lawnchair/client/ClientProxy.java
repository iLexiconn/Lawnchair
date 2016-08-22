package net.ilexiconn.lawnchair.client;

import net.ilexiconn.lawnchair.Lawnchair;
import net.ilexiconn.lawnchair.server.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    private static final Minecraft CLIENT = Minecraft.getMinecraft();

    @Override
    public void onPreInit() {
        super.onPreInit();

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
    }

    @Override
    public void onInit() {
        super.onInit();

        ItemModelMesher modelMesher = ClientProxy.CLIENT.getRenderItem().getItemModelMesher();
        modelMesher.register(Lawnchair.FISH_BOWL_ITEM, 0, new ModelResourceLocation("lawnchair:fish_bowl", "inventory"));
    }
}
