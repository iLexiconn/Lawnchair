package net.ilexiconn.lawnchair.server.network;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.lawnchair.server.block.entity.FabricatorEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ClearFabricatorMessage extends AbstractMessage<ClearFabricatorMessage> {
    private BlockPos pos;

    public ClearFabricatorMessage() {

    }

    public ClearFabricatorMessage(FabricatorEntity entity) {
        this.pos = entity.getPos();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, ClearFabricatorMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, ClearFabricatorMessage message, EntityPlayer player, MessageContext messageContext) {
        TileEntity entity = player.worldObj.getTileEntity(message.pos);
        if (entity instanceof FabricatorEntity) {
            List<ItemStack> inventory = ((FabricatorEntity) entity).inventory;
            inventory.stream().filter(stack -> !player.inventory.addItemStackToInventory(stack)).forEach(stack -> InventoryHelper.spawnItemStack(player.worldObj, message.pos.getX(), message.pos.getY(), message.pos.getZ(), stack));
            inventory.clear();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
    }
}
