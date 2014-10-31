package com.hinasch.unlsaga.inventory;

import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.base.InventoryBase;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;

public class InventoryEquipment extends InventoryBase{


	protected final EntityPlayer thePlayer;
	
	
	public InventoryEquipment(EntityPlayer ep){
		super(3);
		this.thePlayer = ep;
	}
	

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		
		return entityplayer.openContainer != entityplayer.inventoryContainer;
	}

	@Override
	public void closeInventory() {

		ExtendedPlayerData data = ExtendedPlayerData.getData(thePlayer);
		data.setAccessories(this.getStackInSlot(0), this.getStackInSlot(1));
		data.setTablet(this.getStackInSlot(2));
		
//		if(this.thePlayer.getExtendedProperties("unsaga.equipment")==null){
//			ExtendedPlayerData newdata = new ExtendedPlayerData();
//			newdata.setAccessories(this.getStackInSlot(0), this.getStackInSlot(1));
//			newdata.setTablet(this.getStackInSlot(2));
//			this.thePlayer.registerExtendedProperties("unsaga.equipment", newdata);
//		}else{
//			ExtendedPlayerData data = (ExtendedPlayerData) this.thePlayer.getExtendedProperties("unsaga.equipment");
//			data.setAccessories(this.getStackInSlot(0),this.getStackInSlot(1));
//			data.setTablet(this.getStackInSlot(2));
//		}

	}


	@Override
	public String getInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unsaga.inventory.equipment";
	}



}
