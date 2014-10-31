package com.hinasch.unlsaga.event;
//package hinasch.mods.unlsaga.core.event;
//
//import hinasch.mods.unlsaga.Unsaga;
//import hinasch.mods.unlsaga.core.init.UnsagaItems.EnumSelecterItem;
//import hinasch.mods.unlsaga.item.weapon.ItemBowUnsaga;
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.ai.EntityAIArrowAttack;
//import net.minecraft.entity.monster.EntitySkeleton;
//import net.minecraft.entity.monster.EntityZombie;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.projectile.EntityArrow;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.World;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//import net.minecraftforge.event.entity.living.LivingSpawnEvent;
//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
//
////バグのもとのため休眠中
//public class EventInitEnemyWeapon {
//
//	private EntityAIArrowAttack aiArrowAttack;
//
//	@SubscribeEvent 
//	public void EnemySetWeaponEvent(LivingSpawnEvent e){
//		if(e.world.rand.nextInt(3)>0){
//			return;
//		}
//		if(e.entityLiving instanceof EntityZombie){
//			EntityZombie zombi = (EntityZombie)e.entityLiving;
//			if(zombi.getHeldItem()==null && e.world.rand.nextInt(3)>0){
//				ItemStack weapon = Unsaga.items.getRandomWeapon(e.world.rand,this.figurePlayerLV(e.world, zombi),EnumSelecterItem.WEAPONONLY);
//				zombi.setCurrentItemOrArmor(0, weapon);
//			}
//		}
//		if(e.entityLiving instanceof EntitySkeleton){
//			EntitySkeleton ske = (EntitySkeleton)e.entityLiving;
//			ItemStack weapon = null;
//			switch(ske.getSkeletonType()){
//			case 0:
//				
//				
//				aiArrowAttack = new EntityAIArrowAttack(ske, 1.0D, 20, 60, 15.0F);
//				
//				
//				weapon = Unsaga.items.getRandomWeapon(e.world.rand,this.figurePlayerLV(e.world, ske),EnumSelecterItem.BOWONLY);
//				
//				ske.setCurrentItemOrArmor(0, weapon);
//				ske.tasks.addTask(4, aiArrowAttack);
//				break;
//			case 1: //wither ske
//				weapon = Unsaga.items.getRandomWeapon(e.world.rand,this.figurePlayerLV(e.world, ske),EnumSelecterItem.WEAPONONLY);
//				ske.setCurrentItemOrArmor(0, weapon);
//				break;
//			}
//		}
//	}
//	
//	@SubscribeEvent 
//	public void SkeletonAttackWithUnsagaWeapon(LivingHurtEvent e){
//		if(e.source.getEntity()!=null){
//			Unsaga.debug("getEntity:"+e.source.getEntity());
//			Unsaga.debug("getSourceEntity:"+e.source.getSourceOfDamage());
//			if(e.source.getEntity() instanceof EntitySkeleton){
//
//				if(e.source.getSourceOfDamage() instanceof EntityArrow){
//					EntitySkeleton ske = (EntitySkeleton)e.source.getEntity();
//					ItemStack bow = ske.getHeldItem();
//					if(bow.getItem() instanceof ItemBowUnsaga){
//						ItemBowUnsaga bowu = (ItemBowUnsaga)bow.getItem();
//						int modifier = bowu.getStrengthModifier(bow);
//						e.ammount += modifier;
//					}
//				}
//			}
//		}
//	}
//	
//	public int figurePlayerLV(World world,EntityLiving enemy){
//	
//		int rank = 0;
//		EntityPlayer closePlayer = world.getClosestPlayerToEntity(enemy, 300D);
//		if(closePlayer!=null){
//			if(closePlayer.experienceLevel>5){
//				rank ++;
//			}
//			if(closePlayer.experienceLevel>10){
//				rank ++;
//			}
//			if(closePlayer.experienceLevel>15){
//				rank ++;
//			}
//			if(closePlayer.experienceLevel>20){
//				rank ++;
//			}
//			if(closePlayer.experienceLevel>23){
//				rank ++;
//			}
//			if(closePlayer.experienceLevel>26){
//				rank ++;
//			}
//			
////			for(int i=0;i<4;i++){
////				if(closePlayer.inventory.armorItemInSlot(i)!=null){
////					
////					if(closePlayer.inventory.armorItemInSlot(i).isItemEnchanted()){
////						rank ++;
////					}
////					
////				}
////			}
//			
//			
//		}
//		return rank;
//	}
//}
