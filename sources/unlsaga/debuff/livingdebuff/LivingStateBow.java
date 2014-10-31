package com.hinasch.unlsaga.debuff.livingdebuff;

import com.hinasch.lib.ItemUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.item.weapon.ItemBowUnsaga;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class LivingStateBow extends LivingState{

	
	public int shootTick;
	public String tag;
	public float charge;
	InvokeSkill helper;
	
	public LivingStateBow(Debuff par1, int par2, boolean par3,int par4,String tag,float par5) {
		super(par1, par2, false);
		if(tag.equals("double")){
			this.shootTick = 2;
		}else{
			this.shootTick = 3;
		}
		Unsaga.debug(this.shootTick);
		this.tag = tag;
		this.charge = par5;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public String toString(){
		return this.buildSaveString(this.debuff.number,this.remain,this.shootTick,this.tag,this.charge);
	}
	
	@Override
	public void updateTick(EntityLivingBase living) {
		Unsaga.debug("呼ばれてますtick",this.getClass());
		if(this.shootTick<0){
			this.remain = 0;
		}
		if(living instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)living;
			if(ItemUtil.hasItemInstance(ep, ItemBowUnsaga.class)){
				if(helper==null){
					if(this.tag.equals("triple")){
						helper =  new InvokeSkill(ep.worldObj,ep,Unsaga.abilityManager.tripleShot,ep.getHeldItem());
					}else{
						helper =  new InvokeSkill(ep.worldObj,ep,Unsaga.abilityManager.doubleShot,ep.getHeldItem());
					}
					
				}
				Unsaga.debug(shootTick);

				helper.setCharge(charge);
				helper.setParent(this);
				if(this.shootTick>0){
					helper.doSkill();
				}
				this.shootTick -= 1;
				

			}
		}
		
		
	}
	
	
}
