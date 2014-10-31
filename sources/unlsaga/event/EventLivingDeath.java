package com.hinasch.unlsaga.event;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;
import com.hinasch.unlsagamagic.UnsagaMagic;
import com.hinasch.unlsagamagic.item.ItemTablet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventLivingDeath {

	protected WorldHelper worldHelper;
	protected UnsagaMagic magic = Unsaga.magic;

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e){
		EntityLivingBase entityDeath = e.entityLiving;
		Entity attacker = e.source.getEntity();
		this.worldHelper = new WorldHelper(e.entityLiving.worldObj);

		//プレイヤーが死亡
		if(entityDeath instanceof EntityPlayer){
			ExtendedPlayerData.dropAccessoriesOnDeath(e);
		}

		//プレイヤーがモブを倒した時
		if(attacker!=null){
			//モブが死亡
			// ->LivingDropEvent に移動
//			if(entityDeath instanceof EntityMob || e.entityLiving instanceof IMob){
//				enemyDropEvent(e);
//			}
			if(attacker instanceof EntityPlayer){
				EntityLivingBase enemy = e.entityLiving;
				EntityPlayer ep = (EntityPlayer) attacker;
				if(enemy instanceof EntityAnimal && SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.noKillingAnimals)>=0){
					DamageSourceUnsaga uds = new DamageSourceUnsaga(DamageSource.magic);
					uds.setStrengthLPHurt(2.0F);
					ep.attackEntityFrom(uds, (SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.noKillingAnimals)+1)*2);
				}
				Ability.gainAbilityEventOnLivingDeath(e, enemy);

			}
		}
	}



	//ドロップイベント、おもにタブレットやチェスト
	public void enemyDropEvent(LivingDeathEvent e){
		boolean isMiniSlime = false;
		EntityLivingBase entityDeath = e.entityLiving;
		if(entityDeath instanceof EntitySlime){
			if(((EntitySlime)entityDeath).getSlimeSize()<=1){
				isMiniSlime = true;
			}
		}
		if(entityDeath instanceof EntityTreasureSlime){
			if(((EntityTreasureSlime)entityDeath).getSlimeSize()<=1){
				isMiniSlime = true;
			}
		}
		Random rand = entityDeath.getRNG();
		World world = entityDeath.worldObj;
		XYZPos positionDeath = XYZPos.entityPosToXYZ(e.entityLiving);
		
		if(entityDeath instanceof EntityTreasureSlime && !isMiniSlime){

			if(rand.nextInt(100)<20){
				ItemStack tablet = ItemTablet.getRandomMagicTablet(rand);

				ItemUtil.dropItem(world, tablet, positionDeath);
				return;
			}


		}
		if(rand.nextInt(100)<20 && !isMiniSlime){
			if(!world.isRemote){
				if(worldHelper.isAirBlock(positionDeath)){
					worldHelper.setBlock(positionDeath, Unsaga.blocks.bonusChest);
				}
			}
		}
	}

	public void isGainAbility(){

	}
}
