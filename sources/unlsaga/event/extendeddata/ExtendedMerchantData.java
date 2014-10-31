package com.hinasch.unlsaga.event.extendeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtendedMerchantData implements IExtendedEntityProperties{

	protected ItemStack[] merchantInventory = new ItemStack[10];
	protected boolean initMerchandice = false;
	protected boolean foundSecret = false;
	protected int distributionLevel = 0;
	protected int carrierID = -1;
	public long recentPurchaseDate; 
	public static final String VILLAGER = "unsaga.villager";
	
	@SubscribeEvent
	public void attachDataEvent(EntityConstructing e){
	
		if(e.entity instanceof EntityVillager){
			EntityVillager ep = (EntityVillager)e.entity;
			ep.registerExtendedProperties(VILLAGER, new ExtendedMerchantData());
		}
	}
	
	public void setCarrierID(int par1){
		this.carrierID = par1;
	}
	
	public int getCarrierID(){
		return this.carrierID;
	}
	public ItemStack getMerchantInventory(int par1){
		return this.merchantInventory[par1];
	}
	
	public void setMerchantInventory(int par1,ItemStack is){
		this.merchantInventory[par1] = is;
	}
	
	public boolean hasMerchandiceInitialized(){
		return this.initMerchandice;
	}
	
	public void markMerchandiceInitialized(boolean par1){
		this.initMerchandice = par1;
	}
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		NBTTagList tagList = new NBTTagList();
		//Unsaga.debug("nbtへセーブ");
		UtilNBT.writeItemStacksToNBTTag(tagList, this.merchantInventory);
//		for(int i=0;i<9;i++){
//			if(this.merchantInventory[i]!=null){
//                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
//                nbttagcompound1.setByte("Slot", (byte)i);
//                this.merchantInventory[i].writeToNBT(nbttagcompound1);
//                tagList.appendTag(nbttagcompound1);
//			}
//		}
		
		compound.setTag("Bartering.Items", tagList);
		compound.setLong("Bartering.recentPurchase", this.recentPurchaseDate);
		
		compound.setBoolean("initialized", initMerchandice);
		compound.setBoolean("foundSecret", foundSecret);
		
		if(this.carrierID>=0){
			compound.setInteger("carrierID", this.carrierID);
		}
		
		compound.setInteger("distributionLevel", this.distributionLevel);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		Unsaga.debug("nbtから読み込み");
        NBTTagList nbttaglist = compound.getTagList("Bartering.Items",10);
        this.merchantInventory = new ItemStack[10];
        this.merchantInventory = UtilNBT.getItemStacksFromNBT(nbttaglist, this.merchantInventory.length);
        
//        this.merchantInventory = new ItemStack[10];
//        for (int i = 0; i < nbttaglist.tagCount(); ++i)
//        {
//            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
//            int j = nbttagcompound1.getByte("Slot") & 255;
//
//            if (j >= 0 && j < this.merchantInventory.length)
//            {
//                this.merchantInventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
//            }
//        }
		
        this.initMerchandice = compound.getBoolean("initialized");
        this.recentPurchaseDate = compound.getLong("Bartering.recentPurchase");
        this.foundSecret = compound.getBoolean("foundSecret");
        this.distributionLevel = compound.getInteger("distributionLevel");
        
        if(compound.hasKey("carrierID")){
        	this.carrierID = compound.getInteger("carrierID");
        }
	}

	public void setDistributionLevel(int par1){
		this.distributionLevel = par1;
	}
	
	public int getDistributionLevel(){
		return this.distributionLevel;
	}
	public void setFoundSecret(boolean par1){
		this.foundSecret = par1;
	}
	
	public boolean hasFoundSecret(){
		return this.foundSecret;
	}
	@Override
	public void init(Entity entity, World world) {
		this.carrierID = -1;
		this.distributionLevel = 0;
	}

	public static ExtendedMerchantData getData(EntityVillager villager){

		if(villager.getExtendedProperties(VILLAGER)!=null){
			return (ExtendedMerchantData) villager.getExtendedProperties(VILLAGER);
		}
		ExtendedMerchantData data = new ExtendedMerchantData();
		villager.registerExtendedProperties(VILLAGER, data);
		return data;
	}
}
