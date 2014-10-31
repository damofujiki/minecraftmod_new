package com.hinasch.unlsagamagic.event;

import com.hinasch.unlsaga.util.LockOnHelper;
import com.hinasch.unlsagamagic.item.ItemSpellBook;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventSpellTarget {


	@SubscribeEvent
	public void onLivingInteractForTarget(EntityInteractEvent e){
		if(e.target!=null){
			EntityPlayer ep = e.entityPlayer;
			if(ep.getHeldItem()==null)return;
			ItemStack is = ep.getHeldItem();
			World world = ep.worldObj;
			if(ep.isSneaking() && is.getItem() instanceof ItemSpellBook){
				if(!world.isRemote){
					if(e.target instanceof EntityLivingBase){
						LockOnHelper.setSpellTarget(ep, (EntityLivingBase) e.target);
					}
				}

			}
		}
	}
}
