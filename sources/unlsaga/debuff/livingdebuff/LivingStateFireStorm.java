package com.hinasch.unlsaga.debuff.livingdebuff;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.util.FiveElements.Enums;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;
import com.hinasch.unlsagamagic.spell.Spells;

public class LivingStateFireStorm extends LivingState{

	protected XYZPos pos;
	protected final int basereach = 5;
	protected int reach;
	protected int amp;
	protected int entityid;
	protected boolean isCrimsonFlare;
	
	public LivingStateFireStorm(Debuff par1, int par2,int x,int y,int z,int amp,int entityid) {
		super(par1, par2, false);
		this.pos = new XYZPos(x,y,z);
		this.amp = amp;
		this.reach = this.basereach;
		this.entityid = entityid;
		if(this.entityid!=-1){
			this.isCrimsonFlare = true;
		}else{
			this.isCrimsonFlare = false;
		}
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void updateTick(EntityLivingBase living) {
		
		this.remain -= 1;

		if(this.isCrimsonFlare){
			this.doCrimsonFlare(living);
		}else{
			this.doFireStorm(living);
		}
		
	}
	
	public void doCrimsonFlare(EntityLivingBase invoker){
		Random rand = invoker.worldObj.rand;
		Entity target = invoker.worldObj.getEntityByID(entityid);
		if(target==null){
			this.remain = 0;
			return;
		}
		if(this.remain<=1){
			this.remain = 0;
			int explodeamount = amp;
			explodeamount = MathHelper.clamp_int(explodeamount, 1, 6);
			invoker.worldObj.createExplosion(invoker, pos.dx, pos.dy, pos.dz, 1.5F+amp, true);
			for(int i=0;i<explodeamount+1;i++){
				double dx = target.posX;
				double dy = target.posY;
				double dz = target.posZ;
				invoker.worldObj.createExplosion(invoker, dx, dy, dz, 1.5F+amp, true);
			}
			
		}

		if(this.remain % 4 == 0){
			int particlenum = 6 + invoker.getRNG().nextInt(2);
			
			int px = (int)( target.posX + rand.nextInt(3));
			int py = (int)( target.posY + rand.nextInt(3));
			int pz = (int)( target.posZ + rand.nextInt(3));
			
			PacketParticle pp = new PacketParticle(new XYZPos(px,py,pz),particlenum,10);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(target));
			//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getParticleToPosPacket(new XYZPos(px,py,pz), particlenum, 8));
		}
		if(this.remain % 6 == 0){
			//AxisAlignedBB bb = HSLibs.getBounding(pos, reach, reach);
			//List<EntityLivingBase> livings = living.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			//for(EntityLivingBase enemy:livings){

			AxisAlignedBB bb = target.boundingBox.expand(reach, reach, reach);
			List<Entity> livings = invoker.worldObj.getEntitiesWithinAABB(Entity.class, bb);
			for(Entity enemy:livings){
				if(enemy instanceof EntityLivingBase){
					this.doHurt(invoker, enemy);
				}
				if(enemy instanceof EntityDragonPart){
					this.doHurt(invoker, enemy);
				}
			}
					

					//Unsaga.debug(enemy);
				
			//}

			
		}
	}
	
	public void doFireStorm(EntityLivingBase invoker){
		
		reach = basereach * amp;

		int var1 = reach*2;
		if(var1<2){
			var1 = 2;
		}
		if(this.remain % 2 == 0){
			int px = this.pos.x - reach + invoker.getRNG().nextInt(var1);
			int py = this.pos.y - reach + invoker.getRNG().nextInt(var1);
			int pz = this.pos.z - reach + invoker.getRNG().nextInt(var1);
			if(invoker.worldObj.isAirBlock(px, py, pz)){
				invoker.worldObj.setBlock(px, py, pz, Blocks.fire);
			}
		}
		if(this.remain % 4 == 0){
			int particlenum = 6 + invoker.getRNG().nextInt(2);
			int px = this.pos.x - reach + invoker.getRNG().nextInt(var1);
			int py = this.pos.y - reach + invoker.getRNG().nextInt(var1);
			int pz = this.pos.z - reach + invoker.getRNG().nextInt(var1);
			PacketParticle pp = new PacketParticle(new XYZPos(px,py,pz), particlenum, 10);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(new XYZPos(px,py,pz),invoker.worldObj));
			//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getParticleToPosPacket(new XYZPos(px,py,pz), particlenum, 8));
		}
		if(this.remain % 6 == 0){
			AxisAlignedBB bb = HSLibs.getBounding(pos, reach, reach);
			List<EntityLivingBase> livings = invoker.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			for(EntityLivingBase enemy:livings){
				this.doHurt(invoker, enemy);
			}

			
		}
	}
	
	public String toString(){
		return this.buildSaveString(this.debuff.number,this.remain,this.pos.x,this.pos.y,this.pos.z,this.amp,this.entityid);
	}
	
	public void doHurt(EntityLivingBase attacker,Entity enemy){
		Spells spells = Unsaga.magic.spellManager;
		if(enemy!=attacker){
			float at = isCrimsonFlare ? spells.crimsonFlare.getStrHurtHP() * amp : spells.fireStorm.getStrHurtHP() * amp;
			float lphurt = isCrimsonFlare ? spells.crimsonFlare.getStrHurtLP() : spells.fireStorm.getStrHurtLP();
			if(enemy.isImmuneToFire()){
				at -= 2;
			}
			if(enemy instanceof EntityCreature)at += ((EntityCreature)enemy).getCreatureAttribute()==EnumCreatureAttribute.UNDEAD ? 2 : 0;
			if(enemy instanceof EntityMagmaCube)at = 1;
//			EntityDragonPart part = null;
//			if(enemy instanceof EntityDragon){
//				part = HSLibs.getEntityPartFromEntityDragon(attacker.worldObj, (EntityDragon) enemy);
//			}
			
//			if(part!=null){
//				part.attackEntityFrom(DamageSource.causeMobDamage(attacker), at);
//			}
			DamageSourceUnsaga ds = new DamageSourceUnsaga(null,attacker,0.4F,DamageHelper.Type.MAGIC);
			ds.setElement(Enums.FIRE);
			ds.setMagicDamage();
			ds.setFireDamage();
//			if(enemy instanceof EntityLivingBase){
//				if(!HSLibs.isSameTeam(attacker, (EntityLivingBase) enemy)){
//
//				}
//				
//			}
			enemy.attackEntityFrom(ds, at);
			enemy.setFire(10);
//			if(enemy instanceof EntityLivingBase){
//				Unsaga.lpHandler.tryHurtLP((EntityLivingBase) enemy, lphurt);
//			}

			if(enemy.isImmuneToFire()){
				
			}

			

			Unsaga.debug(enemy);
		}

	}
}
