package com.hinasch.lib.base;

import com.hinasch.lib.HelperInventory;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCustomChestBase extends BlockContainer{

	public boolean isItemSpread = true;
	
	protected BlockCustomChestBase(Material p_i45386_1_) {
		super(p_i45386_1_);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	
	@Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer ep, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	WorldHelper worldHelper = new WorldHelper(p_149727_1_);
    	XYZPos pos = new XYZPos(p_149727_2_,p_149727_3_,p_149727_4_);
    	int meta = worldHelper.getBlockMetadata(pos);

        if (p_149727_1_.isRemote)
        {
            return true;
        }
        else
        {
        	TileEntity te = worldHelper.getTileEntity(pos);
        	openGUI(p_149727_1_, te,ep,pos);

            return true;
        }
    }
	
	public void openGUI(World world,TileEntity te,EntityPlayer ep,XYZPos pos){
    	if(te instanceof TileEntityCustomChestBase){
            IInventory iinventory = (IInventory)te;

                    if (iinventory != null)
                    {
                        ep.displayGUIChest(iinventory);
                    }
    	}
	}
	
    @Override
    public void breakBlock(World p_149749_1_, int x, int y, int z, Block bl, int p_149749_6_)
    {
        TileEntityCustomChestBase tileentitychest = (TileEntityCustomChestBase)p_149749_1_.getTileEntity(x, y, z);

        if (tileentitychest != null && this.isItemSpread)
        {
        	
        	HelperInventory uinv = new HelperInventory(tileentitychest);
        	uinv.spreadChestContents(p_149749_1_, tileentitychest, new XYZPos(x,y,z));

        }

        super.breakBlock(p_149749_1_, x, y, z, bl, p_149749_6_);
    }
}
