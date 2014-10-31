package com.hinasch.unlsagamagic.spell.effect;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.ChatUtil;

public abstract class SpellDetectEntity extends SpellBase{
	
	public boolean isAmplified;
	public SpellDetectEntity(){
		
	}


	@Override
	public void invokeSpell(InvokeSpell spell) {
		double range = 50.0D;
		this.isAmplified = spell.getAmp()>1.5F;
		
		AxisAlignedBB bb = spell.getInvoker().boundingBox.expand(range, range,range);
		List<EntityLivingBase> entlist = spell.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		StringBuilder strbuilder = new StringBuilder();
		Multimap<String,Object> entityList =  ArrayListMultimap.create();
		for(EntityLivingBase ent:entlist){
			Unsaga.debug(spell.spell.nameJp);

			this.addEntityList(spell, entityList, ent);
		}
		
		for(String key:entityList.keySet()){
			strbuilder.append(key).append(":").append(entityList.get(key)).append("/");
		}
		String stri = new String(strbuilder);
		if(!spell.world.isRemote && !stri.equals("")){
			ChatUtil.addMessageNoLocalized(spell.getInvoker(), new String(strbuilder));
			//spell.invoker.addChatMessage(new String(strbuilder));
		}
	}
		
	abstract public void addEntityList(InvokeSpell invoke,Multimap<String,Object> entityList,EntityLivingBase entity);
}
