package com.hinasch.unlsaga.event;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsagamagic.item.ItemTablet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventLivingDrops {

	protected WorldHelper wh;
	
	@SubscribeEvent
	public void hookEntityDrop(LivingDropsEvent e){
		EntityLivingBase entityDrops = e.entityLiving;
		Random rand = entityDrops.getRNG();
		World world = entityDrops.worldObj;
		XYZPos positionDeath = XYZPos.entityPosToXYZ(entityDrops);

		if(wh==null){
			wh = new WorldHelper(world);
		}

		if(e.source.getEntity() instanceof EntityLivingBase & isKilledByPlayer((EntityLivingBase) e.source.getEntity())){
			if(entityDrops instanceof EntitySlime || entityDrops instanceof EntityTreasureSlime){
				Unsaga.debug(e.specialDropValue);
				if(this.getSlimeSize(entityDrops)>1 && e.specialDropValue<this.getRareDropProb(entityDrops,e.lootingLevel)*2){
					ItemStack tablet = ItemTablet.getRandomMagicTablet(rand);
					
					if(tablet!=null){
						e.drops.add(ItemUtil.getEntityItem(tablet, positionDeath, entityDrops.worldObj));
						return;
					}
				}
				
			}
			if(entityDrops instanceof EntityWitch && e.specialDropValue<this.getRareDropProb(entityDrops,e.lootingLevel)){
				ItemStack tablet = ItemTablet.getRandomMagicTablet(rand);
				
				if(tablet!=null){
					e.drops.add(ItemUtil.getEntityItem(tablet, positionDeath, entityDrops.worldObj));
					return;
				}
			}
			if(entityDrops instanceof IMob){
				if(!world.isRemote && e.specialDropValue<this.getChestDropValue(entityDrops,e.lootingLevel)*2){
					if(wh.isAirBlock(positionDeath)){
						wh.setBlock(positionDeath, Unsaga.blocks.bonusChest);
					}
				}
			}
		}

	}
	
	public boolean isKilledByPlayer(EntityLivingBase attacker){
		if(attacker instanceof EntityPlayer){
			return true;
		}
		if(attacker instanceof EntityTameable){
			if(((EntityTameable) attacker).isTamed()){
				return true;
			}
		}
		return false;
	}
	
	public int getChestDropValue(EntityLivingBase entityDrops,int looting){
		int prob = 20;
		if(entityDrops instanceof EntitySlime){
			switch(this.getSlimeSize(entityDrops)){
			case 0:
				prob = 3;
				break;
			case 1:
				prob = 7;
				break;
			case 2:
				prob = 20;
				break;
			default:
				prob = 20;
				break;
			}
			
			
		}
		if(entityDrops instanceof EntityEnderman){
			prob = 30;
		}
		if(entityDrops instanceof EntitySilverfish){
			prob = 10;
		}
		prob = 20;
		return prob + (10*looting);
	}
	
	public int getRareDropProb(EntityLivingBase entityDrops,int looting){
		int prob = 5;
		if(entityDrops instanceof EntityTreasureSlime){
			prob =  30;
		}
		if(entityDrops instanceof EntitySlime){
			switch(this.getSlimeSize(entityDrops)){
			case 0:
				prob = 3;
				break;
			case 1:
				prob = 15;
				break;
			case 2:
				prob = 20;
				break;
			default:
				prob = 15;
				break;
			}
			
		}
		prob = 5;
		return prob + (10*looting);
	}
	public int getSlimeSize(EntityLivingBase living){
		if(living instanceof EntitySlime){
			return ((EntitySlime) living).getSlimeSize();
		}
		if(living instanceof EntityTreasureSlime){
			return 2;
		}
		return -1;
	}
}
