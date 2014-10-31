package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.ClientHelper;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncDebuff implements IMessage{

	protected int number;
	protected int entityid;
	protected int remain;
	protected String dataStr;
	protected int mode;
	
	public static final int Sync_END = 1;
	public static final int Sync_START  = 2;
	public PacketSyncDebuff(){
		
	}
	
	public PacketSyncDebuff(int entityid,int number){
		this.number = number;
		this.entityid = entityid;
		this.mode = Sync_END;
	}
	
	public PacketSyncDebuff(int entityid,int number,int remain){
		this.number = number;
		this.entityid = entityid;
		this.remain = remain;
		this.mode = Sync_START;
		
	}
	
	public int getMode(){
		return this.mode;
	}
	
	public int getEntityID(){
		return this.entityid;
	}
	
	public int getRemainingTime(){
		return this.remain;
	}


	public int getDebuffNumber(){
		return this.number;
	}


	@Override
	public void fromBytes(ByteBuf buffer) {
		this.mode = buffer.readInt();
		this.entityid = buffer.readInt();
		this.number = buffer.readInt();
		if(this.mode==Sync_START){
			this.remain = buffer.readInt();
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(mode);
		buf.writeInt(entityid);
		buf.writeInt(number);
		switch(this.mode){
		case Sync_END:
			break;
		case Sync_START:
			buf.writeInt(remain); //クライアント側はremainさえ合えばいいので
			break;
		}
		
	}

	public static class PacketSyncDebuffHandler implements IMessageHandler<PacketSyncDebuff,IMessage>{

		@Override
		public IMessage onMessage(PacketSyncDebuff message, MessageContext ctx) {
			EntityPlayer player;
			if(ctx.side.isClient()){
				player = ClientHelper.getClientPlayer();
				Unsaga.debug("kitemasu",this.getClass());
				switch(message.getMode()){
				case Sync_END:
					if(player.worldObj.getEntityByID(message.getEntityID())==null){
						return null;
					}
					EntityLivingBase living = (EntityLivingBase) player.worldObj.getEntityByID(message.getEntityID());
					if(LivingDebuff.hasDebuff(living, Unsaga.debuffManager.getDebuff(message.getDebuffNumber()))){
						Debuff debuff = Unsaga.debuffManager.getDebuff(message.getDebuffNumber());
						//EntityLivingBase entity = (EntityLivingBase) player.worldObj.getEntityByID(entityid);
						if(LivingDebuff.hasDebuff(living, debuff)){
							LivingDebuff.removeDebuff(living, debuff);
						}
					}
					break;
				case Sync_START:
					Unsaga.debug(player.worldObj.getEntityByID(message.getEntityID()),this.getClass());
					if(player.worldObj.getEntityByID(message.getEntityID()) instanceof EntityLivingBase){
						return null;
					}
					EntityLivingBase linving = (EntityLivingBase) player.worldObj.getEntityByID(message.getEntityID());
					if(!LivingDebuff.hasDebuff(linving, Unsaga.debuffManager.getDebuff(message.getDebuffNumber()))){
						Unsaga.debug("add",this.getClass());
						LivingDebuff.addDebuff(linving, Unsaga.debuffManager.getDebuff(message.getDebuffNumber()), message.getRemainingTime());
					}else{
						LivingDebuff ld = LivingDebuff.getLivingDebuff(linving, Unsaga.debuffManager.getDebuff(message.getDebuffNumber())).get();
						ld.syncRemain(message.getRemainingTime());
					}
					break;
				}
				return null;
			}else{
				player = ctx.getServerHandler().playerEntity;
				if(message.getMode()==Sync_END){
					if(Unsaga.debuffManager.getDebuff(message.getDebuffNumber())!=null && player.worldObj.getEntityByID(message.getEntityID())!=null){
						Debuff debuff = Unsaga.debuffManager.getDebuff(message.getDebuffNumber());
						EntityLivingBase entity = (EntityLivingBase) player.worldObj.getEntityByID(message.getDebuffNumber());
						if(LivingDebuff.hasDebuff(entity, debuff)){
							LivingDebuff.removeDebuff(entity, debuff);
						}
					}
				}
			}
			return null;
		}
		
	}
}
