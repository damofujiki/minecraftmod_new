package com.hinasch.unlsaga.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.ChatHandler;
import com.hinasch.unlsaga.util.translation.Translation;

public class ChatUtil {

	public static void addMessage(EntityLivingBase ep,String mes){
		if(ep instanceof EntityPlayer){
			ChatHandler.sendChatToPlayer((EntityPlayer) ep, Translation.localize(mes));
		}
		                            
		
	}
	
	public static void addMessageNoLocalized(EntityLivingBase ep,String mes){
		if(ep instanceof EntityPlayer){
			ChatHandler.sendChatToPlayer((EntityPlayer) ep, mes);
		}
		
	}
}
