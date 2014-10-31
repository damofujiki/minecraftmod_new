package com.hinasch.unlsaga.util.rangedattack;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;

public class CauseAddVelocity extends RangeDamageUnsaga{

	public CauseAddVelocity(World world, InvokeSkill parent) {
		super(world, parent);
		// TODO 自動生成されたコンストラクター・スタブ
	}



	//protected InvokeSkill helper;


	@Override
	public void hookOnMobHasAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
		boolean flag = false;
		if(helper==null)flag=true;
		if(flag && helper.getSkill()==Unsaga.abilityManager.grassHopper){
			if(mob.onGround){
				EntityLivingBase atacker = (EntityLivingBase) ds.getEntity();
				int j = 1;
				j += EnchantmentHelper.getKnockbackModifier(atacker, mob);
				mob.addVelocity((double)(-MathHelper.sin(atacker.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.5F), 0.9D, (double)(MathHelper.cos(atacker.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.5F));


				mob.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,120,1));
			}
		}

		if(flag && helper.getSkill()==Unsaga.abilityManager.earthDragon){
			if(mob.onGround){
				helper.attack(mob, null);
				helper.addPotionChance(45, mob, Potion.moveSlowdown.id, 90, 1);

				mob.setVelocity(0.0D, 0.0D,0.0D);
			}
		}
		double d0 = helper.owner.posX - mob.posX;
		double d1;

		for (d1 = helper.owner.posZ - mob.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
		{
			d0 = (Math.random() - Math.random()) ;
		}
		mob.knockBack(mob, 0, d0, d1);
		mob.setVelocity(mob.motionX*2,mob.motionY*2,mob.motionZ*2);

	}

}

