package com.hinasch.unlsaga.inventory;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.Village;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.villager.bartering.MerchandiseInfo;

public class MerchantHelper {

	protected InventoryMerchantUnsaga invMerchant;
	protected EntityVillager villager;
	protected final int SLOT_SECRET = 8;
	
	public MerchantHelper(InventoryMerchantUnsaga par1){
		this.invMerchant = par1;
		this.villager = this.invMerchant.villager;
	}
	
	public void changeMerchandiseStock(Random rand){
		for(int i=0;i<9;++i){
			if(i == SLOT_SECRET){
				this.invMerchant.setMerchandise(i, null);
			}else{
				int repu =getHighestDistributionLevel(this.villager.worldObj, XYZPos.entityPosToXYZ(villager), 100.0D); //this.getVillageReputation();
				Unsaga.debug(repu);
				this.syncVillagerDistributionLevel(repu);
				ItemStack mis = MerchandiseInfo.getRandomMerchandise(rand,repu);
				MerchandiseInfo.setBuyPriceTag(mis,MerchandiseInfo.getPrice(mis)*3);
				this.invMerchant.setMerchandise(i, mis);
			}

			

		}
		ExtendedMerchantData data = ExtendedMerchantData.getData(this.villager);
		data.setFoundSecret(false);
	}
	
	public void syncVillagerDistributionLevel(int par1){
		ExtendedMerchantData.getData(this.villager).setDistributionLevel(par1);
	}
	public int getHighestDistributionLevel(World world,XYZPos pos,double range){
		List<EntityVillager> villagers = world.getEntitiesWithinAABB(EntityVillager.class, HSLibs.getBounding(pos, range, range));

		int highest = 0;
		for(EntityVillager avillager:villagers){
			Unsaga.debug(ExtendedMerchantData.getData(avillager).getDistributionLevel());
			if(highest<ExtendedMerchantData.getData(avillager).getDistributionLevel()){

				highest = ExtendedMerchantData.getData(avillager).getDistributionLevel();
			}
		}

		Village village = world.villageCollectionObj.findNearestVillage(pos.x, pos.y, pos.z, 300);
		int limitter = 0;
		if(village!=null){
			if(village.getNumVillagers()>10){
				limitter = 1;
			}
			if(village.getNumVillagers()>20){
				limitter = 2;
			}
		}
		if(limitter==0){
			if(highest>10){
				highest = 10;
			}
			
		}
		if(limitter==1){
			if(highest>20){
				highest = 20;
			}
		}
		Unsaga.debug("流通度："+highest);
		return highest;
	}
	

	public void initMerhcnadiseStock(IMerchant merchant){
		Unsaga.debug(merchant,this.villager,this.invMerchant.theCustomer);
		ExtendedMerchantData data = ExtendedMerchantData.getData((EntityVillager) merchant);
		if(!data.hasMerchandiceInitialized()){
			this.changeMerchandiseStock(villager.getRNG());
			data.recentPurchaseDate = villager.worldObj.getTotalWorldTime();
			data.markMerchandiceInitialized(true);
		}else{
			for(int i=0;i<9;++i){
				this.invMerchant.setMerchandise(i, data.getMerchantInventory(i));

			}
		}


		Unsaga.debug("現在時刻:"+villager.worldObj.getTotalWorldTime()," 以前の仕入れ時間:"+data.recentPurchaseDate,"次の仕入れ時間:"+(data.recentPurchaseDate+(long)24000));
		if((villager.worldObj.getTotalWorldTime() - data.recentPurchaseDate)>=24000){
			this.changeMerchandiseStock(villager.getRNG());
			data.recentPurchaseDate = villager.worldObj.getTotalWorldTime();
			Unsaga.debug(villager.worldObj.getWorldTime());
			Unsaga.debug(data.recentPurchaseDate);
		}
	}
	
	public void registerMerchandiseStock(){
		ExtendedMerchantData data = ExtendedMerchantData.getData(villager);
		for(int i=0;i<9;i++){
			data.setMerchantInventory(i, this.invMerchant.getMerchandise(i));
		}
	}
}
