package com.hinasch.unlsaga.debuff.livingdebuff;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.network.packet.PacketParticle;

public class LivingStateHurricane extends LivingState{

	public int tick ;
	public float angle;
	public LivingStateHurricane(Debuff par1, int par2) {
		super(par1, par2, true);
		tick = 0;
		angle = 0;
	}

	
	@Override
	public void updateTick(EntityLivingBase living) {
		super.updateTick(living);
		tick +=1;
		angle +=25.0F;
		angle = MathHelper.wrapAngleTo180_float(angle);
		living.rotationYaw = angle;
		if(tick%2 ==0){
			living.addVelocity(0, 0.15, 0);
		}
		if(living.getRNG().nextInt(5)==1){
			PacketParticle pp = new PacketParticle(201,living.getEntityId(),3);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
		}
	}
}
