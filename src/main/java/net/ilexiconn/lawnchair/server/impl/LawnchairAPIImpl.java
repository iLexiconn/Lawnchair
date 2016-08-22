package net.ilexiconn.lawnchair.server.impl;

import net.ilexiconn.lawnchair.api.LawnchairAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

public enum LawnchairAPIImpl implements LawnchairAPI {
    INSTANCE;

    private final Map<ItemStack, ItemStack[]> recipes = new HashMap<>();

    @Override
    public void registerFabricatorRecipe(Object output, Object... input) {
        ItemStack outputStack;
        if (output instanceof ItemStack) {
            outputStack = (ItemStack) output;
        } else if (output instanceof Item) {
            outputStack = new ItemStack((Item) output);
        } else if (output instanceof Block) {
            outputStack = new ItemStack((Block) output);
        } else {
            throw new RuntimeException("Object " + output + " isn't an ItemStack, Item or Block!");
        }

        List<ItemStack> inputStacks = new ArrayList<>();
        for (Object obj : input) {
            if (obj instanceof ItemStack) {
                inputStacks.add((ItemStack) obj);
            } else if (obj instanceof Item) {
                inputStacks.add(new ItemStack((Item) obj));
            } else if (obj instanceof Block) {
                inputStacks.add(new ItemStack((Block) obj));
            } else {
                throw new RuntimeException("Object " + obj + " isn't an ItemStack, Item or Block!");
            }
        }

        this.recipes.put(outputStack, inputStacks.toArray(new ItemStack[inputStacks.size()]));
    }

    @Override
    public ItemStack getRecipeOutput(ItemStack... input) {
        List<ItemStack> list = Arrays.asList(input);

        if (list.size() == 0) {
            return null;
        }

        for (Map.Entry<ItemStack, ItemStack[]> entry : this.recipes.entrySet()) {
            List<ItemStack> found = new ArrayList<>();

            for (ItemStack stack : entry.getValue()) {
                found.addAll(list.stream().filter(in -> stack.getItem() == in.getItem()).filter(in -> in.stackSize >= stack.stackSize && (stack.getMetadata() == OreDictionary.WILDCARD_VALUE || in.getMetadata() == stack.getMetadata())).collect(Collectors.toList()));
            }

            if (found.size() == list.size() && list.size() == entry.getValue().length) {
                return entry.getKey().copy();
            }
        }

        return null;
    }

    @Override
    public ItemStack[] getRecipe(ItemStack output) {
        for (Map.Entry<ItemStack, ItemStack[]> entry : this.recipes.entrySet()) {
            if (ItemStack.areItemStacksEqual(output, entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
