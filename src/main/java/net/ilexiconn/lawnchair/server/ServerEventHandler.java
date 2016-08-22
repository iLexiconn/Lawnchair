package net.ilexiconn.lawnchair.server;

import net.ilexiconn.lawnchair.client.gui.FabricatorGUI;
import net.ilexiconn.lawnchair.server.container.FabricatorContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ServerEventHandler implements IGuiHandler {
    INSTANCE;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new FabricatorContainer(new BlockPos(x, y, z), player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new FabricatorGUI(new FabricatorContainer(new BlockPos(x, y, z), player));
    }
}
