package com.hinasch.unlsaga.villager.bartering;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;
import com.hinasch.lib.library.LibraryBook;
import com.hinasch.lib.library.LibraryShelf;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;

public class MerchandiseLibrary extends LibraryShelf{

	public MerchandiseLibrary(){
			libSet.add(getBook("logWood",35));
			libSet.add(getBook("gemAmber",200));
			libSet.add(getBook("ingotTin",200));
			libSet.add(getBook("oreTin",100));
			libSet.add(getBook(Blocks.log,36));
			libSet.add(getBook(Blocks.planks,9));
			libSet.add(getBook(Blocks.cobblestone,5));
			libSet.add(getBook(Blocks.stone,10));
			libSet.add(getBook(Blocks.sand,10));
			libSet.add(getBook(Blocks.glass,20));
			libSet.add(getBook(Blocks.obsidian,500));
			libSet.add(getBook("ingotGold",1000));
			libSet.add(getBook("oreGold",900));
			libSet.add(getBook("ingotIron",500));
			libSet.add(getBook("oreIron",350));
			libSet.add(getBook("ingotCopper",300));
			libSet.add(getBook("oreCopper",200));
			libSet.add(getBook("ingotLead",360));
			libSet.add(getBook("oreLead",260));
			libSet.add(getBook("ingotSilver",480));
			libSet.add(getBook("oreSilver",400));
			libSet.add(getBook("gemRuby",2000));
			libSet.add(getBook("gemSapphire",2200));
			libSet.add(getBook(Items.bow,100));
			libSet.add(getBook(Items.iron_ingot,500));
			libSet.add(getBook(Items.gold_ingot,900));
			libSet.add(getBook(Items.redstone,25));
			libSet.add(getBook(Items.rotten_flesh,5));
			libSet.add(getBook(Items.fish,100));
			libSet.add(getBook(Items.ender_pearl,1200));
			libSet.add(getBook(Items.emerald,2500));
			libSet.add(getBook(Items.gold_nugget,100));
			libSet.add(getBook(Items.string,10));
			libSet.add(getBook(Items.feather,100));
			libSet.add(getBook(Items.diamond,8000));
			libSet.add(getBook(ToolMaterial.GOLD,1000));
			libSet.add(getBook(Items.bone,75));
			libSet.add(getBook(Items.stick,5));
			libSet.add(getBook(Items.coal,30));
			libSet.add(getBook(Blocks.gravel,10));
			libSet.add(getBook(Items.wheat,20));
			libSet.add(getBook(Items.leather,100));
			libSet.add(getBook(Blocks.sand,10));
			libSet.add(getBook(Blocks.wool,20));
			libSet.add(getBook(Items.blaze_rod,500));
			libSet.add(getBook(Items.brick,15));
			libSet.add(getBook(Items.wheat_seeds,5));
			libSet.add(getBook(Items.melon_seeds,15));
			libSet.add(getBook(Items.pumpkin_seeds,9));
			libSet.add(getBook(Items.slime_ball,20));
			libSet.add(getBook(Items.iron_horse_armor,1200));
			libSet.add(getBook(Items.diamond_horse_armor,7500));
			libSet.add(getBook(Items.golden_horse_armor,2000));
			libSet.add(getBook("record",800));
			libSet.add(getBook(Items.book,100));
			libSet.add(getBook(Items.glowstone_dust,50));
			libSet.add(getBook(Blocks.cactus,30));
			libSet.add(getBook(Blocks.anvil,5000));
	}
	
	@Override
	public Optional<LibraryBook> find(Object object){
		
		
		return super.find(object);
		
	}
	
	public MerchandiseLibraryBook getBook(Object par1,int price){
		return new MerchandiseLibraryBook(par1,price);
	}
	public int getPriceFromUnsagaMaterialItem(ItemStack is){
		int price = 0;
		UnsagaMaterial material = null;
		MaterialInfo info = new MaterialInfo(is);
		if(info.getMaterial().isPresent()){
			
			material = info.getMaterial().get();
			price = material.getBasePrice();
//			price = (material.getRank()+3);
//			price *= price;
//			price = price * 8;
//			if(material.rank>=8){
//				price = (int)((float)price *2.5F);
//			}
//			if(material==Unsaga.materialManager.diamond){
//				price *= 3;
//			}
		}else{
			price = 5;
		}
		return price;
	}
	
	
//	public Optional<Integer> findPrice(ItemStack is){
//		if(this.findInfo(is).isPresent()){
//			MerchandiseLibraryBook info = (MerchandiseLibraryBook)this.findInfo(is).get();
//			return Optional.of(info.price);
//		}
//		return Optional.absent();
//	}
//	
//	public Optional<Integer> findPrice(String input){
//		if(this.findEnumInfo(input).isPresent()){
//			MerchandiseLibraryBook info = (MerchandiseLibraryBook)this.findEnumInfo(input).get();
//			return Optional.of(info.price);
//		}
//		return Optional.absent();
//	}
}
