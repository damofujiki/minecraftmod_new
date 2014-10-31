package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.hinasch.lib.ClientHelper;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedDataLP;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketLPNew implements IMessage{


	private int lp;
	private int entityid;
	private boolean isRenderDamagePacket;
	public PacketLPNew(){
		this.isRenderDamagePacket = false;

	}

	public PacketLPNew(int entityid,int lp){
		this.entityid = entityid;
		this.lp = lp;
		this.isRenderDamagePacket = false;
	}

	public static PacketLPNew getPacketRenderDamagedLP(int entityid,int lpdamage){
		PacketLPNew psl = new PacketLPNew(entityid,lpdamage);
		psl.isRenderDamagePacket = true;
		return psl;
	}

	public static PacketLPNew getSyncPacket(int entityid,int lp){
		return new PacketLPNew(entityid,lp);

	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityid = buf.readInt();
		this.lp = buf.readInt();

		this.isRenderDamagePacket = buf.readBoolean();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityid);
		buf.writeInt(lp);
		buf.writeBoolean(isRenderDamagePacket);


	}

	public int getEntityID(){
		return this.entityid;
	}

	public int getLP(){
		return this.lp;
	}
	public static class PacketLPHandler implements IMessageHandler<PacketLPNew,IMessage>{

		@Override
		public IMessage onMessage(PacketLPNew message, MessageContext ctx) {
			EntityPlayer player = null;
			if(ctx.side.isClient()){

				if(!Unsaga.lpManager.isLPEnabled())return null;
				player = ClientHelper.getClientPlayer();
				World world = player.worldObj;
				Entity entity = world.getEntityByID(message.getEntityID());
				if(entity!=null){
					if(message.isRenderDamagePacket){
						Unsaga.debug("レンダー準備",this.getClass());
						EntityLivingBase living = (EntityLivingBase) entity;
						ExtendedDataLP data = Unsaga.lpManager.getData(living);
						data.prepareRenderDamage(message.getLP());
						data.markDirty(true);
						return null;
					}
					Unsaga.debug("sync",this.getClass());
					if(entity instanceof EntityLivingBase){
						EntityLivingBase living = (EntityLivingBase) entity;
						ExtendedDataLP data = Unsaga.lpManager.getData(living);
						data.setLP(message.getLP());
					}
				}
			}else{
				player = ctx.getServerHandler().playerEntity;
				World world = player.worldObj;
				Entity entity = world.getEntityByID(message.getEntityID());
				if(entity instanceof EntityLivingBase){
					EntityLivingBase living = (EntityLivingBase) entity;
					ExtendedDataLP data = Unsaga.lpManager.getData(living);
					data.setLP(message.getLP());
				}
			}
			return null;
		}

	}
}
