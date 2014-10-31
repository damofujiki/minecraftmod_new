package com.hinasch.unlsagamagic.event;

import java.util.HashSet;
import java.util.Set;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Buff;
import com.hinasch.unlsaga.network.packet.PacketSound;
import com.hinasch.unlsaga.util.FiveElements.Enums;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventSpellBuff {

	public static Set<AbstractBuffShield> shieldSet = new HashSet();
	protected PacketSound ps;
	protected AbstractBuffShield shieldMissileGuard = new BuffShieldMissileGuard(Unsaga.debuffManager.missuileGuard,true,Enums.WOOD);
	protected AbstractBuffShield shieldLeaves = new BuffShieldLeavesShield(Unsaga.debuffManager.leavesShield,false,Enums.WOOD);
	protected AbstractBuffShield shieldWater = new BuffShieldWaterShield(Unsaga.debuffManager.waterShield,false,Enums.WATER);
	protected AbstractBuffShield shieldAegis = new BuffShieldAegis(Unsaga.debuffManager.aegisShield,false,Enums.EARTH);
	public class BuffShieldMissileGuard extends AbstractBuffShield{

		public BuffShieldMissileGuard(Buff parent, boolean isGuardAll,
				Enums element) {
			super(parent, isGuardAll, element);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean isEffective(LivingHurtEvent e) {
			if(e.source.getSourceOfDamage() instanceof EntityArrow){
				return true;
			}
			if(e.source instanceof DamageSourceUnsaga){
				DamageSourceUnsaga ds = (DamageSourceUnsaga)e.source;
				if(ds.getUnsagaDamageType()==DamageHelper.Type.SPEAR){
					return true;
				}
			}
			return false;
		}
		

		
	}
	public class BuffShieldAegis extends AbstractBuffShield{

		public BuffShieldAegis(Buff parent, boolean isGuardAll,
				Enums element) {
			super(parent, isGuardAll, element);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean isEffective(LivingHurtEvent e) {
			if(!e.source.isUnblockable()){
				return true;
			}
			return false;
		}
		
	}
	
	public class BuffShieldLeavesShield extends AbstractBuffShield{

		public BuffShieldLeavesShield(Buff parent, boolean isGuardAll,
				Enums element) {
			super(parent, isGuardAll, element);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean isEffective(LivingHurtEvent e) {
			if(e.source.getEntity() instanceof EntityLivingBase && !e.source.isMagicDamage() && !e.source.isUnblockable()){
				return true;
			}
			return false;
		}
		
	}
	
	public class BuffShieldWaterShield extends AbstractBuffShield{

		public BuffShieldWaterShield(Buff parent, boolean isGuardAll,
				Enums element) {
			super(parent, isGuardAll, element);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean isEffective(LivingHurtEvent e) {
			if(e.source.getEntity() instanceof EntityBlaze || e.source.getEntity() instanceof EntityMagmaCube){
				return true;
			}
			if(e.source.getEntity() instanceof EntityFireball){
				return true;
			}
			if(e.source.isFireDamage()){
				return true;
			}

			if(e.source instanceof DamageSourceUnsaga){
				DamageSourceUnsaga ds = (DamageSourceUnsaga)e.source;
				if(ds.getSubDamageType().contains(DamageHelper.SubType.FIRE)){
					return true;
				}
			}

			return false;
		}
		
	}

	@SubscribeEvent
	public void onPlayerHurtDebuff(LivingHurtEvent e){
		
		for(AbstractBuffShield shield:shieldSet){
			shield.doGuard(e);
		}

		
//		if(e.source.getSourceOfDamage() instanceof EntityArrow){
//			EntityLivingBase el = (EntityLivingBase)e.entityLiving;
//			if(LivingDebuff.hasDebuff(el, Debuffs.missuileGuard)){
//				e.ammount = 0;
//				ps = new PacketSound(1022,el.getEntityId(),PacketSound.MODE.AUX);
//
//				EntityPlayer ep = (EntityPlayer)e.source.getEntity();
//				TargetPoint tp = PacketUtil.getTargetPointNear(el);
//				Unsaga.packetPipeline.sendToAllAround(ps, tp);
//				//PacketDispatcher.sendPacketToPlayer(ps.getPacket(),(Player)ep);
//
//
//
//			}
//
//
//		}
//		if(e.entityLiving instanceof EntityLivingBase){
//			EntityLivingBase damagedLiving = (EntityLivingBase)e.entityLiving;
//			if(LivingDebuff.hasDebuff(damagedLiving, Debuffs.leavesShield)){
//				if(LivingDebuff.getLivingDebuff(damagedLiving, Debuffs.leavesShield).isPresent()){
//					LivingBuff buff = (LivingBuff)LivingDebuff.getLivingDebuff(damagedLiving, Debuffs.leavesShield).get();
//					if(damagedLiving.getRNG().nextInt(100)<buff.amp){
//						e.ammount = 0;
//						ps = new PacketSound(1022,damagedLiving.getEntityId(),PacketSound.MODE.AUX);
//
//						EntityPlayer ep = (EntityPlayer)e.source.getEntity();
//						TargetPoint tp = PacketUtil.getTargetPointNear(damagedLiving);
//						Unsaga.packetPipeline.sendToAllAround(ps, tp);
//						//PacketDispatcher.sendPacketToPlayer(ps.getPacket(),(Player)ep);
//
//					}
//
//				}
//
//
//
//			}
//
//
//		}

	}
}
