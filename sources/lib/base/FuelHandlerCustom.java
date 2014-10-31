package com.hinasch.lib.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandlerCustom implements IFuelHandler {

	protected List<FlammableData> listFlammable;
	
	public FuelHandlerCustom(){
		this.listFlammable = new ArrayList();
	}
	
	public void addFuel(ItemStack is,int sec,boolean checkMeta){
		this.listFlammable.add(new FlammableData(is,sec,checkMeta));
	}
	
	public class FlammableData{
		public int burningSec;
		public boolean noCheckMeta;
		public ItemStack itemdata;
		public FlammableData(ItemStack is,int sec,boolean noCheckMeta){
			this.burningSec = sec;
			this.noCheckMeta = noCheckMeta;
			this.itemdata = is;
		}
		
	}
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		for(FlammableData data:this.listFlammable){

				if(data.itemdata.getItem()==fuel.getItem()){
					if(data.noCheckMeta){
						return data.burningSec;
					}else{
						if(data.itemdata.getItemDamage()==fuel.getItemDamage()){
							return data.burningSec;
						}
					}
				}

		}
	return 0;
	}
}
