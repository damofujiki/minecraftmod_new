package com.hinasch.lib;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class SoundAndSFX {

	public static void playBlockBreakSFX(World world,XYZPos pos,PairID blockdata,boolean sw){
		world.playAuxSFX(2001, pos.x, pos.y, pos.z, Block.getIdFromBlock(blockdata.getBlockObject()) + (blockdata.getMeta()  << 12));
		//if(!world.isRemote){
			//Unsaga.logger.log("kiteru");;
			boolean flag = world.setBlockToAir(pos.x, pos.y, pos.z);
			if (blockdata.getBlockObject() != null && flag) {
				blockdata.getBlockObject().onBlockDestroyedByPlayer(world, pos.x, pos.y, pos.z, blockdata.getMeta());
	
				if(!sw){
					blockdata.getBlockObject().dropBlockAsItem(world, pos.x, pos.y, pos.z, blockdata.getMeta(),1);
				}
				
	
	
	
			}
		//}
	}

	public static void playBlockBreakSFX(World world,XYZPos pos,PairID blockdata){
		playBlockBreakSFX(world,pos,blockdata,false);
	}

	public static void playPlaceSound(World par3World,XYZPos xyz,Block.SoundType sound){
		
		par3World.playSoundEffect((double)((float)xyz.dx + 0.5F), (double)((float)xyz.dy + 0.5F), (double)((float)xyz.dz + 0.5F), sound.func_150496_b(), (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
	}
}
