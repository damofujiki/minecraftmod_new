package com.hinasch.unlsaga.util.rangedattack;

import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class CauseExplode extends RangeDamageUnsaga{


	public CauseExplode(World world, InvokeSkill parent) {
		super(world, parent);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void hookOnMobHasAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
		if(this.helper.owner.onGround){
			this.world.createExplosion(ds.getEntity(), mob.posX, mob.posY, mob.posZ, 0.6F * (float)helper.getModifiedAttackDamage(false,0)/2, false);
		}else{
			this.world.createExplosion(ds.getEntity(), mob.posX, mob.posY, mob.posZ, 0.6F * (float)helper.getModifiedAttackDamage(false,0)/2, true);
		}
		
	}
}
