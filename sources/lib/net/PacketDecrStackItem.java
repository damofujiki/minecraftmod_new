package com.hinasch.lib.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDecrStackItem extends AbstractPacket{

	public PacketDecrStackItem(){
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		System.out.println("packetdecrstack:client");
		if(player.getHeldItem()!=null){
			player.getHeldItem().stackSize --;
			if(player.getHeldItem().stackSize<0){
				player.getHeldItem().stackSize = 0;
			}
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		System.out.println("packetdecrstack:server");
		if(player.getHeldItem()!=null){
			player.getHeldItem().stackSize --;
			if(player.getHeldItem().stackSize<0){
				player.getHeldItem().stackSize = 0;
			}
		}
		
	}
	
	
}
