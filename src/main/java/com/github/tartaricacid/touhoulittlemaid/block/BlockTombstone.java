package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/10/2 21:46
 **/
public class BlockTombstone extends Block implements ITileEntityProvider {
    public BlockTombstone() {
        super(Material.ROCK);
        setHardness(1.0f);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "tombstone");
        setRegistryName("tombstone");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityTombstone();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!player.isCreative() && te instanceof TileEntityTombstone) {
            TileEntityTombstone tombstone = (TileEntityTombstone) te;
            for (int i = 0; i < tombstone.handler.getSlots(); i++) {
                spawnAsEntity(worldIn, player.getPosition(), tombstone.handler.getStackInSlot(i));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityTombstone) {
            TileEntityTombstone tombstone = (TileEntityTombstone) te;
            for (int i = 0; i < tombstone.handler.getSlots(); i++) {
                spawnAsEntity(worldIn, playerIn.getPosition(), tombstone.handler.getStackInSlot(i));
            }
        }
        worldIn.setBlockToAir(pos);
        return true;
    }

    /**
     * 略微修改原版该方法，取消默认的半秒拾取延迟
     */
    public static void spawnAsEntity(World worldIn, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean("doTileDrops") && !worldIn.restoringBlockSnapshots) {
            if (captureDrops.get()) {
                capturedDrops.get().add(stack);
                return;
            }
            EntityItem entityitem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            worldIn.spawnEntity(entityitem);
        }
    }
}
