package com.hinasch.unlsagamagic.event;

import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Buff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingBuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.network.packet.PacketSound;
import com.hinasch.unlsaga.util.FiveElements.Enums;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class AbstractBuffShield {

	public boolean isGuardAll;
	protected Buff parentBuff;
	public Enums element;
	protected PacketSound ps;
	
	public AbstractBuffShield(Buff parent,boolean isGuardAll,Enums element){
		this.parentBuff = parent;
		this.isGuardAll = isGuardAll;
		this.element = element;
		EventSpellBuff.shieldSet.add(this);
	}
	
	public void doGuard(LivingHurtEvent e){
		EntityLivingBase hurtLiving = e.entityLiving;
		if(this.isEffective(e)){
			if(LivingDebuff.hasDebuff(hurtLiving, parentBuff)){
				if(LivingDebuff.getLivingDebuff(hurtLiving, parentBuff).isPresent()){
					LivingBuff shield = (LivingBuff)LivingDebuff.getLivingDebuff(hurtLiving, parentBuff).get();
					if((!this.isGuardAll && hurtLiving.getRNG().nextInt(100)<shield.amp) || this.isGuardAll){
						e.ammount = 0;
						ps = new PacketSound(1022,e.entityLiving.getEntityId(),PacketSound.MODE.AUX);

						TargetPoint tp = PacketUtil.getTargetPointNear(e.entityLiving);
						Unsaga.packetPipeline.sendToAllAround(ps, tp);
					}
				}
			}
		}


		
	}
	
	abstract public boolean isEffective(LivingHurtEvent e);
}
