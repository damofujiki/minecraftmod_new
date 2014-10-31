package com.hinasch.unlsaga.entity.projectile;

import com.hinasch.lib.base.EntityThrowableBase;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBoulderNew extends EntityThrowableBase{

	public EntityBoulderNew(World par1World) {
		super(par1World);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public EntityBoulderNew(World par1World, EntityLivingBase par2EntityLiving, float par3)
	{
		super(par1World,par2EntityLiving,par3);
		

	}
	
	@Override
	protected void onImpact(MovingObjectPosition mop) {
		this.worldObj.spawnParticle("largeexplode", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
		if(mop.typeOfHit==MovingObjectPosition.MovingObjectType.ENTITY){
			if(mop.entityHit!=null){
				Entity hitEntity = mop.entityHit;
				DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.getThrower(),1,DamageHelper.Type.PUNCH,this);
				ds.setSubDamageType(DamageHelper.SubType.NONE);
				ds.setMagicDamage();
				hitEntity.attackEntityFrom(ds, this.getDamage());
				int j = this.getKnockBackModifier();
				Entity attacker = this.getThrower();
				hitEntity.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.3F), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.3F));

			}
		}
		this.setDead();
	}
}
