package com.hinasch.unlsaga.network.packet;

import com.hinasch.lib.Statics;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.AbstractPacket;
import com.hinasch.lib.net.PacketUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSound extends AbstractPacket{

	protected int mode;
	protected int id;
	protected int soundnumber;
	protected int entityid;
	protected XYZPos pos;
	protected static final int PLAY_AUX_FROM_PLAYER = 0x01;
	protected static final int PLAY_SOUND_AT_ENTITY = 0x02;
	protected static final int PLAY_AUX_AT_ENTITY = 0x03;
	protected static final int PLAY_SOUND_AT_POS = 0x04;
	
	public static class MODE{
		public static final int AUX = 0x01;
		public static final int SOUND = 0x02;
	}
	
	
	public PacketSound(){
		
	}
	
	public PacketSound(int soundNumber){
		this.soundnumber = soundNumber;
		this.id = PLAY_AUX_FROM_PLAYER;
	}
	public PacketSound(int soundNumber,int entityid,int mode){
		this.soundnumber = soundNumber;
		this.entityid = entityid;
		this.mode = mode;
		switch(mode){
		case MODE.AUX:
			this.id = PLAY_AUX_AT_ENTITY;
			break;
		case MODE.SOUND:
			this.id = PLAY_SOUND_AT_ENTITY;
			break;
		}
			
			
	}
	
	public PacketSound(int soundNumber,XYZPos pos){
		this.soundnumber = soundNumber;
		this.pos = pos;
		this.id = PLAY_SOUND_AT_POS;
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(soundnumber);
		if(id==PLAY_AUX_FROM_PLAYER){
			
		}
		if(id==PLAY_SOUND_AT_ENTITY || id==PLAY_AUX_AT_ENTITY){
			buffer.writeInt(this.entityid);
		}
		if(id==PLAY_SOUND_AT_POS){
			PacketUtil.XYZPosToPacket(buffer, pos);
		}
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.id = buffer.readInt();
		this.soundnumber = buffer.readInt();
		if(id==PLAY_AUX_FROM_PLAYER){
			
		}
		if(id==PLAY_SOUND_AT_ENTITY || id==PLAY_AUX_AT_ENTITY){
			this.entityid = buffer.readInt();
		}
		if(id==PLAY_SOUND_AT_POS){
			this.pos = PacketUtil.bufferToXYZPos(buffer);
		}
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		if(id==PLAY_AUX_FROM_PLAYER){
			if(player instanceof EntityClientPlayerMP){
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				XYZPos po = XYZPos.entityPosToXYZ(ep);
				ep.worldObj.playAuxSFX(soundnumber, po.x, po.y, po.z, 0);
			}
		}
		if(id==PLAY_SOUND_AT_ENTITY){
			if(player instanceof EntityClientPlayerMP){
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				Entity entity = ep.worldObj.getEntityByID(entityid);
				if(entity!=null){
					ep.worldObj.playSoundAtEntity(entity, Statics.soundMap.get(soundnumber), 1.0F, 1.0F / (ep.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);

				}
				
			}

		}
		if(id==PLAY_AUX_AT_ENTITY){
			if(player instanceof EntityClientPlayerMP){
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				Entity entity = ep.worldObj.getEntityByID(entityid);
				if(entity!=null){
					XYZPos po = XYZPos.entityPosToXYZ(entity);
					ep.worldObj.playAuxSFX(soundnumber, po.x, po.y, po.z, 0);
				}

			}
		}
		if(id==PLAY_SOUND_AT_POS){
			if(player instanceof EntityClientPlayerMP){
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				ep.worldObj.playSound((double)pos.x, (double)pos.y, (double)pos.z, Statics.soundMap.get(soundnumber), 1.0F, 1.0F / (ep.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F,false);


			}
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
