package com.hinasch.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ScanHelper {

	public static final int REVERSE = 2;
	
	public int sx;
	public int sy;
	public int sz;
	public int range;
	public int rangeY;
	public final int startX;
	public final int startY;
	public final int startZ;

	public final int endX;
	public final int endY;
	public final int endZ;
	

	public XYZPos count;
	
	public final int mode;
	public Entity entity;
	public World world;
	protected boolean scanned;

	public static XYZPos entityPosToXYZ(EntityLivingBase ep){
		XYZPos xyz = new XYZPos((int)ep.posX,(int)ep.posY,(int)ep.posZ);
		return xyz;
	}

	public static Integer[] swap(int par1,int par2){
		Integer[] var1 = new Integer[2];
		if(par1>par2){
			var1[0] = par2;
			var1[1] = par1;
			return var1;
		}
		var1[0] = par1;
		var1[1] = par2;
		return var1;
	}

	public ScanHelper(int sx1,int sy1,int sz1,int par1range,int par2rangey){
		this.range = par1range;
		this.rangeY = par2rangey;
		this.sx = sx1 - (range/2);
		this.sy = sy1 - (rangeY/2);
		this.sz = sz1 - (range/2);
		this.startX = this.sx;
		this.startY = this.sy;
		this.startZ = this.sz;
		this.scanned = false;
		this.mode = 0;
		this.endX = 0;
		this.endY = 0;
		this.endZ = 0;
	}

	public ScanHelper(Entity ep,int par1range,int par2rangey){
		this.range = par1range;
		this.rangeY = par2rangey;
		this.sx = (int)ep.posX - (range/2);
		this.sy = (int)ep.posY - (rangeY/2);
		this.sz = (int)ep.posZ - (range/2);
		this.startX = this.sx;
		this.startY = this.sy;
		this.startZ = this.sz;
		this.scanned = false;
		this.entity = ep;
		this.mode = 0;
		this.endX = 0;
		this.endY = 0;
		this.endZ = 0;
	}

	public ScanHelper(XYZPos start,XYZPos end){

		this.startX = start.x;
		this.startY = start.y;
		this.startZ = start.z;
		this.sx = this.startX;
		this.sy = this.startY;
		this.sz = this.startZ;
		this.endX = end.x;
		this.endY = end.y;
		this.endZ = end.z;
		this.scanned = false;
		this.mode = 1;

	}


	public ScanHelper(XYZPos start,XYZPos end,int mode){

		this.startX = start.x;
		this.startY = start.y;
		this.startZ = start.z;
		this.sx = this.startX;
		this.sy = this.startY;
		this.sz = this.startZ;
		this.endX = end.x;
		this.endY = end.y;
		this.endZ = end.z;
		this.scanned = false;
		this.mode = mode;

	}

	public ScanHelper setWorld(World par1){
		this.world = par1;
		return this;
	}

	public void next(){
		if(!scanned){
			if(this.mode==2){
				this.sx -= 1;
				
				if(this.sx < this.endX){
					this.sx = this.startX;
					this.sz -=1;
				}
				if(this.sz < this.endZ){
					this.sz = this.startZ;
					this.sy -= 1;
				}
				if(this.sy < this.endY){
					this.scanned = true;
				}
				return;
			}
			if(this.mode==1){
				this.sx += 1;
				if(this.sx > this.endX){
					this.sx = this.startX;
					this.sz += 1;
				}
				if(this.sz > this.endZ){
					this.sz = this.startZ;
					this.sy += 1;
					//System.out.println(this.sy);
				}
				if(this.sy > this.endY){
					this.scanned = true;
				}
				return;
			}
			this.sx += 1;
			if(this.sx > this.startX + range -1){
				this.sx = this.startX;
				this.sz += 1;
			}
			if(this.sz > this.startZ + range -1){

				this.sz = this.startZ;
				this.sy += 1;
				//System.out.println(this.sy);

			}
			if(this.sy > this.startY + rangeY -1){
				this.scanned = true;
			}
		}
	}

	public boolean isSide(){
		if(mode ==1){
			if(this.sx == this.startX)return true;
			if(this.sy == this.startY)return true;
			if(this.sz == this.startZ)return true;
			if(this.sx == this.endX)return true;
			if(this.sy == this.endY)return true;
			if(this.sz == this.endZ)return true;
		}
		return false;
	}
	
	public boolean isEndSide(){
		if(mode ==1){
			if(this.sx == this.endX)return true;
			if(this.sy == this.endY)return true;
			if(this.sz == this.endZ)return true;
		}
		return false;
	}

	public boolean hasNext(){
		if(scanned){
			return false;
		}
		if(this.mode==REVERSE){
			if(this.sy < this.endY){
				return false;
			}
		}
		if(this.mode==1){
			if(this.sy > this.endY){
				return false;
			}
		}
		if(this.mode==0 && this.sy > this.startY + rangeY){
			return false;
		}
		return true;
	}

	public Block getBlock(){

		if(this.world!=null){
			if(this.world.isAirBlock(this.sx,this.sy, this.sz)){
				return Blocks.air;
			}
			return this.world.getBlock(this.sx, this.sy, this.sz);
		}else{
			System.out.println("World is null(ScanHelper)");
			return null;
		}
	}
	
	public PairID getBlockData(){
		WorldHelper worldHelper = new WorldHelper(this.world);
		return worldHelper.getBlockDatas(this.getAsXYZPos());
	}

	public int getMetadata(){

		if(this.world!=null){
			return this.world.getBlockMetadata(sx, sy, sz);
		}else{
			System.out.println("World is null(ScanHelper)");
			return -1;
		}
	}

	public boolean isAirBlock(){
		if(this.world!=null){
			return this.world.isAirBlock(this.sx, this.sy, this.sz);
		}else{
			System.out.println("World is null(ScanHelper)");
			return false;
		}
	}

	public boolean isAirBlockUp(){
		if(this.world!=null){
			return this.world.isAirBlock(this.sx, this.sy+1, this.sz);
		}else{
			System.out.println("World is null(ScanHelper)");
			return false;
		}
	}

	public void	setBlockHere(Block par1){
		if(this.world!=null){
			this.world.setBlock(this.sx, this.sy, this.sz,par1);
			return;
		}else{
			System.out.println("World is null(ScanHelper)");
			return;
		}
	}
	
	public void	setBlockHereOffset(int offsetx,int offsety,int offsetz,Block par1){
		if(this.world!=null){
			this.world.setBlock(this.sx+offsetx, this.sy+offsety, this.sz+offsetz,par1);
			return;
		}else{
			System.out.println("World is null(ScanHelper)");
			return;
		}
	}
	
	public void	setBlockHereOffset(int offsetx,int offsety,int offsetz,Block par1,int par2, int par3){
		if(this.world!=null){
			this.world.setBlock(this.sx+offsetx, this.sy+offsety, this.sz+offsetz,par1,par2,par3);
			return;
		}else{
			System.out.println("World is null(ScanHelper)");
			return;
		}
	}

	public void	setBlockHere(Block par1,int par2,int par3){
		if(this.world!=null){
			this.world.setBlock(this.sx, this.sy, this.sz,par1,par2,par3);
			return;
		}else{
			System.out.println("World is null(ScanHelper)");
			return;
		}
	}

	public boolean canSnowHere(boolean lightCheck){
		if(this.world!=null){
			return this.world.provider.canSnowAt(this.sx, this.sy, this.sz,lightCheck);
		}else{
			System.out.println("World is null(ScanHelper)");
			return false;
		}
	}

	public boolean isOpaqueBlock(){
		if(this.world!=null){
			return this.world.getBlock(this.sx, this.sy, this.sz).isOpaqueCube();
		}else{
			System.out.println("World is null(ScanHelper)");
			return false;
		}
	}
	
	public boolean isValidHeight(){
		if(this.sy>0 && this.sy<255){
			return true;
		}
		return false;
	}

	public boolean isPlayerPos(){
		return ((int)this.entity.posX==this.sx)&&((int)this.entity.posY==this.sy)&&((int)this.entity.posZ==this.sz);
	}
	
	public XYZPos getCount(){
		if(this.count==null)this.count = new XYZPos(0,0,0);
		count.x = Math.abs(Math.abs(this.sx) - Math.abs(this.startX));
		count.y = Math.abs(Math.abs(this.sy) - Math.abs(this.startY));
		count.z = Math.abs(Math.abs(this.sz) - Math.abs(this.startZ));
		return count;
	}
	
	public XYZPos getAsXYZPos(){
		return new XYZPos(this.sx,this.sy,this.sz);
	}

}
