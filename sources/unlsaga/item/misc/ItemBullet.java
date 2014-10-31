package com.hinasch.unlsaga.item.misc;


import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.hinasch.unlsaga.Unsaga;

public class ItemBullet extends Item {

	private String iconname;
	public ItemBullet(String par2) {
		super();
		this.iconname = par2;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registerIcons(IIconRegister par1){
		this.itemIcon = par1.registerIcon(Unsaga.DOMAIN+":"+this.iconname);
	}


}

