package com.hinasch.unlsaga.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;
import com.hinasch.unlsaga.Unsaga;

public class BlockDataUnsaga {
	public List<String> oreDictionaryList = Lists.newArrayList("oreLead","oreRuby","oreSapphire","oreSilver","oreCopper","oreAngelite","oreDemonite");
	public List<String> unlocalizedNames = Lists.newArrayList("oreLead","oreRuby","oreSapphire","oreSilver","oreCopper","oreAngelite","oreDemonite");
	public List<Float> exps = Lists.newArrayList(0.7F,1.0F,1.0F,0.7F,0.7F,1.0F,1.0F);
	public List<Integer> smelted = Lists.newArrayList(5,6,7,4,3,16,17);
	//public static List<String> localizedNameJP = Lists.newArrayList("鉛鉱石","鋼玉鉱石","鋼玉鉱石","銀鉱石","銅鉱石","聖石鉱石","魔石鉱石");
	public List<Integer> harvestLevel = Lists.newArrayList(1,2,2,1,1,1,1);
	//public static List<String> localizedNameEN = Lists.newArrayList("Lead Ore","Corundum Ore","Corundum Ore","Silver Ore","Copper Ore","Angelite Ore","Demonite Ore");
	public List<Integer> containerItem = Lists.newArrayList(-1,6,7,-1,-1,16,17);

	public static final int LEAD = 0;
	public static final int RUBY = 1;
	public static final int SAPPHIRE = 2;
	public static final int SILVER = 3;
	public static final int COPPER = 4;
	public static final int ANGELITE = 5;
	public static final int DEMONITE = 6;

	public Block getOreBlock(int type){
		return Unsaga.blocks.ores[type];
	}
	public void registerSmeltingAndAssociation(){



		for(int i=0;i<Unsaga.blocks.ores.length;i++){
			//ItemStack blockitem = new ItemStack(UnsagaBlocks.blocksOreUnsaga[i],1,i);
			ItemStack smeltedItemStack = Unsaga.noFunctionItems.getItemStack(1, smelted.get(i));			
			FurnaceRecipes.smelting().func_151393_a(Unsaga.blocks.ores[i],smeltedItemStack, exps.get(i));
			OreDictionary.registerOre(oreDictionaryList.get(i),Unsaga.blocks.ores[i]);
		}

		Unsaga.materialManager.copperOre.associate(new ItemStack(Unsaga.blocks.ores[oreDictionaryList.indexOf("oreCopper")],1));

	}
}
