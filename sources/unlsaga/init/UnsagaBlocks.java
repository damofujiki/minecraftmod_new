package com.hinasch.unlsaga.init;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.block.BlockChestUnsagaNew;
import com.hinasch.unlsaga.block.BlockFallStone;
import com.hinasch.unlsaga.block.BlockOreUnsaga;
import com.hinasch.unlsaga.item.misc.ItemBlockOreUnsaga;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsaga.tileentity.TileEntityFallStone;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;

public class UnsagaBlocks {


	public Block fallStone;
	public Block bonusChest;
	public Block blockBlender;
	public Block air;
	public Block[] ores;
	
	
	public Class<? extends TileEntity> tileEntityBonusChest;
	public Class<? extends ItemBlock> classItemBlockOre;
	//public static Class<? extends ItemBlockOreUnsaga> classItemBlockOre;
	
	public void loadConfig(Configuration config) {
//		PropertyCustom prop = new PropertyCustom(new String[]{"BlockID.FallingStone","BlockID.Chest.Unsaga","BlockID.Ores"
//				,"BlockID.AirBlock"});
//		prop.setValues(new Integer[]{1000,1001,1002,1003});
//		prop.setCategoriesAll(config.CATEGORY_BLOCK);
//		prop.buildProps(config);
//		
//		blockFallStoneID = prop.getProp(0).getInt();
//		blockChestUnsagaID = prop.getProp(1).getInt();
//		blockUnsagaOresID = prop.getProp(2).getInt();
//		blockAirID = prop.getProp(3).getInt();
	}

	public void registerBlocksAndTileEntity() {

		bonusChest = new BlockChestUnsagaNew().setBlockName("unsaga.chest").setHardness(2.5F).setCreativeTab(Unsaga.tabMain);
		fallStone = new BlockFallStone(Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone)
				.setBlockName("unsaga.stonefalling").setCreativeTab(Unsaga.tabMain);
		//blockAir = new BlockNothing(blockAirID,Material.air).setHardness(100.0F).setUnlocalizedName("unsaga.nothing");
		ores = new Block[Unsaga.blockOreData.unlocalizedNames.size()];
		for(int i=0;i<Unsaga.blockOreData.unlocalizedNames.size();i++){
			ores[i] = new BlockOreUnsaga(i).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone)
					.setBlockName("unsaga."+Unsaga.blockOreData.unlocalizedNames.get(i)).setCreativeTab(Unsaga.tabMain);
			GameRegistry.registerBlock(ores[i], ItemBlockOreUnsaga.class,Unsaga.blockOreData.unlocalizedNames.get(i));
		}
		//blockOreUnsaga = new BlockOreUnsaga().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone)
		//		.setBlockName("unsaga.ore").setCreativeTab(Unsaga.tabUnsaga);
		//tileEntityChestUnsaga = TileEntityChestUnsaga.class;
		

		GameRegistry.registerTileEntity(TileEntityChestUnsagaNew.class, "unsaga.tileEntityChestUnsaga");
		GameRegistry.registerTileEntity(TileEntityFallStone.class, "unsaga.fallingStone");
		//GameRegistry.registerTileEntity(TileEntityShapeMemory.class, "unsaga.tileEntityShapeMemory");
		GameRegistry.registerBlock(bonusChest,ItemBlock.class,"bonuschest");
		
		GameRegistry.registerBlock(fallStone,ItemBlock.class,"fallstone");
	}

}
