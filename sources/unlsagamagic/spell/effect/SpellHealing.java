package com.hinasch.unlsagamagic.spell.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;

import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.util.ChatUtil;
import com.hinasch.unlsaga.util.translation.Translation;

public abstract class SpellHealing extends SpellBase{

	protected boolean isSelf = false;
	
	public SpellHealing(){
		
	}


	@Override
	public void invokeSpell(InvokeSpell parent) {
		float heal = parent.spell.getStrHurtHP()*parent.getAmp();
		Unsaga.debug(this.getClass(),parent.spell.getStrHurtHP(),parent.getAmp());
		if(!(parent.owner instanceof IMob) && parent.getTarget().isPresent() && !this.isSelf){

			EntityLivingBase target = parent.getTarget().get();
			target.heal(heal);			
			this.hookOnHealed(parent,target);
			String mesbase = Translation.localize("msg.heal");
			String formatted = String.format(mesbase, target.getCommandSenderName(),Math.round(heal));
			ChatUtil.addMessageNoLocalized(parent.getInvoker(), formatted);
			PacketParticle pp = new PacketParticle(3,target.getEntityId(),10);

			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(target));
			
			
		}else{
			parent.getInvoker().heal(heal);
			this.hookOnHealed(parent,parent.getInvoker());
			String mesbase = Translation.localize("msg.heal");
			String formatted = String.format(mesbase, parent.getInvoker().getCommandSenderName(),Math.round(heal));
			ChatUtil.addMessageNoLocalized(parent.getInvoker(), formatted);
			PacketParticle pp = new PacketParticle(3,parent.getInvoker().getEntityId(),3);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(parent.getInvoker()));
			
		}
	}

	abstract public void hookOnHealed(InvokeSpell parent, EntityLivingBase target);
	


}
