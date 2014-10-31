package com.hinasch.lib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Optional;

public class WorldHelper {

	public static XYZPos UP = new XYZPos(0,+1,0);
	public static XYZPos DOWN = new XYZPos(0,-1,0);
	
	public static final int SETBLOCK_FLAG_NO_NOTIFY = 0;
	public static final int SETBLOCK_FLAG_NOTIFY_NEIGHBOUR = 1; //onNeighbour..を呼ぶ
	public static final int SETBLOCK_FLAG_NOTIFY_CHUNKONLY = 2;
	public static final int SETBLOCK_FLAG_NOTIFY_ALL = 3;
	
	public World world;;
	public WorldHelper(World world){
		this.world = world;
	}
	
	public void setBlock(XYZPos pos,PairID pairid){
		this.setBlock(pos, pairid, SETBLOCK_FLAG_NOTIFY_ALL);
		
	}
	
	public void scheduleBlockUpdate(XYZPos pos,Block block,int tickrate){
		this.world.scheduleBlockUpdate(pos.x, pos.y, pos.z, block, tickrate);
	}
	public void setBlock(XYZPos pos,PairID pairid,int flag){
		if(pairid.getBlockObject()!=null){
			world.setBlock(pos.x, pos.y, pos.z, pairid.getBlockObject(), pairid.getMeta(), flag);
		}
	}
	
	public void setBlock(XYZPos pos,Block block,int meta,int flag){
		world.setBlock(pos.x, pos.y, pos.z, block, meta, flag);
		
	}
	public void setBlock(XYZPos pos,Block block){
		world.setBlock(pos.x, pos.y, pos.z, block);
		
		
	}
	
	public void setBlockToAir(XYZPos pos){
		world.setBlockToAir(pos.x, pos.y, pos.z);
	}
	
	public Block getBlock(XYZPos pos){
		return world.getBlock(pos.x, pos.y, pos.z);
	}
	
	public Material getMaterial(XYZPos pos){
		return world.getBlock(pos.x, pos.y,pos.z).getMaterial();
	}
	
	public int getBlockMetadata(XYZPos pos){
		return world.getBlockMetadata(pos.x, pos.y, pos.z);
	}
	
	public PairID getBlockDatas(XYZPos pos){
		PairID pairID = new PairID(world.getBlock(pos.x, pos.y, pos.z),this.getBlockMetadata(pos));
		return pairID;
	}
	
	public boolean isAirBlock(XYZPos pos){
		return world.isAirBlock(pos.x, pos.y, pos.z);
	}
	

	
	public float getBlockHardness(XYZPos pos){
		return this.getBlock(pos).getBlockHardness(world, pos.x, pos.y, pos.z);
	}
	
	public boolean isSideSolid(XYZPos xyz,ForgeDirection side){
		return world.isSideSolid(xyz.x, xyz.y, xyz.z, side);
	}
	public int getBlockHarvestLevel(XYZPos pos){
		return this.getBlock(pos).getHarvestLevel(this.getBlockMetadata(pos));
	}
	
	public void setBlockMetadata(XYZPos pos,int meta,int flag){
		this.world.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, meta, flag);
	}
	
	public void addBlockMetadata(XYZPos pos,int flag){
		int meta = this.getBlockMetadata(pos);
		if(meta<=0 && meta>=15){
			this.setBlockMetadata(pos, meta+1, flag);
		}
	}
	public XYZPos findNearMaterial(World world,Material material,XYZPos pos,int range){
		ScanHelper scan = new ScanHelper(pos.x,pos.y,pos.z,range,range);
		scan.setWorld(world);
		for(;scan.hasNext();scan.next()){
			if(scan.getBlock().getMaterial()==material){
				return scan.getAsXYZPos();
			}
		}
		return null;
	}
	
	public XYZPos findNearBlock(World world,Block block,XYZPos pos,int range){
		ScanHelper scan = new ScanHelper(pos.x,pos.y,pos.z,range,range).setWorld(world);
		for(;scan.hasNext();scan.next()){
			if(scan.getBlock()==block){
				return scan.getAsXYZPos();
			}
		}
		return null;
	}
	
	public TileEntity getTileEntity(XYZPos pos){
		return this.world.getTileEntity(pos.x, pos.y, pos.z);
	}
	
	public boolean isReplaceable(XYZPos pos){
		return this.getBlock(pos).isReplaceable(world, pos.x, pos.y, pos.z);
	}
	
	public static XYZPos getMapLength(FileObject pasteTempFile){
		int lenX = Integer.parseInt(pasteTempFile.read().get());
		int lenY = Integer.parseInt(pasteTempFile.read().get());
		int lenZ = Integer.parseInt(pasteTempFile.read().get());
		return new XYZPos(lenX,lenY,lenZ);
	}
	public static void loadMapToWorld(XYZPos pastePos,FileObject pasteTempFile,World world,WorldHelper worldHelper,UndoHook hook){
		int mx = Integer.parseInt(pasteTempFile.read().get());
		int my = Integer.parseInt(pasteTempFile.read().get());
		int mz = Integer.parseInt(pasteTempFile.read().get());
		XYZPos endPoint = pastePos.add(new XYZPos(mx,my,mz));
		if(hook!=null){
			hook.doUndo(world, pastePos, endPoint);
		}

		
		boolean flag = false;
		XYZPos shift = new XYZPos(0,0,0);
		//制限つき
		for(int stopper = 0; !flag || stopper<400;stopper += 1){
			Optional<String> line = pasteTempFile.read();
			if(!line.isPresent()){
				flag = true;
			}else{
				String[] data = line.get().split(",");
				//System.out.println(data.toString());
				PairID pair = new PairID();

				for(int i=0;i<data.length-1;i+=2){
					if(data[i]!=null && !data[i].equals("")){
						pair.setObject(Block.blockRegistry.getObjectById(Integer.parseInt(data[i])));

					}
					
					if(data.length > i+1){
						if(data[i+1]!=null && !data[i+1].equals("")){
							pair.setMeta(Integer.parseInt(data[i+1]));
						}
						//CreativeToolMod.log(Integer.parseInt(data[i+1]));
					}
					worldHelper.setBlock(pastePos.add(shift), pair);
					shift.x += 1;
					if(shift.x > mx){
						shift.z += 1;
						shift.x = 0;
					}
					if(shift.z > mz){
						shift.y += 1;
						shift.z = 0;
					}
					if(shift.y > my){
						flag = true;
					}
				}
			}

		}
	}
	
	public static class UndoHook{
		public void doUndo(World w,XYZPos xyz,XYZPos endpoint){
			
		}
	}
	
	public static int getHeightFromPos(World world,XYZPos pos){
		if(pos.y<0){
			return 0;
		}
		if(world.isAirBlock(pos.x,pos.y,pos.z)){
			return getHeightFromPos(world,new XYZPos(pos.x,pos.y-1,pos.z));
		}
		return pos.y;
		
	}
	public static PairIDList saveMapToTemp(FileObject fileobj,ScanHelper scan,World world,WorldHelper worldHelper){
		PairID blockSet = new PairID();
		//String mcdir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
		//FileObject fileobj = new FileObject(mcdir+"\\creativeitem_temp.txt");
		PairIDList listPair =  new PairIDList();
		fileobj.openForOutput();
		String temp = "";
		fileobj.write(String.valueOf(scan.endX - scan.startX)+"\r\n");
		fileobj.write(String.valueOf(scan.endY - scan.startY)+"\r\n");
		fileobj.write(String.valueOf(scan.endZ - scan.startZ)+"\r\n");
		for(;scan.hasNext();scan.next()){

			//blockSet.getFromWorld(world, scan.sx, scan.sy, scan.sz);
			blockSet = worldHelper.getBlockDatas(scan.getAsXYZPos());
			listPair.addStack(blockSet, 1);
			temp += Block.blockRegistry.getIDForObject(blockSet.getBlockObject())+","+blockSet.getMeta()+",";
			if(scan.isEndSide()){
				//				if(temp.substring(temp.length()-1).equals(",")){
				//					temp = temp.substring(0, temp.length()-2);
				//					
				//				}
				fileobj.write(temp+"\r\n");
				temp = "";
			}
		}
		

		return listPair;
	}
}
