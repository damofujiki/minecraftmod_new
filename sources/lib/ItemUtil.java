package com.hinasch.lib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemUtil {

	public static EntityItem getEntityItem(ItemStack tablet,XYZPos pos,World world){
		return new EntityItem(world,pos.dx,pos.dy,pos.dz,tablet);
	}
	public static void dropItem(ItemStack dropItemStack,EntityPlayer ep){
		if(dropItemStack!=null){
			ep.entityDropItem(dropItemStack, 0.1F);
			
		}
	}

	public static void dropItem(World world,ItemStack itemstack,double x,double y,double z){
	
		EntityItem item = new EntityItem(world, x,y,z,itemstack);
		if(!world.isRemote){
			world.spawnEntityInWorld(item);
		}
		return;
	}

	public static void dropItem(World world,ItemStack itemstack,XYZPos xyz){
		dropItem(world, itemstack, xyz.dx, xyz.dy, xyz.dz);
		return;
	}

	public static int getPotionTime(int sec){
		return sec*20;
	}

	public static void removePotionEffects(EntityLivingBase living,Potion... potions){
		for(Potion potion:potions){
			living.removePotionEffect(potion.id);
		}
	}

	public static void addPotionIfLiving(Entity entity,PotionEffect potionEffect){
		if(entity instanceof EntityLivingBase){
			EntityLivingBase el = (EntityLivingBase)entity;
			el.addPotionEffect(potionEffect);
		}
	}

	public static boolean isSameClass(ItemStack is,Class _class){
	
			if(_class.isInstance(is.getItem())){
				return true;
			}
			if(is.getItem().getClass()==_class){
				return true;
			}
		
		return false;
		
	}

	public static boolean hasItemInstance(EntityLivingBase player,Class _class){
		if(player.getHeldItem()!=null){
			if(_class.isInstance(player.getHeldItem().getItem())){
				return true;
			}
			if(player.getHeldItem().getItem().getClass()==_class){
				return true;
			}
		}
		return false;
		
	}
}
