package com.hinasch.unlsaga.villager.smith;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import com.hinasch.lib.library.LibraryShelf;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.material.UnsagaMaterials;


public class MaterialLibrary extends LibraryShelf{
	

	public MaterialLibrary(){
		super();
		UnsagaMaterials materials = Unsaga.materialManager;

		addShelf(new MaterialLibraryBook(Items.iron_ingot,materials.iron,100));
		addShelf(new MaterialLibraryBook("oreSilver",materials.serpentine,18));
		addShelf(new MaterialLibraryBook("oreCopper",materials.copperOre,17));
		addShelf(new MaterialLibraryBook("oreIron",materials.ironOre,25));
		addShelf(new MaterialLibraryBook("ingotIron",materials.iron,80));
		addShelf(new MaterialLibraryBook("ingotCopper",materials.copper,50));
		addShelf(new MaterialLibraryBook("ingotSteel",materials.steel1,300));
		addShelf(new MaterialLibraryBook("ingotSilver",materials.silver,70));
		addShelf(new MaterialLibraryBook("ingotDamascus",materials.damascus,2000));
		addShelf(new MaterialLibraryBook("ingotLead",materials.lead,100));
		addShelf(new MaterialLibraryBook("gemRuby",materials.corundum1,1400));
		addShelf(new MaterialLibraryBook("gemSapphire",materials.corundum2,1400));
		addShelf(new MaterialLibraryBook("ingotFaerieSilver",materials.fairieSilver,1000));
		addShelf(new MaterialLibraryBook("stoneMeteorite",materials.meteorite,200));
		addShelf(new MaterialLibraryBook("ingotMeteoriticIron",materials.meteoricIron,400));
		addShelf(new MaterialLibraryBook("gemCarnelian",materials.carnelian,50));
		addShelf(new MaterialLibraryBook("gemTopaz",materials.topaz,50));
		addShelf(new MaterialLibraryBook("gemRavenite",materials.ravenite,50));
		addShelf(new MaterialLibraryBook("gemLapis",materials.lazuli,50));
		addShelf(new MaterialLibraryBook("gemOpal",materials.opal,50));
		addShelf(new MaterialLibraryBook("gemAngelite",materials.lazuli,100));
		addShelf(new MaterialLibraryBook("gemDemonite",materials.opal,100));
		addShelf(new MaterialLibraryBook("stoneDebris",materials.debris,1));
		addShelf(new MaterialLibraryBook("logWood",materials.wood,20));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log,1,0),materials.oak,20));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log,1,1),materials.spruce,25));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log,1,2),materials.birch,25));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log,1,3),materials.jungleWood,20));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log2,1,0),materials.acacia,30));
		addShelf(new MaterialLibraryBook(new ItemStack(Blocks.log2,1,1),materials.darkOak,30));
		addShelf(new MaterialLibraryBook(Blocks.cobblestone,materials.serpentine,5));
		addShelf(new MaterialLibraryBook(Blocks.stone,materials.serpentine,10));
		addShelf(new MaterialLibraryBook(Blocks.planks,materials.wood,5));
		addShelf(new MaterialLibraryBook(Items.stick,materials.oak,2));
		addShelf(new MaterialLibraryBook(Items.diamond,materials.diamond,2000));
		addShelf(new MaterialLibraryBook(Blocks.stone,materials.categoryStone,13));
		addShelf(new MaterialLibraryBook(Items.feather,materials.feather,3));
		addShelf(new MaterialLibraryBook(Items.bone,materials.bone,5));
		addShelf(new MaterialLibraryBook(Blocks.dragon_egg,materials.dragonHeart,30000));
		addShelf(new MaterialLibraryBook(Unsaga.items.musket,materials.iron,200));
		addShelf(new MaterialLibraryBook(ToolMaterial.EMERALD,materials.diamond,2000));
		addShelf(new MaterialLibraryBook(ToolMaterial.WOOD,materials.oak,20));
		addShelf(new MaterialLibraryBook(ToolMaterial.IRON,materials.iron,80));
		addShelf(new MaterialLibraryBook(ToolMaterial.STONE,materials.serpentine,5));
	}
}
