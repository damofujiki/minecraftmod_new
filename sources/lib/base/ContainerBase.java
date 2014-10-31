package com.hinasch.lib.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hinasch.lib.net.PacketGuiButtonBaseNew;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public abstract class ContainerBase extends Container{

	public EntityPlayer ep;
	public IInventory inv;
	public boolean spreadSlotItems = true;
	public int buttonID;
	public Object[] argsSent;
	
	public ContainerBase(EntityPlayer ep,IInventory inv){
		
		this.ep = ep;
		this.inv = inv;
		
		
		if(this.isShowPlayerInv()){
			for (int i = 0; i < 3; ++i)
			{
				for (int j = 0; j < 9; ++j)
				{
					this.addSlotToContainer(new Slot(ep.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
				}
			}

			for (int i = 0; i < 9; ++i)
			{
				this.addSlotToContainer(new Slot(ep.inventory, i, 8 + i * 18, 142));
			}
		}

	}
	
	public boolean isShowPlayerInv(){
		return true;
	}
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO 自動生成されたメソッド・スタブ
		return inv.isUseableByPlayer(var1);
	}

	public void onButtonPushed(int id,Object... args){
		PacketGuiButtonBaseNew packetButton = this.getPacketGuiButton(this.getGuiID(), id,args);
		this.getPacketPipeline().sendToServer(packetButton);
	}

	public abstract PacketGuiButtonBaseNew getPacketGuiButton(int guiID,int buttonID,Object... args);
	
	public abstract SimpleNetworkWrapper getPacketPipeline();
	
	public abstract int getGuiID();
	
	//事故帽子に
	@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
	
	public abstract void onPacketData();
	
	public void readPacketData(int buttonID,Object... args){
		this.buttonID = buttonID;

		if(args!=null && args.length>0){

			this.argsSent = new Object[args.length];
			for(int i=0;i<args.length;i++){
				this.argsSent[i] = args[i];
			}
		}

	}
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{

		super.onContainerClosed(par1EntityPlayer);
		if(!this.spreadSlotItems)return;
		if(!par1EntityPlayer.worldObj.isRemote){
			for(int i=0;i<this.inv.getSizeInventory();i++){
				ItemStack is = this.inv.getStackInSlotOnClosing(i);
				if(is!=null){
					par1EntityPlayer.entityDropItem(is,0.1F);
				}
			}
		}

	}
}
