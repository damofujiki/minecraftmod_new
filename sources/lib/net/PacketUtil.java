package com.hinasch.lib.net;

import com.hinasch.lib.XYZPos;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class PacketUtil {

	public static XYZPos bufferToXYZPos(ByteBuf buf){
		XYZPos xyz = new XYZPos(0,0,0);
		xyz.x = buf.readInt();
		xyz.y = buf.readInt();
		xyz.z = buf.readInt();
		xyz.sync();
		return xyz;
	}
	
	public static void XYZPosToPacket(ByteBuf buf,XYZPos xyz){
		buf.writeInt(xyz.x);
		buf.writeInt(xyz.y);
		buf.writeInt(xyz.z);
	}
	
	public static TargetPoint getTargetPointNear(Entity entity){
		TargetPoint target = new TargetPoint(entity.worldObj.getWorldInfo().getVanillaDimension(), 
				entity.posX, entity.posY, entity.posZ,100D);
		return target;
	}
	
	public static TargetPoint getTargetPointNear(XYZPos pos,World world){
		TargetPoint target = new TargetPoint(world.getWorldInfo().getVanillaDimension(), 
				pos.dx, pos.dy, pos.dz,100D);
		return target;
	}
}
