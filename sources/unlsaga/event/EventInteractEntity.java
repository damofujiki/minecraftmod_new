package com.hinasch.unlsaga.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.event.extendeddata.ExtendedEntityTag;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.item.weapon.ItemAxeUnsaga;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;
import com.hinasch.unlsaga.util.LockOnHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventInteractEntity {

	@SubscribeEvent 
	public void onPlayerInteractsVillager(EntityInteractEvent e){
		onPlayerTargetting(e);

		if(e.target instanceof EntityVillager){


			interactsVillager(e,e.entityPlayer,(EntityVillager) e.target,true);
		}
		
		if(e.entityPlayer.getHeldItem()==null && SkillPanels.getHighestLevelOfPanel(e.target.worldObj, e.entityPlayer, Unsaga.skillPanels.punch)>=1 && e.target instanceof EntityLivingBase){
			if(!(e.target instanceof EntityEnderman)){
				Unsaga.debug("kitemasu",this.getClass());
				e.entityPlayer.swingItem();
				Skill skill = null;
				if(SkillPanels.getHighestLevelOfPanel(e.target.worldObj, e.entityPlayer, Unsaga.skillPanels.punch)>=4){
					skill = Unsaga.abilityManager.cyclone;
				}
				if(SkillPanels.getHighestLevelOfPanel(e.target.worldObj, e.entityPlayer, Unsaga.skillPanels.punch)==2){
					skill = Unsaga.abilityManager.callBack;
				}
				if(SkillPanels.getHighestLevelOfPanel(e.target.worldObj, e.entityPlayer, Unsaga.skillPanels.punch)==1){
					skill = Unsaga.abilityManager.airThrow;
				}
				if(skill!=null){
					Unsaga.debug(skill,this.getClass());
					InvokeSkill invoker = new InvokeSkill(e.target.worldObj, e.entityPlayer, skill, null);
					invoker.setParent(e);
					invoker.doSkill();
					if(e.entityPlayer.getRNG().nextInt(5)==1){
						FoodStats foodStats = e.entityPlayer.getFoodStats();
						foodStats.setFoodLevel(foodStats.getFoodLevel()-skill.getDamageForWeapon());
					}

				}
			}


		}
		
	}

	public void throwing(EntityLivingBase enemy,EntityLivingBase attacker){
//		World world = enemy.worldObj;
//		float dis = enemy.getDistanceToEntity(attacker);
//		Vec3 v1 = attacker.getLookVec();
//		if(dis<1.8F){
//			attacker.swingItem();
//			enemy.addVelocity(v1.xCoord, 1.5D,v1.zCoord);
//			LivingDebuff.addLivingDebuff(enemy, new LivingStateHurricane(Unsaga.debuffManager.hurricane, 10));
//		}
		
	}
	public static void interactsVillager(EntityInteractEvent e, EntityPlayer ep,EntityVillager villager,boolean requireSneak){
		int profession = villager.getProfession();
		if(profession==Unsaga.configs.villagerID_Carrier && e!=null){
			e.setCanceled(true);
			if(!ep.worldObj.isRemote){
				XYZPos p = XYZPos.entityPosToXYZ(villager);
				WorldSaveDataUnsaga worldData = WorldSaveDataUnsaga.getData(villager.worldObj);
				ExtendedMerchantData data = ExtendedMerchantData.getData(villager);
				if(data.getCarrierID()<=0){
					int id = worldData.getNextCarrierID();
					data.setCarrierID(id);
					worldData.addCarrierData(id,villager);
					worldData.setDirty(true);
				}
				if(!ep.worldObj.isRemote){
					Unsaga.debug("Carrier ID:"+data.getCarrierID(),"interacts");
				}

				ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
				pdata.setMerchant(villager);
				villager.setCustomer(ep);
				PacketGuiOpen pgo = new PacketGuiOpen(Unsaga.guiNumber.CARRIER);
				Unsaga.packetPipeline.sendToServer(pgo);
			}


			//ep.openGui(Unsaga.instance, guiNumber.CARRIER, ep.worldObj, p.x, p.y, p.z);
			return;
		}
		if(!requireSneak ||(ep.isSneaking() && requireSneak)){
			XYZPos xyz = XYZPos.entityPosToXYZ(ep);
			
			Unsaga.debug("村人:"+profession,"カスタマー:"+villager.getCustomer());

			if(profession==3){ //鍛冶屋
				Unsaga.debug("鍛冶屋です");
				villager.setCustomer(ep);
				ExtendedPlayerData data = ExtendedPlayerData.getData(ep);
				data.setMerchant(villager);
				ExtendedEntityTag.initVillager(villager);
				PacketGuiOpen pgo = new PacketGuiOpen(Unsaga.guiNumber.SMITH,villager);
				Unsaga.packetPipeline.sendToServer(pgo);


			}
			if(profession==0 || profession==4){ //農家か肉屋
				villager.setCustomer(ep);
				ExtendedPlayerData data = ExtendedPlayerData.getData(ep);
				data.setMerchant(villager);
				XYZPos pos = XYZPos.entityPosToXYZ(ep);
				PacketGuiOpen pgo = new PacketGuiOpen(Unsaga.guiNumber.BARTERING,villager);
				Unsaga.packetPipeline.sendToServer(pgo);

			}
		}
	}

	public void onPlayerTargetting(EntityInteractEvent e){
		if(e.target!=null){
			EntityPlayer ep = e.entityPlayer;
			//スカイドライブにしても、ロックオン指定しない場合、投げる「前」にひろうようにするとよさそう
			if(ep.getHeldItem()==null)return;
			ItemStack is = ep.getHeldItem();
			World world = ep.worldObj;
			if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.skyDrive, is) && ep.isSneaking() && is.getItem() instanceof ItemAxeUnsaga){
				if(!world.isRemote){
					if(e.target instanceof EntityLivingBase){
						LockOnHelper.setAttackTarget(ep, (EntityLivingBase) e.target);
					}
				}

			}
		}
	}



}
