package com.hinasch.unlsaga.debuff.livingdebuff;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.item.weapon.ItemSwordUnsaga;
import com.hinasch.unlsaga.util.rangedattack.CauseKnockBack;

public class LivingState extends LivingDebuff{

	public boolean isOnlyAir = false;
	public int startRemain;
	
	public LivingState(Debuff par1, int par2,boolean par3) {
		super(par1, par2);
		this.isOnlyAir = par3;
		this.startRemain = par2;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public String toString(){
		return this.debuff.number+":"+this.remain+":"+(this.isOnlyAir ? "0" : "-1");
	}
	
	@Override
	public void updateRemain(EntityLivingBase living){
		Unsaga.debug(this.debuff+"updateremain");
		this.remain -= 1;
		if(this.remain<=0){

			this.remain = 0;
		}


	}
	
	@Override
	public void updateTick(EntityLivingBase living) {
		super.updateTick(living);
		if(this.isOnlyAir && living.onGround && this.remain<this.startRemain){
			this.remain = 0;
			Unsaga.debug("地面にいますので消します",this.getClass());
		}
		if(this.debuff==Unsaga.debuffManager.sleep){
			living.setVelocity(0, 0, 0);
		}
		if(this.debuff==Unsaga.debuffManager.gust){
			this.remain -= 1;
			living.moveForward = 0.5F;
			AxisAlignedBB bb = HSLibs.getBounding(XYZPos.entityPosToXYZ(living), 2.0D, 1.0D);
			List<EntityLivingBase> livings = living.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			if(!livings.isEmpty()){
				for(EntityLivingBase lv:livings){
					if(lv!=living){
						lv.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)living),Unsaga.abilityManager.gust.getStrHurtHP());

						this.remain = 0;
					}
				}
				
			}
		}
		if(this.debuff==Unsaga.debuffManager.rushBlade){
			
			if(living instanceof EntityPlayer){
				EntityPlayer ep = (EntityPlayer)living;

				if(ep.motionX + ep.motionZ <= 0.0001D){
					this.remain = 0;
				}
				if(ItemUtil.hasItemInstance(ep, ItemSwordUnsaga.class)){
					
					ep.setSneaking(true);
					InvokeSkill helper = new InvokeSkill(ep.worldObj,ep,Unsaga.abilityManager.chargeBlade,ep.getHeldItem());
					CauseKnockBack causeknock = new CauseKnockBack(ep.worldObj,1.0D);
					AxisAlignedBB bb = ep.boundingBox
							.expand(1.5D, 1.0D, 1.5D);
					//causeknock.setLPDamage(helper.getAttackDamageLP());
					causeknock.causeDamage(helper.getDamageSource(), bb,helper.getAttackDamage());
					
				
				}else{
					this.remain = 0;
				}

			}

		}
	}
	

}
