package com.hinasch.unlsagamagic.spell.effect;

import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.network.packet.PacketSound;

import net.minecraft.entity.Entity;

public class SpellProjectile extends SpellBase{

	public void hookStart(InvokeSpell parent){
		PacketSound ps = new PacketSound(1008,parent.getInvoker().getEntityId(),PacketSound.MODE.AUX);
		Unsaga.packetPipeline.sendToAllAround(ps, PacketUtil.getTargetPointNear(parent.getInvoker()));
	}
	
	public void hookEnd(InvokeSpell parent){
		
	}
	
	@Override
	public void invokeSpell(InvokeSpell parent) {
		this.hookStart(parent);
		Entity projectile = this.getProjectileEntity(parent);
		if (!parent.world.isRemote)
		{
			parent.world.spawnEntityInWorld(projectile);
		}
		this.hookEnd(parent);
		return;
		
	}

	public Entity getProjectileEntity(InvokeSpell parent){
		return null;
	}
}
