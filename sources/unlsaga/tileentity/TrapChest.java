package com.hinasch.unlsaga.tileentity;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;


public class TrapChest {

	public final String name;
	public final int number;
	
	public TrapChest(Integer number,String name,Map map){
		this.name = name;
		this.number = number;
		if(number>0 && map!=null){
			map.put(number, this);
		}

	}
	public void activate(TileEntityChestUnsagaNew chest,EntityPlayer ep){
		
	}
}
