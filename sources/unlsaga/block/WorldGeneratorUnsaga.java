package com.hinasch.unlsaga.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.IWorldGenerator;

public class WorldGeneratorUnsaga implements IWorldGenerator{


	private void generateEnd(World var1, Random var2, int var3, int var4) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	private void generateNether(World var1, Random var2, int var3, int var4) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		if(Unsaga.configs.enableGenerateOres){
	        this.genStandardOre1(world, rand, new XYZPos(chunkX,0,chunkZ), 20, 0, 64,Unsaga.blockOreData.getOreBlock(BlockDataUnsaga.COPPER),8);
	        this.genStandardOre1(world, rand, new XYZPos(chunkX,0,chunkZ), 4, 0, 40,Unsaga.blockOreData.getOreBlock(BlockDataUnsaga.LEAD),8);
	        this.genStandardOre1(world, rand, new XYZPos(chunkX,0,chunkZ), 2, 0, 32,Unsaga.blockOreData.getOreBlock(BlockDataUnsaga.SILVER),8);
	        this.genStandardOre1(world, rand, new XYZPos(chunkX,0,chunkZ), 1, 0, 24,Unsaga.blockOreData.getOreBlock(BlockDataUnsaga.SAPPHIRE),6);
	        this.genStandardOre1(world, rand, new XYZPos(chunkX,0,chunkZ), 1, 0, 24,Unsaga.blockOreData.getOreBlock(BlockDataUnsaga.RUBY),6);
	        
		}

	}
		
    protected void genStandardOre1(World world,Random rand,XYZPos chunkPos,int chance,  int min, int max,Block block,int dens)
    {
        for (int l = 0; l < chance; ++l)
        {
            int firstBlockXCoord = chunkPos.x + rand.nextInt(16);
            int firstBlockYCoord = rand.nextInt(max - min) + min;
            int firstBlockZCoord =chunkPos.z + rand.nextInt(16);
            (new WorldGenMinable(block, dens)).generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
        }
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.dimensionId){
        case -1:
            generateNether(world, random, chunkX * 16, chunkZ * 16);
            break;
        case 0:
        	generateOverworld(world, random, chunkX * 16, chunkZ * 16);
            break;
        case 1:
            generateEnd(world, random, chunkX * 16, chunkZ * 16);
            break;
        }
		
	}
}
