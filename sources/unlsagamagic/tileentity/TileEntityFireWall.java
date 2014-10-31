package com.hinasch.unlsagamagic.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;

public class TileEntityFireWall extends TileEntity{

	protected int remaining = 100;
	

	
	public TileEntityFireWall(){
		
	}
	
	public void buildFireWall(int par1,XYZPos start,XYZPos end){
		
	}
	public void init(int par1){
		Unsaga.debug("FireWall remains "+par1,this.getClass());
		this.remaining = par1;
	}
	
	
	public void setRemaining(int par1){
		this.remaining = par1;
	}
	@Override
	public Packet getDescriptionPacket() {

		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.remaining = pkt.func_148857_g().getInteger("remaining");

    }
	
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {

    	super.readFromNBT(compound);
    	this.remaining = compound.getInteger("remaining");


    }
	
    @Override
    public void writeToNBT(NBTTagCompound compound)
    {

    	super.writeToNBT(compound);
    	compound.setInteger("remaining", this.remaining);

    }
    
    @Override
    public void updateEntity() {
    	this.remaining -=1;
    	
    	if(this.remaining<0){
    		this.setDeath();
    	}
    }
    
    protected void setDeath(){
    	this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
    }
}
