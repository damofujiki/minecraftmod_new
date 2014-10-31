package com.hinasch.unlsaga.ability.skill.effect;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.hinasch.lib.AbstractScanner;
import com.hinasch.lib.PairID;
import com.hinasch.lib.SoundAndSFX;
import com.hinasch.lib.XYZPos;

public class ScannerBreakBlock extends AbstractScanner{

	protected int breaked;
	
	public ScannerBreakBlock(World world,int length,PairID compareBlock, XYZPos startpoint) {
		super(world,compareBlock, startpoint,length);
		this.breaked = 0;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void hook(World world, Block currentBlock, XYZPos currentPos) {

		currentBlock.dropXpOnBlockBreak(world, startPoint.x, startPoint.y, startPoint.z, currentBlock.getExpDrop(world, compareBlock.getMeta(), 0));
		SoundAndSFX.playBlockBreakSFX(world, currentPos, compareBlock);
		breaked += 1;


	}



}
