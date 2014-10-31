package com.hinasch.lib;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;

public interface IMultiBlock {

	public int damageDropped(int par1);
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List);
}
