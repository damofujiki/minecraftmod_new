package com.hinasch.unlsaga.inventory.slot;

import com.hinasch.unlsagamagic.item.ItemTablet;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotTablet extends Slot{

	public SlotTablet(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
        return (item instanceof ItemTablet)? true : false;
    }
}
