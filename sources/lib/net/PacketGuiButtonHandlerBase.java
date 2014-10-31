package com.hinasch.lib.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

import com.hinasch.lib.base.ContainerBase;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketGuiButtonHandlerBase {

	public PacketGuiButtonHandlerBase(){
		
	}
	public IMessage onPacketData(PacketGuiButtonBaseNew message, MessageContext ctx) {
		if(ctx.side.isServer()){

			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			Container epContainer = player.openContainer;
			ContainerBase container = message.getContainer(epContainer, message.getGuiID());
			if(container!=null){
				container.readPacketData(message.getButtonID(),message.getArgs());
				container.onPacketData();
			}
		}
		return null;
	}
}
