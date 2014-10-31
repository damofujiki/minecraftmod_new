package com.hinasch.lib;

import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;


public class XYZPos {

	public int x;
	public int y;
	public int z;
	public boolean sw = false;
	
	public static final List<XYZPos> around = Lists.newArrayList(new XYZPos(1,0,0),new XYZPos(-1,0,0),new XYZPos(0,1,0),new XYZPos(0,-1,0),new XYZPos(0,0,1),new XYZPos(0,0,-1));
	
	//TODO ゲッターをつけたほうがいい？
	public double dx;
	public double dy;
	public double dz;
	protected boolean isBlockPos = false;
	
	public XYZPos(int par1,int par2,int par3){
		x=par1;
		y=par2;
		z=par3;
		dx=(double)par1;
		dy=(double)par2;
		dz=(double)par3;
		this.isBlockPos = true;
	}
	
	public XYZPos(double par1,double par2,double par3){
		x=(int)par1;
		y=(int)par2;
		z=(int)par3;
		dx=par1;
		dy=par2;
		dz=par3;
		this.isBlockPos = false;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if(this.isBlockPos){
			sb.append(x).append(",").append(y).append(",").append(z);
		}else{
			sb.append(dx).append(",").append(dy).append(",").append(dz);
		}
		
		return new String(sb);
		
	}
	
	public void setAsBlockPos(boolean par1){
		this.isBlockPos = par1;
	}
	
	public static XYZPos strapOff(String par1){
		String[] str = par1.split(",");
		if(str.length<3)return null;
		XYZPos xyz = new XYZPos(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
		return xyz;
		
	}
	
	public XYZPos getDirectedPos(ForgeDirection dir){
		return new XYZPos(this.x+dir.offsetX,this.y+dir.offsetY,this.z+dir.offsetZ);
	}
	
	public static XYZPos entityPosToXYZ(Entity en){
		XYZPos xyz = new XYZPos((int)en.posX,(int)en.posY,(int)en.posZ);
		xyz.dx = en.posX;
		xyz.dy = en.posY;
		xyz.dz = en.posZ;
		return xyz;
	}
	
	public static XYZPos tileEntityPosToXYZ(TileEntity en){
		XYZPos xyz = new XYZPos((int)en.xCoord,(int)en.yCoord,(int)en.zCoord);
		xyz.dx = en.xCoord;
		xyz.dy = en.yCoord;
		xyz.dz = en.zCoord;
		return xyz;
	}
	
	public static XYZPos[] compareAndSwap(XYZPos par1,XYZPos par2){
		XYZPos[] xyz = new XYZPos[2];
		XYZPos newxyz = new XYZPos(0,0,0);
		XYZPos newxyze = new XYZPos(0,0,0);
		if(par1.x>par2.x){
			newxyz.x = par2.x;
			newxyze.x = par1.x;
		}else{
			newxyz.x = par1.x;
			newxyze.x = par2.x;
		}
		if(par1.y>par2.y){
			newxyz.y = par2.y;
			newxyze.y = par1.y;
		}else{
			newxyz.y = par1.y;
			newxyze.y = par2.y;
		}
		if(par1.z>par2.z){
			newxyz.z = par2.z;
			newxyze.z = par1.z;
		}else{
			newxyz.z = par1.z;
			newxyze.z = par2.z;
		}
		xyz[0] = newxyz;
		xyz[1] = newxyze;
		return xyz;
		
	}
	
	public XYZPos subtract(XYZPos par2){
		return new XYZPos(this.x - par2.x,this.y - par2.y,this.z - par2.z);
	}
	
	public XYZPos add(XYZPos par2){
		return new XYZPos(this.x + par2.x,this.y + par2.y,this.z + par2.z);
	}
	
	public XYZPos addPos(int px,int py,int pz){
		XYZPos newpos = this.add(new XYZPos(px,py,pz));
		return newpos;
	}
	
	public XYZPos addDouble(double ax,double ay,double az){
		XYZPos newpos = new XYZPos(this.dx,this.dy,this.dz);
		newpos.dx += ax;
		newpos.dy += ay;
		newpos.dz += az;
		return newpos;
	}
	
	//おもにデバグ用？
	public void setBlockToHere(World world,Block blockid){
		world.setBlock(this.x,this.y, this.z, blockid);
	}
	
	public void sync(){
		this.dx = (double)this.x;
		this.dy = (double)this.y;
		this.dz = (double)this.z;
	}
	
	public boolean equalsInt(XYZPos pos){
		if(this.x==pos.x && this.y==pos.y && this.z==pos.z){
			return true;
		}
		return false;
	}
	
	public static XYZPos buildFromByteArrayDataInput(ByteArrayDataInput data){
		XYZPos xyz = new XYZPos(0,0,0);
		xyz.x = data.readInt();
		xyz.y = data.readInt();
		xyz.z = data.readInt();
		return xyz;
	}
	
	public XYZPos getGaussian(Random rand){
		XYZPos xyz = new XYZPos(this.x,this.y,this.z);
		xyz.dx += rand.nextGaussian();
		xyz.dy += rand.nextGaussian();
		xyz.dz += rand.nextGaussian();
		return xyz;
	}
	
	/* integer only */
	public void writeToBuffer(ByteBuf buff){
		buff.writeInt(this.x);
		buff.writeInt(this.y);
		buff.writeInt(this.z);
	}
	
	public double getDistanceTo(XYZPos pos){
		Vec3 v1 = Vec3.createVectorHelper(this.dx,this.dy,this.dz);
		Vec3 v2 = Vec3.createVectorHelper(pos.dx, pos.dy, pos.dz);
		return v1.distanceTo(v2);
	}
	public static XYZPos readFromBuffer(ByteBuf buf){
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		return new XYZPos(x,y,z);
	}
}
