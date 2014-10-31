package com.hinasch.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class AbstractScanner extends UpdateEvent{
	
	//protected ArrayList<XYZPos> scheduledBreak;
	protected XYZPool scheduledBreak;
	protected PairID compareBlock;
	protected XYZPos startPoint;
	protected WorldHelper helper;

	protected int index;
	protected World world;
	protected int length;
	public boolean finish = false;

	public AbstractScanner(World world,PairID compareBlock,XYZPos startpoint,int length){
		this.compareBlock = compareBlock;
		this.scheduledBreak = new XYZPool(50);
		this.scheduledBreak.add(startpoint);
		this.startPoint = startpoint;
		this.length = length;
		this.world = world;
		this.helper = new WorldHelper(world);
	}
	

	@Override
	public void loop(){
		
		if(this.index<this.length){
			List<XYZPos> removeList = new ArrayList();
			List<XYZPos> addList = new ArrayList();
			for(XYZPos pos:scheduledBreak){
				Block block = helper.getBlock(pos);
				//Block block = world.getBlock(pos.x, pos.y, pos.z);
				if(block!=Blocks.air && !scheduledBreak.isDummy(pos)){

					this.hook(world, block, pos);
				}

				removeList.add(pos);
				for(XYZPos round:XYZPos.around){
					XYZPos addedPos = pos.add(round);
					PairID roundBlockData = helper.getBlockDatas(addedPos);
					if(compareBlock.equalsOrSameBlock(roundBlockData)){
						addList.add(addedPos);
					}
					if((compareBlock.getBlockObject() instanceof BlockRedstoneOre) && (roundBlockData.getBlockObject() instanceof BlockRedstoneOre)){
						addList.add(addedPos);
					}

				}
				
			}
			//消去を同時にやると怒られるので、分けてやる
			for(XYZPos rm:removeList){
				scheduledBreak.remove(rm);
			}
			
			for(XYZPos ad:addList){
				scheduledBreak.add(ad);
			}

			
		}else{
			this.finish = true;
			return;
		}
		this.index += 1;
		

	}
	
	@Override
	public boolean hasFinished(){
		return this.finish;
	}

	
	abstract public void hook(World world,Block currentBlock,XYZPos currentPos);
	
	public static class XYZPool extends Pool<XYZPos>{

		public XYZPool(int size) {
			super(size, new XYZPos(0,0,0));
			
		}
		
		@Override
		public boolean equalElements(XYZPos left,XYZPos right){
			return left.equalsInt(right);
		}
	}
}
