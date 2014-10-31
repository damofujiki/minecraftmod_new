package com.hinasch.lib.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

import com.hinasch.lib.base.ContainerBase;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class PacketGuiButtonBaseNew implements IMessage{

	public int buttonID;
	public int guiid;
	public Object[] args;
	
	public PacketGuiButtonBaseNew(){
		this.buttonID = 0;
		this.guiid = 0;
	}
	
	public PacketGuiButtonBaseNew(int guiid,int buttonID,Object... args){
		this.buttonID = buttonID;
		this.guiid = guiid;
		if(args.length>0){
			this.args = new Object[args.length];
			for(int i=0;i<args.length;i++){
				this.args[i] = args[i];
			}
		}

	}


	public int getButtonID(){
		return this.buttonID;
	}
	
	public int getGuiID(){
		return this.guiid;
	}
	
	public Object[] getArgs(){
		return this.args;
	}
	
	
	public void encodeAdditionalArgs(ByteBuf buffer){
		for(int i=0;i<args.length;i++){
			if(this.args[i] instanceof Integer){
				buffer.writeInt((Integer)args[i]);
			}
			if(this.args[i] instanceof Byte){
				buffer.writeByte((Byte)args[i]);
			}
			if(this.args[i] instanceof Float){
				buffer.writeFloat((Float)args[i]);
			}
			if(this.args[i] instanceof Short){
				buffer.writeShort((Short)args[i]);
			}
		}
	}

	
	public Class[] getAdditionalArgs(int guiID){
		return null;
	}
	public void decodeAdditionalArgs(ByteBuf buffer){
		Class[] clazzes = this.getAdditionalArgs(this.guiid);
		this.args = new Object[clazzes.length];

		for(int i=0;i<clazzes.length;i++){
			if(clazzes[i]==Integer.class){
				this.args[i] = buffer.readInt();
			}
			if(clazzes[i]==Byte.class){
				this.args[i] = buffer.readByte();
			}
			if(clazzes[i]==Float.class){
				this.args[i] = buffer.readFloat();
			}
			if(clazzes[i]==Short.class){
				this.args[i] = buffer.readShort();
			}
		}
	}

	

	public abstract ContainerBase getContainer(Container openContainer,int guiID);

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.guiid = buffer.readInt();
		if(this.getAdditionalArgs(this.guiid)!=null){
			this.decodeAdditionalArgs(buffer);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.buttonID);
		buffer.writeInt(this.guiid);
		if(this.getAdditionalArgs(this.guiid)!=null){
			this.encodeAdditionalArgs(buffer);
		}
		
	}


	
}
