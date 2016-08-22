package net.ilexiconn.lawnchair.api;

import net.minecraft.item.ItemStack;

public interface LawnchairAPI {
    /**
     * Register a fabricator recipe.
     *
     * @param output the output of the recipe. This can either be a {@link net.minecraft.block.Block}, {@link net.minecraft.item.Item} or {@link net.minecraft.item.ItemStack}.
     * @param input  the input of the recipe.  This can either be a {@link net.minecraft.block.Block}, {@link net.minecraft.item.Item} or {@link net.minecraft.item.ItemStack}. The order doesn't matter.
     */
    void registerFabricatorRecipe(Object output, Object... input);

    /**
     * Get the output for the given input, null if there is none.
     *
     * @param input the input stacks.
     * @return the output for the given input, or null.
     */
    ItemStack getRecipeOutput(ItemStack... input);

    /**
     * Get the recipe for the given output, null if there is none.
     *
     * @param output the output stack
     * @return the input for the given output, or null.
     */
    ItemStack[] getRecipe(ItemStack output);
}
