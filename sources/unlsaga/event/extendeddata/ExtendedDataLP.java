package com.hinasch.unlsaga.event.extendeddata;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.network.packet.PacketLPNew;
import com.hinasch.unlsaga.util.ChatUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class ExtendedDataLP implements IExtendedEntityProperties{

	public int hurtInterval;
	protected boolean lpInitialized;
	protected int lifePoint;
	protected boolean marked;
	public int damage;
	public int renderTick;
	public XYZPos renderTextPos;
	protected static final String KEY = "unsaga.lifePoint";
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("LP", lifePoint);
		compound.setBoolean("init", this.lpInitialized);
	
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.lifePoint = compound.getInteger("LP");
		this.lpInitialized = compound.getBoolean("init");
	}

	public void markDirty(boolean par1){
		this.renderTick = 10;
		this.marked = par1;
	}
	
	public void prepareRenderDamage(int damage){
		this.damage = damage;
	}
	public boolean isDirty(){
		return this.marked;
	}
	public void decrLP(int decr){
		this.lifePoint -= decr;
		if(this.lifePoint<0){
			this.lifePoint = 0;
		}
	}
	
	public int getLP(){
		return this.lifePoint;
	}
	
	public void setLP(int lp){
		this.lifePoint = lp;
	}
	
	public void initLP(EntityLivingBase living){
		this.lifePoint = Unsaga.lpManager.getMaxLP(living);
	}
	@Override
	public void init(Entity entity, World world) {


		this.hurtInterval = 0;
		this.lifePoint = 3;
		this.marked = false;
		this.renderTick = 0;
		this.lpInitialized = false;
		
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e){
		ExtendedDataLP data = ExtendedDataLP.getExtended(e.entityLiving);
		if(data.hurtInterval>0){
			if(e.entityLiving.ticksExisted % 20 * 12 == 0){
				data.hurtInterval -= 1;
			}
		}else{
			data.hurtInterval = 0;
		}
	}
	
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e){
		if(e.entity instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) e.entity;
			//Unsaga.debug(living.getMaxHealth(),this.getClass());
			ExtendedDataLP data = ExtendedDataLP.getExtended(living);
			if(!data.lpInitialized){
				int var1 = (int)(living.getMaxHealth() / 3.0F);
				data.setLP(var1);
				data.lpInitialized = true;
			}

			if(living instanceof EntityPlayer){
				if(!living.worldObj.isRemote){
					Unsaga.debug("同期します",this.getClass());
					PacketLPNew psl = new PacketLPNew(living.getEntityId(),data.getLP());
					Unsaga.packetDispatcher.sendTo(psl, (EntityPlayerMP)living);
				}
			}
		}
	}
	@SubscribeEvent
	public void attachDataEvent(EntityConstructing e){
	
		if(e.entity instanceof EntityLivingBase){
			if(e.entity.getExtendedProperties(KEY)==null){
				e.entity.registerExtendedProperties(KEY, getExtended((EntityLivingBase) e.entity));
			}
		}


		
//		if(e.entity instanceof EntityLivingBase){
//			this.lifePoint = 3;
//			EntityLivingBase living = (EntityLivingBase)e.entity;
//			this.lifePoint = (int)(living.getMaxHealth() / 3.0F);
//			Unsaga.debug(this.lifePoint,e.entity,this.getClass().getName());
//			if(e.entity instanceof EntityPlayer){
//				if(!e.entity.worldObj.isRemote){
//					Unsaga.debug("同期します",this.getClass());
//					PacketSyncLP psl = new PacketSyncLP(e.entity.getEntityId(),this.lifePoint);
//					Unsaga.packetPipeline.sendTo(psl, (EntityPlayerMP) e.entity);
//				}
//
//			}
//		}
	}
	
	protected static ExtendedDataLP getExtended(EntityLivingBase entity){
		if(entity!=null){
			if(entity.getExtendedProperties(KEY)!=null){
				return (ExtendedDataLP) entity.getExtendedProperties(KEY);
			}
			return new ExtendedDataLP();
		}

		return null;
	}
	

	


	




	
	
	public static class LPManager{
		public ExtendedDataLP getData(EntityLivingBase entity){
			return ExtendedDataLP.getExtended(entity);
		}
		
		
		public int getMaxLP(EntityLivingBase living){
			if(living instanceof EntityCreeper){
				return 3;
			}
			int var1 = (int)(living.getMaxHealth() / 3.0F);
			if(var1<0){
				var1= 0;
			}
			return var1;
		}
		
		public void sendRenderHurtLPPacket(int lpdamage,EntityLivingBase entityHurt,EntityPlayerMP renderTo){
			if(!isLPEnabled())return;
			PacketLPNew psl = PacketLPNew.getPacketRenderDamagedLP(entityHurt.getEntityId(), lpdamage);
			Unsaga.packetDispatcher.sendTo(psl, (EntityPlayerMP) renderTo);
		}
		
		public void sendSyncLPPacket(int lp,EntityLivingBase entitySync,EntityPlayerMP playerSync){
			if(!isLPEnabled())return;
			PacketLPNew psl = PacketLPNew.getSyncPacket(entitySync.getEntityId(),lp);
			Unsaga.packetDispatcher.sendTo(psl, (EntityPlayerMP) playerSync);
		}
		
		public void broadcastRenderHurtLPPacket(int lp,EntityLivingBase syncEntity,TargetPoint target){
			if(!isLPEnabled())return;
			PacketLPNew psl = PacketLPNew.getPacketRenderDamagedLP(syncEntity.getEntityId(), lp);
			Unsaga.packetDispatcher.sendToAllAround(psl, target);
		}
		
		public boolean isLPEnabled(){
			//Unsaga.logger.log(this.getClass().getName(),Unsaga.configs.enableLP);
			return Unsaga.configs.enableLP;
		}
		public void processLPHurt(Entity attacker,EntityLivingBase entityHurt,float lpdamage){
			if(!isLPEnabled())return;
			
			if(lpdamage>=1.0F){
				ExtendedDataLP.getExtended(entityHurt).decrLP((int)lpdamage);
				ExtendedDataLP.getExtended(entityHurt).hurtInterval = 5;
				Unsaga.debug(entityHurt,ExtendedDataLP.getExtended(entityHurt).getLP());
				if(attacker instanceof EntityPlayer && !attacker.worldObj.isRemote){
					sendRenderHurtLPPacket((int)lpdamage,entityHurt,(EntityPlayerMP) attacker);
					ChatUtil.addMessage((EntityPlayer) attacker, entityHurt.getCommandSenderName()+" hurt "+(int)lpdamage+"LP!");
				}
				if(entityHurt instanceof EntityPlayer){
					sendSyncLPPacket((int) ExtendedDataLP.getExtended(entityHurt).getLP(),entityHurt,(EntityPlayerMP) entityHurt);
					ChatUtil.addMessage((EntityPlayer) entityHurt, entityHurt.getCommandSenderName()+" hurt "+(int)lpdamage+"LP!");
				}

				if(HSLibs.isSameTeam(Minecraft.getMinecraft().thePlayer, entityHurt)){
					String ownername = getOwnerName(entityHurt);
					World world = entityHurt.worldObj;
					if(ownername!=null && world.getPlayerEntityByName(ownername)!=null){
						EntityPlayer owner = world.getPlayerEntityByName(ownername);
						sendSyncLPPacket((int)ExtendedDataLP.getExtended(entityHurt).getLP(),entityHurt,(EntityPlayerMP) owner);
					}
				}

				if(!(entityHurt instanceof EntityPlayer) && !(attacker instanceof EntityPlayer)){
					broadcastRenderHurtLPPacket((int)lpdamage,entityHurt,PacketUtil.getTargetPointNear(entityHurt));
				}
			}
		}
		
		public float getLPHurtAmount(EntityLivingBase damaged,float strLP,Random rand,boolean isPoisonDamage){
			float var1 = damaged.getHealth() / damaged.getMaxHealth();
			int offset = 0;
			if(HelperAbility.getAmountAbility(damaged, Unsaga.abilityManager.lifeGuard)>0){
				offset += 10 * HelperAbility.getAmountAbility(damaged, Unsaga.abilityManager.lifeGuard);
			}
			var1 = 1.0F - var1;

			if(var1>0.7F){
				offset = 50;
			}
			if(var1>0.8F){
				offset = 70;
			}
			if(var1>0.9F){
				offset = 90;
			}
			if(isPoisonDamage){
				offset = 20;
			}
			int var2 = (int)((var1+strLP)* 100F);
			if(var2<2){
				var2 = 2;
			}

			int rnd = rand.nextInt(var2) + offset;

			Unsaga.debug(rnd);
			if(rnd<70){
				return 0.0F;
			}
			if(rnd<100){
				return 1.0F;
			}
			float f2 = rnd * 0.01F;
			return f2;
		}
		
		public String getOwnerName(EntityLivingBase living){
			if(living instanceof EntityTameable){
				return ((EntityTameable) living).getOwner().getCommandSenderName();
			}
			if(living instanceof EntityHorse){
				//もうちょっと細かく
				UUID uuid = UUID.fromString( ((EntityHorse) living).func_152119_ch());
				return uuid == null?null : living.worldObj.func_152378_a(uuid).getCommandSenderName();
			}
			return null;
		}
		
		public void lpEventOnLivingUpdate(LivingUpdateEvent e) {
			if(!isLPEnabled())return;
			
			if(!this.getData(e.entityLiving).lpInitialized){
				ExtendedDataLP data = getData(e.entityLiving);
				data.setLP(getMaxLP(e.entityLiving));
				data.lpInitialized = true;
			}
			
			if(e.entityLiving instanceof EntityPlayer){
				EntityPlayer ep = (EntityPlayer)e.entityLiving;
				if(ep.getSleepTimer() == 99 && !ep.worldObj.isRemote){
					ExtendedDataLP epdata = ExtendedDataLP.getExtended(ep);
					epdata.initLP(ep);

					ChatUtil.addMessage(ep, ep.getCommandSenderName()+"''s LP has Restored.");
					//if(!ep.worldObj.isRemote){
					this.sendSyncLPPacket(epdata.getLP(), ep, (EntityPlayerMP) ep);

					
					List<EntityLivingBase> livings = ep.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, ep.boundingBox.expand(40.0D, 40.0D, 40.0D));
					for(EntityLivingBase living:livings){
						if(living!=ep && HSLibs.isSameTeam(ep, living)){
							ExtendedDataLP data = ExtendedDataLP.getExtended(living);
							data.initLP(living);
							ChatUtil.addMessage(ep, living.getCommandSenderName()+"''s LP has Restored.");
						}
						
					}
				}
			}
			
		}
	}
}
