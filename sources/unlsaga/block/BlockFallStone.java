package com.hinasch.unlsaga.block;

import java.util.Random;

import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.tileentity.TileEntityFallStone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFallStone extends BlockFalling implements ITileEntityProvider
{
    /** Do blocks fall instantly to where they stop or do they fall over time */
    public static boolean fallInstantly = false;

    
    public BlockFallStone()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public BlockFallStone(Material par3Material)
    {
        super(par3Material);
    }
    
    public int tickRate(World par1World)
    {
        return 2;
    }
    
//    @Override
//    public TileEntity createTileEntity(World world, int metadata)
//    {
//    	int blockid = Block.cobblestone.blockID;
//    	if(metadata==3){
//    		blockid = Block.dirt.blockID;
//    	}
//    	if(metadata==5){
//    		blockid = Block.netherrack.blockID;
//    	}
//        return new TileEntityShapeMemory(blockid,0,200);
//    }
//    

	
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("stonebrick");

    }
    
    @Override
    public IIcon getIcon(int par1, int par2)
    {
    	return this.getBlockFromMeta(par2).getBlockTextureFromSide(0);
    }

    
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    	WorldHelper worldHelper = new WorldHelper(par1World);
    	XYZPos pos = new XYZPos(par2,par3,par4);
    	TileEntity te = worldHelper.getTileEntity(pos);
    	if(te instanceof TileEntityFallStone){
    		((TileEntityFallStone) te).init(this.getBlockFromMeta(worldHelper.getBlockMetadata(pos)), 0);
    	}
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate());
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate());
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            this.tryToFall(par1World, par2, par3, par4);
        }
    }

    /**
     * If there is space to fall below will start this block falling
     */
    
    private void tryToFall(World par1World, int par2, int par3, int par4)
    {
    	int metadata = par1World.getBlockMetadata(par2, par3, par4);
        if (canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            byte var8 = 32;

            if (!fallInstantly && par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8))
            {
                if (!par1World.isRemote)
                {
                    EntityFallingBlock var9 = new EntityFallingBlock(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), this, par1World.getBlockMetadata(par2, par3, par4));
                    this.onStartFalling(var9);
                    par1World.spawnEntityInWorld(var9);
                }
            }
            else
            {
                par1World.setBlockToAir(par2, par3, par4);

                while (canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
                {
                    --par3;
                }

                if (par3 > 0)
                {
                    par1World.setBlock(par2, par3, par4, this, metadata, 3);
                }
            }
        }
    }

    /**
     * Called when the falling block entity for this block is created
     */
    protected void onStartFalling(EntityFallingBlock par1EntityFallingSand) {}

    /**
     * How many world ticks before ticking
     */
    
    public int tickRate()
    {
        return 5;
    }

    /**
     * Checks to see if the sand can fall into the block below it
     */
    public static boolean canFallBelow(World par0World, int par1, int par2, int par3)
    {
        Block var4 = par0World.getBlock(par1, par2, par3);

        if (var4.isAir(par0World, par1, par2, par3))
        {
            return true;
        }
        else if (var4 == Blocks.fire)
        {
            return true;
        }
        else
        {
            Material var5 = var4.getMaterial();
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }

    public Block getBlockFromMeta(int meta){
    	switch(meta){
    	case 5:
    		return Blocks.netherrack;
    	case 3:
    		return Blocks.dirt;
    	default:
    		return Blocks.cobblestone;
    	}
    }
    /**
     * Called when the falling block entity for this block hits the ground and turns back into a block
     */
//    public void onFinishFalling(World par1World, int par2, int par3, int par4, int par5) {
//    	
//    	Block blockObj = this.getBlockFromMeta(par1World.getBlockMetadata(par2, par3, par4));
//
//    	
//    	par1World.setBlock(par2, par3, par4, blockObj, 0, 2);
//    
//    }
    
    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return null;
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		if(!var1.isRemote){
			return new TileEntityFallStone();
		}
		return null;
	}

	@Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
        p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    }
	
	@Override
    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }
}
