package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.XYZPos;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientThunder implements IMessage{

	protected XYZPos pos;
	public PacketClientThunder(){
		
	}
	

	public PacketClientThunder(XYZPos pos){
		this.pos = pos;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = XYZPos.readFromBuffer(buf);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		this.pos.writeToBuffer(buf);
		
	}
	
	public XYZPos getPos(){
		return this.pos;
	}

	
	public static class PacketClientThunderHandler implements IMessageHandler<PacketClientThunder,IMessage>{



		@Override
		public IMessage onMessage(PacketClientThunder mes,
				MessageContext ctx) {
			EntityPlayer clientPlalyer = Minecraft.getMinecraft().thePlayer;
			EntityLightningBolt bolt = new EntityLightningBolt(clientPlalyer.worldObj,mes.getPos().x,mes.getPos().y,mes.getPos().z);
			clientPlalyer.worldObj.spawnEntityInWorld(bolt);
			return null;
		}
		
	}
}
