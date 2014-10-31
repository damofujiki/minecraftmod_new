package com.hinasch.unlsaga.tileentity;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFallStone extends TileEntity{

	
	protected int remaining = 100;
	protected Block blockObj;
	protected int metadata;
	
	public void init(Block blockobj,int meta){
		this.blockObj = blockobj;
		this.metadata = meta;
	}
	
	@Override
	public void updateEntity(){
		if(this.worldObj.isRemote){
			this.worldObj.removeTileEntity(xCoord, yCoord, zCoord);
		}
		this.remaining -= 1;
		Unsaga.debug(this.remaining+" blockobj:"+this.blockObj+":"+(new XYZPos(xCoord,yCoord,zCoord)));
		if(this.remaining<0){
			this.remaining = -1;
			this.setExpired();
		}
		
	}
	
	public void setExpired(){

		if(blockObj==null){
			blockObj = Blocks.cobblestone;
		}
		
		if(blockObj!=null && !this.worldObj.isRemote){
			this.worldObj.setBlock(xCoord, yCoord, zCoord, blockObj, metadata, 3);
			
		}
		this.worldObj.removeTileEntity(xCoord, yCoord, zCoord);
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	nbt.setInteger("blockNum", Block.getIdFromBlock(blockObj));
    	nbt.setInteger("metadata", this.metadata);
    	nbt.setInteger("remaining", this.remaining);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound comp){
    	this.blockObj = Block.getBlockById(comp.getInteger("blockNum"));
    	this.metadata = comp.getInteger("metadata");
    	this.remaining = comp.getInteger("remaining");
    }
}
