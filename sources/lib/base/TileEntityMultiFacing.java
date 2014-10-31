package com.hinasch.lib.base;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMultiFacing extends TileEntity{
	
    protected int orientation;
    
    @Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
	}
   
    
	public int getOrientation(){
    	return this.orientation;
    }
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.orientation = pkt.func_148857_g().getInteger("orientation");
		
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
        if(nbt.hasKey("orientation")){
        	this.setOrientation(nbt.getInteger("orientation"));
        }else{
        	this.setOrientation(3);
        }
    }
    
	public void setOrientation(int par1){
    	this.orientation = par1;
    	
    }
	
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
        nbt.setInteger("orientation", this.orientation);
    }
	
	
    public static void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	int ori = BlockPistonBase.determineOrientation(world, x, y, z, p_149689_5_);
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te instanceof TileEntityMultiFacing){
    		TileEntityMultiFacing multiFace = (TileEntityMultiFacing)te;
    		multiFace.setOrientation(ori);
    	}
    }
}
