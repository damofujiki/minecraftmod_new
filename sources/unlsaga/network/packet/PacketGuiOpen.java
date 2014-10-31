package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.AbstractPacket;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class PacketGuiOpen extends AbstractPacket{

	protected byte guinum;
	protected XYZPos pos;
	protected int entityid;
	
	public PacketGuiOpen(){
		
	}
	
	public PacketGuiOpen(int numberGui){
	
		this.guinum = (byte)numberGui;
	}
	
	public PacketGuiOpen(int numberGui,XYZPos pos){
		this(numberGui);
		this.pos = pos;
	}
	
	public PacketGuiOpen(int numberGui,EntityVillager entity){
		this(numberGui);
		this.entityid = entity.getEntityId();
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(this.guinum);
		switch(this.guinum){
		case guiNumber.CHEST:
			buffer.writeInt(pos.x);
			buffer.writeInt(pos.y);
			buffer.writeInt(pos.z);
			break;
		case guiNumber.SMITH:
			buffer.writeInt(this.entityid);
			break;
		case guiNumber.BARTERING:
			buffer.writeInt(this.entityid);
			break;
		}

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.guinum = buffer.readByte();
		switch(this.guinum){
		case guiNumber.CHEST:
			this.pos = new XYZPos(0,0,0);
			this.pos.x = buffer.readInt();
			this.pos.y = buffer.readInt();
			this.pos.z = buffer.readInt();
			break;
		case guiNumber.BARTERING:
		case guiNumber.SMITH:
			this.entityid = buffer.readInt();
			break;
		}

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		EntityPlayerMP ep = (EntityPlayerMP)player;
		EntityVillager villager = null;
		switch(this.guinum){
		case guiNumber.EQUIP:

			FMLNetworkHandler.openGui((EntityPlayer) player, Unsaga.instance, Unsaga.guiNumber.EQUIP, ep.worldObj, (int)ep.posX, (int)ep.posY, (int)ep.posZ);
			break;
		case guiNumber.SMITH:
			 villager = (EntityVillager) ep.worldObj.getEntityByID(this.entityid);
			Unsaga.debug(villager);
			if(villager!=null){
				ExtendedPlayerData.getData(ep).setMerchant(villager);
				XYZPos pos = XYZPos.entityPosToXYZ(ep);
				FMLNetworkHandler.openGui((EntityPlayer) player, Unsaga.instance, Unsaga.guiNumber.SMITH, ep.worldObj,pos.x,pos.y,pos.z);
			}
			break;
		case guiNumber.BARTERING:
			villager = (EntityVillager) ep.worldObj.getEntityByID(this.entityid);
			if(villager!=null){
				ExtendedPlayerData.getData(ep).setMerchant(villager);
				XYZPos pos = XYZPos.entityPosToXYZ(ep);
				FMLNetworkHandler.openGui((EntityPlayer) player, Unsaga.instance, Unsaga.guiNumber.BARTERING, ep.worldObj,pos.x,pos.y,pos.z);
			}
			break;
		case guiNumber.BLENDER:
			FMLNetworkHandler.openGui((EntityPlayer) player, Unsaga.instance, Unsaga.guiNumber.BLENDER, ep.worldObj, (int)ep.posX, (int)ep.posY, (int)ep.posZ);
			break;
		case guiNumber.CHEST:
			FMLNetworkHandler.openGui((EntityPlayer) player, Unsaga.instance, Unsaga.guiNumber.CHEST, ep.worldObj, pos.x,pos.y,pos.z);
			break;
		case guiNumber.CARRIER:
			XYZPos p = XYZPos.entityPosToXYZ(ep);
			FMLNetworkHandler.openGui(player, Unsaga.instance, guiNumber.CARRIER, ep.worldObj, p.x, p.y, p.z);
			break;
		case guiNumber.SKILLPANEL:
			break;
		}

	}

}
