package com.hinasch.unlsagamagic.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.base.InventoryBase;
import com.hinasch.unlsagamagic.item.ItemBlender;

public class InventoryBlender extends InventoryBase{

	public InventoryBlender() {
		super(9);
	
	}


	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return ItemUtil.hasItemInstance(entityplayer, ItemBlender.class);
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String getInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "inventory.blender";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


}
