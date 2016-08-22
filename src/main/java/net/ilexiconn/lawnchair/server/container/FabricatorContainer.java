package net.ilexiconn.lawnchair.server.container;

import net.ilexiconn.lawnchair.server.block.entity.FabricatorEntity;
import net.ilexiconn.lawnchair.server.impl.LawnchairAPIImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class FabricatorContainer extends Container {
    public final BlockPos pos;
    public final EntityPlayer player;
    public final FabricatorEntity entity;

    public FabricatorContainer(BlockPos pos, EntityPlayer player) {
        this.pos = pos;
        this.player = player;
        this.entity = (FabricatorEntity) this.player.worldObj.getTileEntity(this.pos);

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 144));
            for (int y = 0; y < 3; y++) {
                this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 86 + y * 18));
            }
        }

        this.addSlotToContainer(new Slot(this.entity, 0, 26, 18) {
            @Override
            public void putStack(ItemStack stack) {
                if (stack == null) {
                    return;
                }

                for (ItemStack s : FabricatorContainer.this.entity.inventory) {
                    if (ItemStack.areItemsEqual(s, stack)) {
                        s.stackSize += stack.stackSize;
                        return;
                    }
                }

                FabricatorContainer.this.entity.inventory.add(stack);
            }
        });
        this.addSlotToContainer(new Slot(this.entity, 1, 26, 54) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
                if (player.capabilities.isCreativeMode) {
                    return;
                }

                ItemStack[] recipe = LawnchairAPIImpl.INSTANCE.getRecipe(stack);

                if (recipe == null) {
                    stack.stackSize = 0;
                    return;
                }

                for (ItemStack recipeStack : recipe) {
                    new ArrayList<>(entity.inventory).stream().filter(input -> ItemStack.areItemsEqual(recipeStack, input)).forEach(input -> {
                        input.stackSize -= recipeStack.stackSize;
                        if (input.stackSize <= 0) {
                            entity.inventory.remove(input);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.entity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return null;
    }
}
