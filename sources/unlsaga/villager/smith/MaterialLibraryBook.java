package com.hinasch.unlsaga.villager.smith;

import com.hinasch.lib.library.LibraryBook;
import com.hinasch.unlsaga.material.UnsagaMaterial;

public class MaterialLibraryBook extends LibraryBook{

	public UnsagaMaterial material;

	public int damage;
	
	public MaterialLibraryBook(Object par1,UnsagaMaterial material,int damage) {
		super(par1);
		this.material = material;
		this.damage = damage;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	
}
