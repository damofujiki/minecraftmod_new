package com.hinasch.unlsaga.ability.skill.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateHurricane;

public class SkillMartialArts extends SkillEffect{
	
	public static SkillMartialArts INSTANCE;
	
	public static SkillMartialArts getInstance(){
		if(INSTANCE==null){
			INSTANCE = new SkillMartialArts();
		}
		return INSTANCE;
	}
	
	public final SkillMelee airThrow = new SkillThrowing(SkillMelee.Type.RIGHTCLICK);
	public final SkillMelee hurricaneThrow = new SkillHuricane(SkillMelee.Type.RIGHTCLICK);
	public final SkillMelee callBack = new SkillCallBack(SkillMelee.Type.RIGHTCLICK);
	public static class SkillThrowing extends SkillMelee{

		public SkillThrowing(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public void invokeSkill(InvokeSkill parent) {

			EntityInteractEvent e = (EntityInteractEvent) parent.parent;
			EntityLivingBase enemy = (EntityLivingBase) e.target;
			EntityLivingBase attacker = parent.owner;
			World world = enemy.worldObj;
			float dis = enemy.getDistanceToEntity(attacker);
			Vec3 v1 = attacker.getLookVec();
			if(dis<1.8F){
				attacker.swingItem();
				enemy.addVelocity(v1.xCoord, 1.2D,v1.zCoord);
			}
		}
	}
	
	public static class SkillHuricane extends SkillMelee{

		public SkillHuricane(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public void invokeSkill(InvokeSkill parent) {

			EntityInteractEvent e = (EntityInteractEvent) parent.parent;
			EntityLivingBase enemy = (EntityLivingBase) e.target;
			EntityLivingBase attacker = parent.owner;
			World world = enemy.worldObj;
			float dis = enemy.getDistanceToEntity(attacker);
			Vec3 v1 = attacker.getLookVec();
			if(dis<1.8F){
				attacker.swingItem();
				enemy.addVelocity(v1.xCoord, 1.5D,v1.zCoord);
				LivingDebuff.addLivingDebuff(enemy, new LivingStateHurricane(Unsaga.debuffManager.hurricane, 7));
			}
		}
	}
	
	public static class SkillCallBack extends SkillMelee{

		public SkillCallBack(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public void invokeSkill(InvokeSkill parent) {

			EntityInteractEvent e = (EntityInteractEvent) parent.parent;
			EntityLivingBase enemy = (EntityLivingBase) e.target;
			EntityLivingBase attacker = parent.owner;
			World world = enemy.worldObj;
			float dis = enemy.getDistanceToEntity(attacker);
			Vec3 v1 = enemy.getLookVec();
			if(dis<2.0F){
				attacker.swingItem();
				enemy.addVelocity(v1.xCoord, 1.2D,v1.zCoord);
			}
		}
	}
}
