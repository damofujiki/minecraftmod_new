package com.hinasch.unlsaga.inventory;



import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.villager.bartering.MerchandiseInfo;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryMerchantUnsaga implements IInventory{

	protected String KEY = ExtendedMerchantData.VILLAGER;
	public final int RESULT = 8;
	protected ItemStack[] bartering = new ItemStack[10];
	protected ItemStack[] merchandise = new ItemStack[10];
	protected ItemStack result;
	protected EntityPlayer theCustomer;
	protected IMerchant theMerchant;
	protected EntityVillager villager;
	protected MerchantHelper helper;

	public InventoryMerchantUnsaga(EntityPlayer ep,IMerchant merchant){
		this.theCustomer = ep;
		this.theMerchant = merchant;

		//サーバー側だけEntityVillagerを保持
		if(this.theMerchant instanceof EntityVillager){
			this.villager = (EntityVillager)merchant;
		}else{
			this.villager = null;
		}

		this.helper = new MerchantHelper(this);
		//サーバー側だけ情報を保存・読み込み
		if(!ep.worldObj.isRemote){
			this.helper.initMerhcnadiseStock(merchant);
		}
		


	}
	
	public MerchantHelper getHelper(){
		return this.helper;
	}
	public int getSizeBarteringInv(){
		return 7;
	}
	
	public int getCurrentPriceToSell(){
		int price = 0;
		for(int i=0;i<this.getSizeBarteringInv();i++){
			//途中
			//price = price + this.getBartering(i);
			if(this.getBartering(i)!=null){
				if(MerchandiseInfo.isPossibleToSell(this.getBartering(i))){
					price += MerchandiseInfo.getPrice(this.getBartering(i));

				}
			}
		}
		return price;
	}
	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.bartering.length + this.merchandise.length ;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO 自動生成されたメソッド・スタブ
		if(i==RESULT){
			return this.result;
		}
		if(i>9){
			return this.merchandise[i-10];
		}
		if(i<=this.getSizeBarteringInv()){
			return this.bartering[i];
		}

		return null;
	}

	public ItemStack getResult(){
		return this.getStackInSlot(RESULT);
	}
	
	public void setResult(ItemStack is){
		this.setInventorySlotContents(RESULT, is);
	}
	public ItemStack getMerchandise(int par1){
		return this.getStackInSlot(10+par1);					
	}
	public ItemStack getBartering(int par1){
		return this.getStackInSlot(par1);
	}

	public void setMerchandise(int par1,ItemStack is){

		this.setInventorySlotContents(10+par1,is);	

	}
	public void setBartering(int par1,ItemStack is){
		this.setInventorySlotContents(par1,is);
	}
	@Override
	public ItemStack decrStackSize(int i, int request) {
		// TODO 自動生成されたメソッド・スタブ
		Unsaga.debug("decr:"+i);
		if(i<=this.getSizeBarteringInv()){
			if(this.bartering[i]!=null){
				int stack = request;
				ItemStack is;
				if(this.bartering[i].stackSize<=request){
					is = this.bartering[i];
					this.bartering[i] = null;

					return is;
				}else{
					is = this.bartering[i].splitStack(request);
					if(this.bartering[i].stackSize == 0){
						this.bartering[i] = null;
					}

					return is;
				}

			}
		}
		if(i==RESULT){
			if(this.result!=null){
				int stack = request;
				ItemStack is;
				if(this.result.stackSize<=request){
					is = this.result;
					this.result = null;

					return is;
				}else{
					is = this.result.splitStack(request);
					if(this.result.stackSize == 0){
						this.result = null;
					}

					return is;
				}

			}
		}
		if(i>9){
			int j = i-10;
			if(this.merchandise[j]!=null){
				
				
					ItemStack bought;
					bought = this.merchandise[j];
					this.merchandise[j] = null;
					
					return bought;
				

				
			}
		}
		return null;
	}

	public boolean canBuy(ItemStack is){
		if(this.getResult()==null){
			int priceBuy = MerchandiseInfo.getPrice(is);
			int priceSell = this.getCurrentPriceToSell();
			if(priceSell>=priceBuy){
				return true;
			}
		}

		return false;

	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(i<=this.getSizeBarteringInv()){
			if(this.bartering[i]!=null){
				ItemStack is = this.bartering[i];
				this.bartering[i] = null;
				return is;
			}
			return null;
		}
		if(i==RESULT){
			if(this.result!=null){
				ItemStack is = this.result;
				this.result = null;
				return is;
			}
		}
		return null;

	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		Unsaga.debug(i+"に"+itemstack+"をセットします");
		if(i<=this.getSizeBarteringInv()){
			this.bartering[i] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		if(i==RESULT){
			this.result = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}
		}
		if(i>9){
			

			this.merchandise[i-10] = itemstack;

			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			{
				itemstack.stackSize = this.getInventoryStackLimit();
			}

		}





	}

	@Override
	public String getInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unsaga.merchant.inventory";
	}


	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 64;
	}



	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.theCustomer == entityplayer;
	}



	@Override
	public void closeInventory() {
		Unsaga.debug("データ書き込みました");
		if(!this.theCustomer.worldObj.isRemote){
			this.helper.registerMerchandiseStock();
		}

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}



	@Override
	public boolean hasCustomInventoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void markDirty() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void openInventory() {
		// TODO 自動生成されたメソッド・スタブ
		
	}



}
