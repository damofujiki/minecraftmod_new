package com.hinasch.unlsaga.villager.smith;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;
import com.hinasch.lib.PairID;
import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.NoFunctionItems;
import com.hinasch.unlsaga.item.NoFunctionItems.ItemData;
import com.hinasch.unlsaga.item.UnsagaEnum;
import com.hinasch.unlsaga.item.misc.ItemIngotsUnsaga;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class MaterialInfo {

	
	public ItemStack is;
	
	public MaterialInfo(ItemStack is){
		this.is = is;
	}
	
	//回復するダメージ量
	public Optional<Integer> getPositiveDamage(){
		Item item = (Item)this.is.getItem();
		if(item.isRepairable()){
			return Optional.of(is.getMaxDamage() - is.getItemDamage());
		}
		if(item instanceof ItemIngotsUnsaga){
			NoFunctionItems.ItemData nofunc = Unsaga.noFunctionItems.getDataFromMeta(this.is.getItemDamage());
			return Optional.of(nofunc.repair);
		}
		if(Unsaga.materialLibrary.find(this.is).isPresent()){
			MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialLibrary.find(this.is).get();
			return Optional.of(info.damage);
		}
		return Optional.absent();
	}
	
	public int getWeight(){
		if(UtilNBT.hasKey(this.is, "weight")){
			return HelperUnsagaItem.getCurrentWeight(this.is);
			
		}
		if(this.getMaterial().isPresent()){
			return this.getMaterial().get().getWeight();
		}
		return 0;
	}
	
	public UnsagaEnum.ToolCategory getCategory(){
		if(this.is.getItem() instanceof IUnsagaMaterialTool){
			return ((IUnsagaMaterialTool)is.getItem()).getCategory();
		}
		return null;
	}
	public boolean isValidMaterial(){
		//鎧は作れない
		if(this.is.getItem() instanceof ItemArmor){
			return false;
		}
		//とりあえず素材の情報がとれたらOK

		if(this.getMaterial().isPresent()){
			return true;
		}
		return false;
	}
	
	public Optional<UnsagaMaterial> getMaterial(){
		Item item = (Item)this.is.getItem();
		UnsagaMaterial material = null;
		if(item instanceof IUnsagaMaterialTool){
			if(material==null){
				material = HelperUnsagaItem.getMaterial(this.is);
			}
			
			
		}
		Unsaga.debug(material,this.getClass());
//		if(item instanceof ItemSword && material==null){
//			
//		}
		//素材と関連付けられたアイテムから走査
		for(UnsagaMaterial mate:Unsaga.materialManager.getAllMaterialValues()){
			if(mate.getAssociatedItemStack().isPresent()){
				PairID pair = new PairID(mate.getAssociatedItemStack().get().getItem(),mate.getAssociatedItemStack().get().getItemDamage());
				if(pair.equalsPair(new PairID(is.getItem(),is.getItemDamage()))){
					if(material==null){
						material = mate;
					}

				}
			}
		}
		//無料力アイテムから
		if(item instanceof ItemIngotsUnsaga && material==null){
			ItemData nofunc = Unsaga.noFunctionItems.getDataFromMeta(this.is.getItemDamage());
			if(material==null){
				material = nofunc.getAssociatedMaterial();
			}
			
		}
		
		if(Unsaga.materialLibrary.find(is).isPresent()){
			MaterialLibraryBook book = (MaterialLibraryBook) Unsaga.materialLibrary.find(is).get();
			if(material==null){
				material =  book.material;
			}

		}
		//
//		if(item instanceof ItemTool && material==null){
//			if(Unsaga.materialFactory.findEnumInfo(((ItemTool)item).getToolMaterialName()).isPresent()){
//				MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialFactory.findEnumInfo(((ItemTool)item).getToolMaterialName()).get();
//				material = info.material;
//			}
//		}
//		if(item instanceof ItemSword && material==null){
//			if(Unsaga.materialFactory.findEnumInfo(((ItemSword)item).getToolMaterialName()).isPresent()){
//				MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialFactory.findEnumInfo(((ItemSword)item).getToolMaterialName()).get();
//				material = info.material;
//			}
//		}
//		if(item instanceof ItemArmor && material==null){
//			if(Unsaga.materialFactory.findEnumInfo(((ItemArmor)item).getArmorMaterial().toString()).isPresent()){
//				MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialFactory.findEnumInfo(((ItemSword)item).getToolMaterialName()).get();
//				material = info.material;
//			}
//		}
//		if(item instanceof ItemHoe && material==null){
//			if(Unsaga.materialFactory.findEnumInfo(((ItemHoe)item).getMaterialName().toString()).isPresent()){
//				MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialFactory.findEnumInfo(((ItemSword)item).getToolMaterialName()).get();
//				material = info.material;
//			}
//		}
//		if(Unsaga.materialFactory.findInfo(this.is).isPresent() && material==null){
//			MaterialLibraryBook info = (MaterialLibraryBook) Unsaga.materialFactory.findInfo(this.is).get();
//			material = info.material;
//		}
		
		if(material==null){
			return Optional.absent();
		}
		return Optional.of(material);
	}
	
	

}
