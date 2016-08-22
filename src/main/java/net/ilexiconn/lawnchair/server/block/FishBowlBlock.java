package net.ilexiconn.lawnchair.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class FishBowlBlock extends Block {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.875F);

    public FishBowlBlock() {
        super(Material.GLASS);
        this.setUnlocalizedName("fish_bowl");
        this.setRegistryName("lawnchair:fish_bowl");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.2F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < 1 + Block.RANDOM.nextInt(3) + (fortune > 0 ? Block.RANDOM.nextInt(fortune) : 0); i++) {
            list.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
        }

        if (world instanceof World) {
            ((World) world).setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 11);
        }

        return list;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FishBowlBlock.AABB;
    }
}
