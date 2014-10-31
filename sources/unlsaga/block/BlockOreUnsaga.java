package com.hinasch.unlsaga.block;

import java.util.Random;

import com.hinasch.unlsaga.Unsaga;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockOreUnsaga extends BlockOre{

	protected int blockDataIndex;
	public BlockOreUnsaga(int dataindex){
		super();
		this.blockDataIndex = dataindex;
		this.setHarvestLevel("pickaxe", Unsaga.blockOreData.harvestLevel.get(blockDataIndex));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+Unsaga.blockOreData.oreDictionaryList.get(blockDataIndex));
		
	}
	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{	
		return Unsaga.blockOreData.containerItem.get(blockDataIndex)==-1? Item.getItemFromBlock(this) : Unsaga.items.materials;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		return Unsaga.blockOreData.containerItem.get(blockDataIndex)==-1? par1 : Unsaga.blockOreData.containerItem.get(blockDataIndex);
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		Item drop = this.getItemDropped(meta, random, fortune);
		if (fortune > 0 && this != Block.getBlockFromItem(drop))
		{
			int j = random.nextInt(fortune + 2) - 1;

			if (j < 0)
			{
				j = 0;
			}

			return this.quantityDropped(random) * (j + 1);
		}
		else
		{
			return this.quantityDropped(random);
		}
	}
	
    private Random rand = new Random();
	@Override
	public int getExpDrop(IBlockAccess par1World, int par5, int par7)
	{
		if (this.getItemDropped(par5, rand, par7) != Item.getItemFromBlock(this))
		{
			int j1 = 0;

			if (Unsaga.blockOreData.containerItem.get(blockDataIndex)!=-1)
			{
				j1 = MathHelper.getRandomIntegerInRange(rand, 2, 5);
			}
			return j1;
		}

		return 0;
	}
	

}
