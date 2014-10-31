package com.hinasch.unlsaga.entity.projectile;

import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityThrowableUnsaga extends EntityThrowable implements IProjectile{

	protected EntityLivingBase shootingEntity;
	
	public EntityThrowableUnsaga(World par1World) {
		super(par1World);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1.entityHit!=null){
			if(var1.entityHit!=this.getThrower()){
				onImpactEntity(var1.entityHit);
			}
				
		}
		
	}

	private void onImpactEntity(Entity hitEntity) {
		hitEntity.attackEntityFrom(getDamageSource(),1.0F );
		
	}
	
	protected float getAttackDamage(){
		return 1.0F;
	}
	
	protected DamageSource getDamageSource(){
		return new DamageSourceUnsaga("mob",this.getThrower(),0.1F,DamageHelper.Type.PUNCH);
	}

}
