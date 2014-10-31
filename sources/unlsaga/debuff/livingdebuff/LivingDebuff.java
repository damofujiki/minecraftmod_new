package com.hinasch.unlsaga.debuff.livingdebuff;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.event.extendeddata.ExtendedLivingData;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.network.packet.PacketSyncDebuff;
import com.hinasch.unlsaga.util.ChatUtil;



public class LivingDebuff {

	protected Debuff debuff;
	protected int remain;

	//これを継承してステートにもできる。initとtostringを上書きする。

	public LivingDebuff(Debuff par1,int par2){
		this.debuff = par1;
		this.remain = par2;
		
		
	}

	public Debuff getDebuff(){
		return this.debuff;
	}

	public int getRemaining(){
		return this.remain;
	}

	//打ち消すアビリティを持ってれば消える
	public void checkAbilitiesAgainst(EntityLivingBase living){
		for(Ability ab:this.debuff.getAbilityAgainst()){
			if(HelperAbility.getAmountAbility(living, ab)>0){
				this.remain = 0;
			}
		}
	}

	//Tickごとに
	public void updateTick(EntityLivingBase living) {
		if(!this.debuff.getAbilityAgainst().isEmpty()){
			this.checkAbilitiesAgainst(living);
		}


	}

	//１秒ごとに
	public void updateRemain(EntityLivingBase living){
		this.remain -= 1;
		if(this.remain<=0){
			if(this.debuff==Unsaga.debuffManager.cooling && !living.worldObj.isRemote){
				if(living instanceof EntityPlayer){
					ChatUtil.addMessage(living, living.getCommandSenderName()+" has finished cooling down.");
				}
			}
			this.remain = 0;
		}

		if(living instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)living;
			if(this.debuff==Unsaga.debuffManager.downSkill && HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.skillGuard)>0){
				this.remain = 0;
			}
			if(this.debuff==Unsaga.debuffManager.sleep && HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.antiSleep)>0){
				this.remain = 0;
			}
			if(this.debuff.getClass() == Debuff.class && HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.antiDebuff)>0){
				this.remain = 0;
			}
			if(this.debuff==Unsaga.debuffManager.downPhysical && HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.bodyGuard)>0){
				this.remain = 0;
			}
			if(this.debuff==Unsaga.debuffManager.downMagic && HelperAbility.getAmountAbility(ep, Unsaga.abilityManager.magicGuard)>0){
				this.remain = 0;
			}

		}

		if(this.debuff.getParticleNumber()!=-1){
			if(living.getRNG().nextInt(4)<=1){
				PacketParticle pp = new PacketParticle(debuff.getParticleNumber(),living.getEntityId(),3);
				Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
			}
		}
		//		if(this.debuff==DebuffRegistry.earthVeil){
		//			if(living.getRNG().nextInt(4)<=1){
		//				PacketParticle pp = new PacketParticle(200,living.getEntityId(),3);
		//				Unsaga.packetPipeline.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
		//			}
		//		}
		//		if(this.debuff==DebuffRegistry.woodVeil){
		//			if(living.getRNG().nextInt(4)<=1){
		//				PacketParticle pp = new PacketParticle(201,living.getEntityId(),3);
		//				Unsaga.packetPipeline.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
		//			}
		//		}
		//		if(this.debuff==DebuffRegistry.fireVeil){
		//			if(living.getRNG().nextInt(4)<=1){
		//				PacketParticle pp = new PacketParticle(StaticWords.getParticleNumber(StaticWords.particleFlame),living.getEntityId(),3);
		//				Unsaga.packetPipeline.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
		//			}
		//		}
		//		if(this.debuff.getClass()== Debuff.class || this.debuff.getClass()==Buff.class){
		//			if(living.getRNG().nextInt(4)<=1){
		//				PacketParticle pp = new PacketParticle(4,living.getEntityId(),3);
		//				Unsaga.packetPipeline.sendToAllAround(pp, PacketUtil.getTargetPointNear(living));
		//				//PacketDispatcher.sendPacketToAllPlayers(pp.getPacket());
		////                double d0 = (double)(7 >> 16 & 255) / 255.0D;
		////                double d1 = (double)(7 >> 8 & 255) / 255.0D;
		////                double d2 = (double)(7 >> 0 & 255) / 255.0D;
		////				 living.worldObj.spawnParticle("mobSpell", living.posX + (living.getRNG().nextDouble() - 0.5D) * (double)living.width, living.posY + living.getRNG().nextDouble() * (double)living.height - (double)living.yOffset, living.posZ + (living.getRNG().nextDouble() - 0.5D) * (double)living.width, 0, 0, 0);
		//
		//			}
		//        }






		Unsaga.debug(this.debuff.name+":"+this.toString());
	}

	public boolean isExpired(){

		if(this.remain<=0){
			Unsaga.debug(this.debuff.name+" is expired.");

			return true;
		}

		return false;
	}

	public String toString(){
		return this.debuff.number+":"+this.remain;
	}


	public static LivingDebuff buildFromString(String data){
		String[] strs = data.split(":");
		Debuff debuff = Unsaga.debuffManager.getDebuff(Integer.valueOf(strs[0]));
		LivingDebuff output = debuff.init(strs);
		Unsaga.debug(output.debuff.name+"復元");
		return output;

	}


	public static void addDebuff(EntityLivingBase living,Debuff debuff,int remain){
		ExtendedLivingData.addDebuff(living, debuff, remain);
	}
	public static void addLivingDebuff(EntityLivingBase living,LivingDebuff livdebuff){
		ExtendedLivingData.addLivingDebuff(living, livdebuff);
	}

	public static void removeDebuff(EntityLivingBase living,Debuff debuff){
		ExtendedLivingData.removeDebuff(living, debuff);
	}

	public static boolean hasDebuff(EntityLivingBase living,Debuff debuff){
		return ExtendedLivingData.hasDebuff(living, debuff);
	}

	public static Optional<LivingDebuff> getLivingDebuff(EntityLivingBase living,Debuff debuff){
		return ExtendedLivingData.getDebuff(living, debuff);
	}

	public static int getModifierAttackBuff(EntityLivingBase living){
		int amount = 0;
		if(LivingDebuff.getLivingDebuff(living, Unsaga.debuffManager.powerup).isPresent()){
			LivingBuff buff = (LivingBuff)LivingDebuff.getLivingDebuff(living, Unsaga.debuffManager.powerup).get();
			amount += buff.amp;
		}
		return amount;
	}

	public static boolean isCooling(EntityLivingBase living){
		return hasDebuff(living,Unsaga.debuffManager.cooling);
	}

	protected String buildSaveString(Object... strs){
		List<Object> list = Lists.newArrayList(strs);
		StringBuilder saveString = new StringBuilder();
		for(Iterator<Object> ite=list.iterator();ite.hasNext();){
			String str = ite.next().toString();
			saveString.append(str);
			if(ite.hasNext()){
				saveString.append(":");
			}
		}

		return new String(saveString);

	}

	//TODO : このへん要改変
	public void onExpiredEvent(EntityLivingBase living) {
		if(this.debuff.getAttributeModifier()!=null){
			IAttributeInstance entityAttribute = living.getEntityAttribute(this.debuff.getAttributeType());
			if(entityAttribute!=null){
				if(entityAttribute.getModifier(this.debuff.getAttributeModifier().getID())!=null){
					entityAttribute.removeModifier(this.debuff.getAttributeModifier());
					Unsaga.debug("おわりしました："+living.getEntityAttribute(this.debuff.getAttributeType()).getAttributeValue());	
				}
			}
			//			living.getEntityAttribute(this.debuff.getAttributeType()).removeModifier(this.debuff.getAttributeModifier());
			//			Unsaga.debug("おわりしました："+living.getEntityAttribute(this.debuff.getAttributeType()).getAttributeValue());

		}
		//TODO:デバフの終了を通知
		if(living instanceof EntityPlayer){
			
		}
		if(!living.worldObj.isRemote){
			PacketSyncDebuff psd = new PacketSyncDebuff(living.getEntityId(),this.debuff.number);
			Unsaga.packetDispatcher.sendToAll(psd);
		}
		//		if(living.worldObj.isRemote){
		//			PacketSyncDebuff psd = new PacketSyncDebuff(living.getEntityId(),this.debuff.number);
		//			Unsaga.packetPipeline.sendToServer(psd);
		//		}EntityPlayer


	}

	public void onInitEvent(EntityLivingBase living) {
		if(!living.worldObj.isRemote){
			PacketSyncDebuff psd = new PacketSyncDebuff(living.getEntityId(),this.debuff.number,this.remain);
			Unsaga.packetDispatcher.sendToAll(psd);
		}

		if(this.debuff.getAttributeModifier()!=null){
			IAttributeInstance entityAttribute = living.getEntityAttribute(this.debuff.getAttributeType());
			if(entityAttribute!=null){
				if(entityAttribute.getModifier(this.debuff.getAttributeModifier().getID())==null){
					entityAttribute.applyModifier(this.debuff.getAttributeModifier());
					Unsaga.debug(this.debuff.getAttributeModifier().getName()+"アプライしました："+living.getEntityAttribute(this.debuff.getAttributeType()).getAttributeValue());
				}
			}
		}
		//		if(this.debuff.getAttributeModifier()!=null && living.getEntityAttribute(this.debuff.getAttributeType()).getModifier(this.debuff.getAttributeModifier().getID())==null){
		//			living.getEntityAttribute(this.debuff.getAttributeType()).applyModifier(this.debuff.getAttributeModifier());
		//			Unsaga.debug(this.debuff.getAttributeModifier().getName()+"アプライしました："+living.getEntityAttribute(this.debuff.getAttributeType()).getAttributeValue());
		//		}
	}

	public void syncRemain(int remain){
		this.remain = remain;
	}

}
