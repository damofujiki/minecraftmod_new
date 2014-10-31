package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.net.AbstractPacket;
import com.hinasch.unlsaga.client.gui.GuiBartering;
import com.hinasch.unlsaga.client.gui.GuiCarrier;

public class PacketSyncGui extends AbstractPacket{

	public static final int SYNC_BARTERINGGUI   = 100;
	public static final int SYNC_CARRIER = 2;
	
	public int sw;
	
	public int priceDown;
	public int priceUp;
	
	public int carrierid;
	
	public PacketSyncGui(){
		
		
	}
	
	public PacketSyncGui(int sw,int... args){
		this.sw = sw;
		if(this.sw==SYNC_BARTERINGGUI){
			this.priceDown = args[0];
			this.priceUp = args[1];
		}

		if(sw==SYNC_CARRIER){
			this.carrierid = args[0];
		}
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(sw);
		if(sw==SYNC_BARTERINGGUI){
			buffer.writeInt(priceDown);
			buffer.writeInt(priceUp);
		}

		if(sw==SYNC_CARRIER){
			buffer.writeInt(carrierid);
		}
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		sw = buffer.readInt();
		if(sw==SYNC_BARTERINGGUI){
			priceDown = buffer.readInt();
			priceUp = buffer.readInt();
		}
		if(sw==SYNC_CARRIER){
			carrierid = buffer.readInt();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		if(Minecraft.getMinecraft().currentScreen instanceof GuiBartering){
			GuiBartering gui = (GuiBartering) Minecraft.getMinecraft().currentScreen;
			gui.priceDown = priceDown;
			gui.priceUp = priceUp;
		}
		if(Minecraft.getMinecraft().currentScreen instanceof GuiCarrier){
			GuiCarrier gui = (GuiCarrier) Minecraft.getMinecraft().currentScreen;
			gui.syncCarrierID(carrierid);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
