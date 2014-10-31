package com.hinasch.unlsaga.item.misc;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockOreUnsaga extends ItemBlock{
	
	public ItemBlockOreUnsaga(Block parent) {
		super(parent);
		this.setHasSubtypes(true);

		
		// TODO Auto-generated constructor stub
	}

	
	@Override
    public int getMetadata(int par1)
    {
        return par1;
    }
	

}
