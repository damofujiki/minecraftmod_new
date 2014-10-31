package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.net.AbstractPacket;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedEntityTag;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;

public class PacketSyncTag extends AbstractPacket{

	public int key;
	public int entID;
	
	public PacketSyncTag(){
		
	}
	
	public PacketSyncTag(int entID,int par1){
		this.key = par1;
		this.entID = entID;
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		buffer.writeByte(key);
		buffer.writeInt(entID);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		this.key = (int)buffer.readByte();
		this.entID = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Entity ent = player.worldObj.getEntityByID(entID);
		if(ent instanceof EntityVillager){
			Unsaga.debug("sync",this.getClass());
			switch(this.key){

			case 0:
					ExtendedEntityTag.addTagToEntity(ent, ContainerSmithUnsaga.SMITH_TYPE_REPAIR_PRO);
					ExtendedEntityTag.addTagToEntity(ent, "initialized");
					break;

			case 1:
					ExtendedEntityTag.addTagToEntity(ent, ContainerSmithUnsaga.SMITH_TYPE_ADD_ABILTY);
					ExtendedEntityTag.addTagToEntity(ent, "initialized");
					break;
			}
		}
		
	}

}
