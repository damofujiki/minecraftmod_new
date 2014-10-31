package com.hinasch.unlsaga.villager.smith;

import com.hinasch.unlsaga.item.UnsagaEnum;
import com.hinasch.unlsaga.material.UnsagaMaterial;

import net.minecraft.item.Item;

public class SmithValidWeapon {

	
	public UnsagaMaterial associated;
	public UnsagaEnum category;
	public Item item;
	
	public SmithValidWeapon(Item item,UnsagaEnum cate,UnsagaMaterial material){
		this.item = item;
		this.category = cate;
		this.associated = material;
	}
	

}
