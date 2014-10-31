package com.hinasch.unlsaga.network.packet;

import net.minecraft.inventory.Container;

import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.lib.net.PacketGuiButtonHandlerBase;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.inventory.container.ContainerBartering;
import com.hinasch.unlsaga.inventory.container.ContainerCarrier;
import com.hinasch.unlsaga.inventory.container.ContainerChestUnsaga;
import com.hinasch.unlsaga.inventory.container.ContainerEquipment;
import com.hinasch.unlsaga.inventory.container.ContainerSkillPanel;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;
import com.hinasch.unlsagamagic.inventory.container.ContainerBlender;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketGuiButtonNew extends PacketGuiButtonBaseNew{


	public PacketGuiButtonNew(){
		super();
	}
	

	public PacketGuiButtonNew(int guiID, int buttonID) {
		super(guiID,buttonID);
	}
	
	public PacketGuiButtonNew(int guiID,int buttonID,byte bytearg){
		super(guiID,buttonID,(byte)bytearg);
	}

	@Override
	public Class[] getAdditionalArgs(int guiID){
		switch(guiID){
		case guiNumber.SMITH:

			return new Class[]{Byte.class};
		case guiNumber.BARTERING:
			return new Class[]{Byte.class};
		case guiNumber.SKILLPANEL:
			return new Class[]{Byte.class};
		
		}
		return null;
	}
	
	@Override
	public ContainerBase getContainer(Container openContainer,int guiID){
		switch(guiID){
		case guiNumber.SMITH:

			return (ContainerSmithUnsaga)openContainer;
		case guiNumber.BARTERING:
			return (ContainerBartering)openContainer;
		case guiNumber.SKILLPANEL:
			return (ContainerSkillPanel)openContainer;
		case guiNumber.EQUIP:
			return (ContainerEquipment)openContainer;
		case guiNumber.CHEST:
			return (ContainerChestUnsaga)openContainer;
		case guiNumber.BLENDER:
			return (ContainerBlender)openContainer;
		case guiNumber.CARRIER:
			return (ContainerCarrier)openContainer;
		}
		return null;
	}
	

	public static class PacketGuiButtonHandler extends PacketGuiButtonHandlerBase implements IMessageHandler<PacketGuiButtonNew,IMessage> {

		@Override
		public IMessage onMessage(PacketGuiButtonNew message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			return this.onPacketData(message, ctx);
		}
		
	}
}
