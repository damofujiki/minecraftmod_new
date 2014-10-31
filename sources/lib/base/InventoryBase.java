package com.hinasch.lib.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryBase implements IInventory{
	protected ItemStack[] theInventory;

	
	
	public InventoryBase(int size){

		this.theInventory = new ItemStack[size];
	}
	
	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.theInventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {

		return this.theInventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.theInventory[i]!=null){
			ItemStack is = this.theInventory[i];
			this.theInventory[i] = null;
			return is;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.theInventory[i]!=null){
			ItemStack is = this.theInventory[i];
			this.theInventory[i] = null;
			return is;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {

		this.theInventory[i] = itemstack;
		
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
	}





	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 64;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		
		return false;
	}



	@Override
	public void closeInventory() {


	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String getInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unknown";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void markDirty() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void openInventory() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
