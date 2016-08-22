package net.ilexiconn.lawnchair.server.block.entity;

import net.ilexiconn.lawnchair.server.impl.LawnchairAPIImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class FabricatorEntity extends TileEntity implements IInventory, ITickable {
    private final ItemStack[] stacks = new ItemStack[2];
    public final List<ItemStack> inventory = new ArrayList<>();

    public String customName;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        this.inventory.stream().filter(stack -> stack != null).forEach(stack -> list.appendTag(stack.serializeNBT()));
        compound.setTag("Inventory", list);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory.clear();
        NBTTagList list = compound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
            this.inventory.add(stack);
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < this.getSizeInventory();
    }

    @Override
    public int getSizeInventory() {
        return this.stacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.isValidIndex(index) ? this.stacks[index] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(this.stacks, index, count);

        if (stack != null) {
            this.markDirty();
        }

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.stacks[index] != null) {
            ItemStack stack = this.stacks[index];
            this.stacks[index] = null;
            return stack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (this.isValidIndex(index)) {
            this.stacks[index] = stack;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F) <= 64.0F;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.stacks.length; i++) {
            this.stacks[i] = null;
        }
        this.inventory.clear();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "tile.fabricator.name";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public void update() {
        ItemStack stack = LawnchairAPIImpl.INSTANCE.getRecipeOutput(this.inventory.toArray(new ItemStack[this.inventory.size()]));
        this.stacks[1] = stack;
    }
}
