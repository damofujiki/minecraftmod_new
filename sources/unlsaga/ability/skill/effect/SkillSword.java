package com.hinasch.unlsaga.ability.skill.effect;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.Statics;
import com.hinasch.lib.UtilNBT;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingState;
import com.hinasch.unlsaga.item.weapon.ItemSwordUnsaga;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.util.rangedattack.CauseAddVelocity;
import com.hinasch.unlsaga.util.rangedattack.CauseKnockBack;

public class SkillSword extends SkillEffect{

	private static SkillSword INSTANCE;


	public final SkillBase gust = new SkillGust(SkillMelee.Type.STOPPED_USING).setRequireSneak(false);
	public final SkillBase kaleidoScope = new SkillKaleidoScope(SkillMelee.Type.ENTITY_LEFTCLICK);
	public final SkillBase chargeBlade = new SkillChargeBlade(SkillMelee.Type.RIGHTCLICK).setRequireSneak(false);
	public final SkillBase vandelize = new SkillVandelize(SkillMelee.Type.ENTITY_LEFTCLICK);
	public final SkillBase smash = new SkillSmash(SkillMelee.Type.STOPPED_USING).setRequireSneak(false);
	public final SkillBase roundabout = new SkillRoundabout(SkillMelee.Type.RIGHTCLICK).setRequireSneak(false);
	
	public class SkillRoundabout extends SkillMelee{

		public SkillRoundabout(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		@Override
		public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){

			if(!LivingDebuff.hasDebuff(ep, Unsaga.debuffManager.roundabout)){
				return super.canInvoke(world, ep, is, pos);
			}

			return false;
		}
		@Override
		public void invokeSkill(InvokeSkill helper) {
			helper.owner.playSound("mob.sheep.step", 0.5F, 1.8F / (helper.owner.getRNG().nextFloat() * 0.4F + 1.2F));
			PacketParticle pp = new PacketParticle(Statics.getParticleNumber(Statics.particleCrit),helper.owner.getEntityId(),5);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(helper.owner));
			LivingDebuff.addLivingDebuff(helper.owner, new LivingState(Unsaga.debuffManager.antiFallDamage,10,true));
			LivingDebuff.addLivingDebuff(helper.owner, new LivingState(Unsaga.debuffManager.roundabout,5,true));
			helper.owner.motionY += 0.8;

		}
		
	}
	public class SkillSmash extends SkillMelee{

		public SkillSmash(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public void invokeSkill(InvokeSkill helper) {
			helper.setFailed(true);
			World world = helper.world;
			EntityLivingBase ep = helper.owner;
			List<EntityLivingBase> entlist = world.getEntitiesWithinAABB(EntityLivingBase.class, ep.boundingBox.expand(2.0D, 1.0D, 2.0D));
			ep.swingItem();
			if(entlist!=null && !entlist.isEmpty()){
				for(EntityLivingBase damageentity:entlist){
					if(damageentity!=ep){
						helper.attack(damageentity, null,helper.charge);
						helper.damageWeapon();
					}
				}
			}
		}
	}
	public class SkillGust extends SkillMelee{

		public SkillGust(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){
			if(this.isGust(is)){
				return super.canInvoke(world, ep, is, pos);
			}
			return false;
		}
		
		public void setGust(ItemStack is,boolean par1){
			UtilNBT.setFreeTag(is, ItemSwordUnsaga.GUSTKEY, par1);
		}
		
		public boolean isGust(ItemStack is){
			return (UtilNBT.hasKey(is, ItemSwordUnsaga.GUSTKEY)? UtilNBT.readFreeTagBool(is, ItemSwordUnsaga.GUSTKEY) : false);
		}
		
		@Override
		public void invokeSkill(InvokeSkill helper) {
			this.setGust(helper.weapon, false);
			if(helper.charge>15.0F){
				helper.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (helper.owner.getRNG().nextFloat() * 0.4F + 1.2F));
				Vec3 lookvec = helper.owner.getLookVec();
				helper.owner.setVelocity(lookvec.xCoord*2.5, 0, lookvec.zCoord*2.5);
				LivingDebuff.addLivingDebuff(helper.owner, new LivingState(Unsaga.debuffManager.gust,100,false));
			}else{
				helper.setFailed(true);
			}

			return;
		}
		
	}
	public void doGust(InvokeSkill helper){
		helper.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (helper.owner.getRNG().nextFloat() * 0.4F + 1.2F));
		Vec3 lookvec = helper.owner.getLookVec();
		helper.owner.setVelocity(lookvec.xCoord*2.5, 0, lookvec.zCoord*2.5);
		LivingDebuff.addLivingDebuff(helper.owner, new LivingState(Unsaga.debuffManager.gust,100,false));
		return;
	}

	public class SkillVandelize extends SkillMelee{

		public SkillVandelize(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSkill(InvokeSkill parent) {
			EntityLivingBase entity = parent.target;
			parent.attack(parent.target, null);
			if(!parent.world.isRemote){
				if(HSLibs.isEntityLittleMaidAvatar(parent.owner)){
					parent.world.createExplosion(HSLibs.getLMMFromAvatar(parent.owner), entity.posX, entity.posY, entity.posZ, 1.5F,false);
					return;
				}
				parent.world.createExplosion(parent.owner, entity.posX, entity.posY, entity.posZ, 1.5F,false);

			}
		}
		
	}
	public void doVandelize(InvokeSkill parent){

		
		EntityLivingBase entity = parent.target;
		if(!parent.world.isRemote){
			if(HSLibs.isEntityLittleMaidAvatar(parent.owner)){
				parent.world.createExplosion(HSLibs.getLMMFromAvatar(parent.owner), entity.posX, entity.posY, entity.posZ, 1.5F,false);
				return;
			}
			parent.world.createExplosion(parent.owner, entity.posX, entity.posY, entity.posZ, 1.5F,false);

		}


	}

	public class SkillKaleidoScope extends SkillMelee{

		public SkillKaleidoScope(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSkill(InvokeSkill parent) {
			Random random = parent.owner.getRNG();
			parent.attack(parent.target, null);
			LivingDebuff.addLivingDebuff(parent.owner,new LivingState(Unsaga.debuffManager.antiFallDamage,10,true));
			LivingDebuff.addLivingDebuff(parent.owner, new LivingState(Unsaga.debuffManager.kalaidoscope,10,true));
			parent.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (parent.owner.getRNG().nextFloat() * 0.4F + 1.2F));
			double newposY = parent.target.posY + random.nextInt(4)+12;
			//ep.posY = newposY;
			parent.owner.motionY = 0.8;


			double disX = parent.target.posX-parent.owner.posX;
			double disZ =parent.target.posZ-parent.owner.posZ;
			double distance = Math.sqrt(Math.pow(disX, 2)+Math.pow(disZ,2));

			parent.owner.motionX = (disX*0.08);
			parent.owner.motionZ = (disZ*0.08);
		}
		
	}
	public void doKaleidoscope(InvokeSkill parent){
		//Unsaga.lpHandler.tryHurtLP(parent.target, parent.getAttackDamageLP());
		Random random = parent.owner.getRNG();
		LivingDebuff.addLivingDebuff(parent.owner,new LivingState(Unsaga.debuffManager.antiFallDamage,10,true));
		LivingDebuff.addLivingDebuff(parent.owner, new LivingState(Unsaga.debuffManager.kalaidoscope,10,true));
		parent.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (parent.owner.getRNG().nextFloat() * 0.4F + 1.2F));
		double newposY = parent.target.posY + random.nextInt(4)+12;
		//ep.posY = newposY;
		parent.owner.motionY = 0.8;


		double disX = parent.target.posX-parent.owner.posX;
		double disZ =parent.target.posZ-parent.owner.posZ;
		double distance = Math.sqrt(Math.pow(disX, 2)+Math.pow(disZ,2));

		parent.owner.motionX = (disX*0.08);
		parent.owner.motionZ = (disZ*0.08);


	}

	public class SkillChargeBlade extends SkillMelee{

		public SkillChargeBlade(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		
		@Override
		public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){
			if(ep.isSprinting()){
				return super.canInvoke(world, ep, is, pos);
			}
			return false;
		}
		@Override
		public void invokeSkill(InvokeSkill parent) {
			parent.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (parent.owner.getRNG().nextFloat() * 0.4F + 1.2F));
			CauseAddVelocity knock = new CauseAddVelocity(parent.world, parent);
			AxisAlignedBB bb = parent.owner.boundingBox.expand(2.0D, 1.0D, 2.0D);
			knock.causeDamage(parent.getDamageSource(), bb, parent.getModifiedAttackDamage(false, 0));
		}
		
	}
	public void doRearBlade(InvokeSkill parent){
		//parent.owner.hurtResistantTime = 5;
		parent.owner.playSound("mob.wither.shoot", 0.5F, 1.8F / (parent.owner.getRNG().nextFloat() * 0.4F + 1.2F));
		CauseKnockBack knock = new CauseKnockBack(parent.world, 1.5F);
		AxisAlignedBB bb = parent.owner.boundingBox.expand(2.0D, 1.0D, 2.0D);
		knock.causeDamage(parent.getDamageSource(), bb, parent.getModifiedAttackDamage(false, 0));
		//parent.causeRangeDamage(knock, parent.world,bb , parent.getAttackDamage(), DamageSource.causePlayerDamage(parent.owner), false);
		//LivingDebuff.addLivingDebuff(parent.owner, new LivingState(DebuffRegistry.rushBlade,1, false));

		//parent.ownerSkill.moveEntityWithHeading(parent.ownerSkill.moveStrafing,-parent.ownerSkill.moveForward);

		//ep.moveEntityWithHeading(1.0F	, 2.0F);
	}


	public static SkillSword getInstance(){
		if(INSTANCE==null){
			INSTANCE = new SkillSword();
		}
		return INSTANCE;
		
	}
}
