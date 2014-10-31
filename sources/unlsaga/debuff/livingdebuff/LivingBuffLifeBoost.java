package com.hinasch.unlsaga.debuff.livingdebuff;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class LivingBuffLifeBoost extends LivingBuff {

	public LivingBuffLifeBoost(Debuff par1, int par2, int amp) {
		super(par1, par2, amp);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void updateRemain(EntityLivingBase living){
		this.remain -= 1;
		if(living instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)living;
			if(ep.shouldHeal() && !ep.getFoodStats().needFood()){
				ep.heal(1.0F);
			}
		}else{
			if(this.remain % 3 == 0){
				living.heal(1);
			}
		}
		
		if(this.remain<=0){
			this.remain = 0;
		}
		Unsaga.debug(this.toString());
	}

}
