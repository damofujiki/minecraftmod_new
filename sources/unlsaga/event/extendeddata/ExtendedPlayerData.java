package com.hinasch.unlsaga.event.extendeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.google.common.base.Optional;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtendedPlayerData implements IExtendedEntityProperties{

	protected ItemStack[] invAccessories;

	protected ItemStack invTablet;
	
	

	protected int contractedCarrying;

	//TODO:タブレットインベントリ
	protected EntityVillager merchant;
	public final static String KEY = "unsaga.equipment";
	
	public int getContractedCarryingID(){
		return this.contractedCarrying;
	}
	
	public Optional<Integer> getEmptyAccessorySlot(){
		int index = 0;
		for(ItemStack is:this.invAccessories){
			if(is==null){
				return Optional.of(index);
			}
			index +=1;
		}
		return Optional.absent();
	}
	public void setContractedCarrying(int par1){
		this.contractedCarrying = par1;
	}
	protected int getInventoryLength(){
		return 3;
	}
	
	public ItemStack getAccessory(int par1){
		if(par1==2){
			return this.invTablet;
		}
		return this.invAccessories[par1];
	}
	
	public ItemStack[] getAccessories(){
		return invAccessories;
	}
	
	public ItemStack[] getAccessoriesAndTablet(){
		ItemStack[] iss = new ItemStack[3];
		iss[0] = this.invAccessories[0];
		iss[1] = this.invAccessories[1];
		iss[2] = this.invTablet;
		return iss;
	}
	public ItemStack getSkillPanel(int num){
		return this.getSkillPanel(num);
	}
	
//	public void setSkillPanel(int num,ItemStack panel){
//		this.invSkillPanels[num] = panel;
//	}
//	
//	public ItemStack[] getSkillPanels(){
//		return this.invSkillPanels;
//	}
	
	public ItemStack getTablet(){
		return this.invTablet;
	}
	public Optional<EntityVillager> getMerchant(){
		if(this.merchant!=null){
			return Optional.of(this.merchant);
		}
		return Optional.absent();
	}
	
	public void setMerchant(EntityVillager villager){
		this.merchant = villager;
	}
	
	public void setAccessory(int par1,ItemStack par2){
		if(par1==2){
			this.invTablet = par2;
			return;
		}
		this.invAccessories[par1] = par2;
	}
	public void setAccessories(ItemStack i1,ItemStack i2){
		this.invAccessories[0] = i1;
		this.invAccessories[1] = i2;
	}
	
	public void setTablet(ItemStack tablet){
		this.invTablet = tablet;
	}
	
	@SubscribeEvent
	public void attachDataEvent(EntityConstructing e){
	
		if(e.entity instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)e.entity;
			if(ep.getExtendedProperties(KEY)==null){
				ep.registerExtendedProperties(KEY, new ExtendedPlayerData());
			}
			
		}
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		

		
		NBTTagList tagList = new NBTTagList();
		Unsaga.debug("nbtへセーブ");
		UtilNBT.writeItemStacksToNBTTag(tagList, this.getAccessoriesAndTablet());
		compound.setTag("Items", tagList);
		

		if(this.contractedCarrying>=0){
			compound.setInteger("carry.id", this.contractedCarrying);
		}
//		NBTTagList tagListPanel = new NBTTagList();
//		UtilNBT.writeItemStacksToNBTTag(tagListPanel, invSkillPanels);
//
//		compound.setTag("Panels", tagListPanel);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		Unsaga.debug("nbtから読み込み");
        NBTTagList nbttaglist = UtilNBT.getTagList(compound, "Items");
        this.init(null, null);
        ItemStack[] iss = UtilNBT.getItemStacksFromNBT(nbttaglist, 3);
        for(int i=0;i<iss.length;i++){
        	if(iss[i]!=null){
        		this.setAccessory(i, iss[i].copy());
        	}
        }

        if(compound.hasKey("carry.id")){
        	this.contractedCarrying = compound.getInteger("carry.id");
        }
//        NBTTagList tagListPanel = UtilNBT.getTagList(compound, "Panels");
//        this.invSkillPanels = UtilNBT.getItemStacksFromNBT(tagListPanel, 9);
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO 自動生成されたメソッド・スタブ
		this.invAccessories = new ItemStack[2];
		this.invTablet = null;
		this.contractedCarrying = -1;
//		this.invSkillPanels = new ItemStack[9];
	}
	
	public static ExtendedPlayerData getData(EntityPlayer ep){
		ExtendedPlayerData data = (ExtendedPlayerData)ep.getExtendedProperties(KEY);
		if(data!=null){
			return data;
		}
		data = new ExtendedPlayerData();
		return data;
	}
	
	public static void dropAccessoriesOnDeath(LivingDeathEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer))return;
		EntityPlayer ep = (EntityPlayer)e.entityLiving;
			ExtendedPlayerData data = ExtendedPlayerData.getData(ep);
			for(ItemStack is:data.getAccessories()){
				ItemUtil.dropItem(is, ep);
			}
			ItemUtil.dropItem(data.getTablet(), ep);
		
	}



}
