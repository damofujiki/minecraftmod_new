package com.hinasch.unlsaga.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.hinasch.lib.base.InventoryBase;

public class InventorySkillPanel extends InventoryBase{

	
	public InventorySkillPanel() {
		super(9);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ItemStack[] getPlayerPanels(){
		ItemStack[] iss = new ItemStack[7];
		for(int i=0;i<7;i++){
			iss[i] = this.theInventory[i]!=null ? this.theInventory[i].copy() : null;
		}
		return iss;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		
		return entityplayer.openContainer != entityplayer.inventoryContainer;
	}
	
	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}
}
