package com.hinasch.unlsaga.inventory.slot;

import com.hinasch.unlsaga.inventory.InventoryMerchantUnsaga;
import com.hinasch.unlsaga.villager.bartering.MerchandiseInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotMerchandise extends Slot{

	public boolean slotLocked = true;
	public boolean isInit = true;
	public MerchandiseInfo info;
	public InventoryMerchantUnsaga invMerhcant;
	public int SlotID;
	
	public SlotMerchandise(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		
		this.invMerhcant = (InventoryMerchantUnsaga)par1iInventory;
		this.SlotID = par2;
		if(this.invMerhcant.getMerchandise(par2-10)!=null){
			this.info = new MerchandiseInfo(this.invMerhcant.getMerchandise(par2-10));
		}
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
//	@Override
//    public void putStack(ItemStack par1ItemStack)
//    {
//
//		
//    }
//    
//	@Override
//    public ItemStack decrStackSize(int par1)
//    {
//		if(canBuy(this.invMerhcant.getMerchandise(this.SlotID))){
//			super.decrStackSize(1);
//		}
//    	return null;
//    }
//	
//	public boolean canBuy(ItemStack is){
//		if(is==null)return false;
//		MerchandiseInfo info = new MerchandiseInfo(is);
//		int priceBuy = info.getBuyPriceTag();
//		int priceSell = this.invMerhcant.getCurrentPriceToSell();
//		if(priceBuy<=priceSell){
//			return true;
//		}
//		return false;
//	}
    
	@Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
		return false;
//    	if(this.info==null)return false;
//    	int priceBuy = info.getBuyPriceTag();
//    	int priceSell = this.invMerhcant.getCurrentPriceToSell();
//    	if(priceSell>=priceBuy){
//    		this.slotLocked = false;
//    	}
//    	Unsaga.debug(this.slotLocked);
//        return slotLocked ? false : true;
    }

}
