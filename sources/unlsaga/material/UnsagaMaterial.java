package com.hinasch.unlsaga.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;
import com.hinasch.lib.PairID;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.util.translation.Translation;

public class UnsagaMaterial {

	protected PairID associatedItem;
	protected Optional<Float> attackModifier = Optional.absent();
	protected Optional<Integer> bowModifier = Optional.absent();
	protected Optional<ArmorMaterial> enumArmor = Optional.absent();
	protected Optional<ToolMaterial> enumTool = Optional.absent();

	protected Map<ToolCategory,Float> suitedMap = new HashMap();
	
	public String iconname;
	public boolean isChild;
	public boolean isRelatedVanillaItem = false;
	public Optional<Integer> itemMeta = Optional.absent();

	public String name;
	protected UnsagaMaterial parentMaterial;
	public int rank;
	protected Optional<Integer> renderColor = Optional.absent();
	
	
	protected int basePrice;
	protected Map<ToolCategory,List<String>> specialArmorTextureMap;

	protected Map<ToolCategory,String> specialIconMap;
	protected Map<ToolCategory,String> specialNameMap;
	protected Map<String,UnsagaMaterial> subMaterialMap = new HashMap();
	public int weight;
	
	
	public UnsagaMaterial(String name,int weight,int rank){
		this.name = name;
		this.weight = weight;
		this.rank = rank;
		this.isChild = false;
		this.iconname = name;
		this.basePrice = rank * rank;
		//Unsaga.materialManager.allMaterialMap.put(this.name, this);
	}
	
	
	
	public UnsagaMaterial(String name,int weight,int rank,String en,String jp,String jpname){

		this(name,weight,rank);
//		this.jpName = jpname;

		
	}
	
	public UnsagaMaterial setBasePrice(int price){
		this.basePrice = price;
		return this;
	}
	
	public int getBasePrice(){
		return this.basePrice;
	}
	public String getName(){
		return this.name;
	}
	
	public int getRank(){
		return this.rank;
	}
	
	public int getWeight(){
		return this.weight;
	}
	public boolean isChild(){
		return this.isChild;
	}
	public UnsagaMaterial addToMap(Map<String,UnsagaMaterial> map){
		map.put(this.name, this);
		return this;
	}
	
	public UnsagaMaterial addSubMaterial(UnsagaMaterial mat){
		if(this.isChild){
			Unsaga.debug("this Material is a child.can't add child Material."+mat);
			return mat;
		}
		mat.isChild = true;
		mat.parentMaterial = this;
		this.subMaterialMap.put(mat.name, mat);		
		return mat;
	}
	public void associate(ItemStack is){
		Item item = (Item)is.getItem();
		this.associatedItem = new PairID(item,is.getItemDamage());

	}
	

	
	public ItemArmor.ArmorMaterial getArmorMaterial(){
		if(this.enumArmor.isPresent()){
			return this.enumArmor.get();
		}
		if(this.isChild){
			return this.getParentMaterial().getArmorMaterial();
		}
		Unsaga.debug("this Material has no EnumArmorMAterial:"+this.name);
		return ItemArmor.ArmorMaterial.CLOTH;
	}
	public Optional<ItemStack> getAssociatedItemStack(){
		if(this.associatedItem!=null){
			return Optional.of(new ItemStack(this.associatedItem.getItemObject(),1,this.associatedItem.getMeta()));
		}
		return Optional.absent();
	}
	
	public float getAttackModifier(ToolCategory type){
		float base = 4.0F;
		switch(type){
		case AXE:
			base = 3.0F;
			break;
		case STAFF:
			base = 2.0F;
			break;
		case SPEAR:
			base = 3.0F;
			break;
		default:
		}

		
		if(this.attackModifier.isPresent()){
			return base + this.attackModifier.get();
		}
		return base + this.getToolMaterial().getDamageVsEntity();
	}
	
	public int getBowModifier(){
		if(this.bowModifier.isPresent()){
			return this.bowModifier.get();
		}
		return 0;
	}
	
	public String getLocalized(){
		return Translation.localize(this.getUnlocalizedName());
	}
	
	public String getLocalized(String lang){
		return Unsaga.translation.getLocalized(this.getUnlocalizedName(), lang);
	}
	
	public UnsagaMaterial getParentMaterial(){
		Optional<UnsagaMaterial> mat = Optional.absent();
		if(this.parentMaterial!=null){
			mat = Optional.of(this.parentMaterial);
		}
		return mat.get();
	}
	
	public UnsagaMaterial getRandomSubMaterial(Random rand){
		if(this.hasSubMaterials()){
			List<UnsagaMaterial> mlist = new ArrayList(this.subMaterialMap.values());
			return mlist.get(rand.nextInt(mlist.size()));
		}
		return this;
	}
	
	public Optional<Integer> getRenderColor() {
		if(this.renderColor.isPresent()){
			return this.renderColor;
		}
		if(this.isChild){
			if(this.parentMaterial.getRenderColor().isPresent()){
				return this.parentMaterial.getRenderColor();
			}
		}
		return Optional.absent();
	}
	
	public Optional<String> getSpecialArmorTexture(ToolCategory category,int par1){
		if(this.specialArmorTextureMap!=null){
			if(this.specialArmorTextureMap.get(category)!=null){
				return Optional.of(this.specialArmorTextureMap.get(category).get(par1));
			}
		}
		if(this.isChild && this.getParentMaterial().specialArmorTextureMap!=null){
			return this.getParentMaterial().getSpecialArmorTexture(category, par1);
		}
		return Optional.absent();
	}
	
	public Optional<String> getSpecialIcon(ToolCategory category){
		if(this.specialIconMap!=null){
			if(this.specialIconMap.get(category)!=null){
				return Optional.of(this.specialIconMap.get(category));
			}
		}
		if(this.isChild){
			return this.getParentMaterial().getSpecialIcon(category);
		}
		return Optional.absent();
	}
	
	public Optional<String> getSpecialName(ToolCategory category,int en_or_jp){
		if(specialNameMap!=null){
			if(specialNameMap.get(category)!=null){
				String[] names = specialNameMap.get(category).split(",");
				if(names.length!=2)return Optional.absent();
				return Optional.of(names[en_or_jp]);
			}
		}
		return Optional.absent();
	}
	
	public Optional<UnsagaMaterial> getSubMaterial(String key){
		Optional<UnsagaMaterial> material = Optional.absent();
		if(this.hasSubMaterials()){
			material = Optional.of(this.subMaterialMap.get(key));
			return material;
		}
		return material;
	}
	

	public float getSuitedModifier(ToolCategory category){
		if(this.suitedMap.containsKey(category)){
			return this.suitedMap.get(category);
		}
		return 0.0F;
	}
	public Map<String,UnsagaMaterial> getSubMaterials(){
		return this.subMaterialMap;
	}
	
	public ToolMaterial getToolMaterial(){
		if(this.enumTool.isPresent()){
			return this.enumTool.get();
		}
		if(this.isChild){
			return this.getParentMaterial().getToolMaterial();
		}
		Unsaga.debug("this Material has no EnumToolMAterial:"+this.name);
		return ToolMaterial.STONE;
	}
	
	public String getUnlocalizedName(){
		return "material."+this.name;
	}
	
	public boolean hasSubMaterials(){
		if(this.subMaterialMap.isEmpty()){
			return false;
		}
		return true;
	}
	
	public UnsagaMaterial setArmorMaterial(ArmorMaterial par1){
		this.enumArmor = Optional.of(par1);
		return this;
	}
	
	public void setAttackModifier(float par1){
		this.attackModifier = Optional.of(par1);
	}

	public UnsagaMaterial setBowModifier(int par1){
		this.bowModifier = Optional.of(par1);
		return this;
	}
	
	public UnsagaMaterial addSuitedModifier(ToolCategory cate,float par1){
		this.suitedMap.put(cate, par1);
		return this;
	}
	
	public UnsagaMaterial setIconKey(String key){
		this.iconname = key;
		return this;
	}
	
	public UnsagaMaterial setRenderColor(int par1){
		this.renderColor = Optional.of(par1);
		return this;
	}
	
	public UnsagaMaterial setSpecialArmorTexture(ToolCategory category,String par1,String par2){
		if(this.specialArmorTextureMap==null){
			this.specialArmorTextureMap = new HashMap();
		}
		List<String> texturePair = new ArrayList();
		texturePair.add(par1);
		texturePair.add(par2);
		this.specialArmorTextureMap.put(category, texturePair);
		return this;
	}
	
	public UnsagaMaterial setSpecialIcon(ToolCategory helmet,String name){
		if(specialIconMap==null)this.specialIconMap = new HashMap();
		this.specialIconMap.put(helmet, name);
		return this;
	}
	
	public UnsagaMaterial setSpecialName(ToolCategory category,String name){
		if(specialNameMap==null)this.specialNameMap = new HashMap();
		this.specialNameMap.put(category, name);
		return this;
	}
	
	public UnsagaMaterial setToolMaterial(ToolMaterial par1){
		this.enumTool = Optional.of(par1);
		return this;
	}
	
	public boolean isChildOwnedBy(UnsagaMaterial uns){
		if(uns.hasSubMaterials()){
			for(UnsagaMaterial u:uns.getSubMaterials().values()){
				if(u==this){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean setContainsThis(Set<UnsagaMaterial> set){
		for(UnsagaMaterial u:set){
			if(this.isChildOwnedBy(u)){
				return true;
			}
			if(this==u){
				return true;
			}
		}
		return false;
	}
	

	@Override
	public String toString(){
		return this.name;
	}
}
