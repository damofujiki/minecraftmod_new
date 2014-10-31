package com.hinasch.unlsaga.util;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hinasch.lib.UtilNBT;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.AbilityRegistry;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.ability.skill.effect.SkillMelee;
import com.hinasch.unlsaga.init.UnsagaItems;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.armor.ItemAccessory;
import com.hinasch.unlsaga.item.weapon.ItemBowUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemStaffUnsaga;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;

public class HelperUnsagaItem {
	
	protected UnsagaMaterial materialItem;
	protected IIcon itemIcon;
	//protected HashMap<String,Icon> iconMap;
	protected AbilityRegistry abilities = Unsaga.abilityManager;
	protected ToolCategory category;
	
	public HelperUnsagaItem(){
		
	}
	public HelperUnsagaItem(UnsagaMaterial materialItem,IIcon itemIcon,ToolCategory category){
		this.abilities = Unsaga.abilityManager;
		this.materialItem = materialItem;
		this.category = category;
		this.itemIcon = itemIcon;
	}

	public void init(UnsagaMaterial materialItem,IIcon itemIcon,ToolCategory category){
		this.abilities = Unsaga.abilityManager;
		this.materialItem = materialItem;
		this.category = category;
		this.itemIcon = itemIcon;
	}
	
    public static int getCurrentWeight(ItemStack is){
    	if(UtilNBT.hasKey(is, "weight")){
    		return UtilNBT.readFreeTag(is, "weight");
    	}
    	return getMaterial(is).weight;
    }
    

    public static boolean isHeavy(ItemStack weapon){
		if(HelperUnsagaItem.getCurrentWeight(weapon)>5){
			return true;
		}
		return false;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		MaterialInfo info = new MaterialInfo(par2ItemStack);
		if(!info.getMaterial().isPresent()){
			return false;
		}

        return getMaterial(par1ItemStack) == info.getMaterial().get() ? true : false;
    }
	
    public int getColorFromItemStack(ItemStack par1ItemStack, int pass)
    {
    	boolean multipass = true;
    	if(par1ItemStack.getItem() instanceof ItemBowUnsaga){
    		multipass = false;
    	}
    	if(par1ItemStack.getItem() instanceof ItemStaffUnsaga){
    		UnsagaMaterial mate = getMaterial(par1ItemStack);
    		if(mate.isChild){
    			if(mate.getParentMaterial()==Unsaga.materialManager.categorywood){
    				multipass = false;
    			}
    		}
    		
    	}
    	if(par1ItemStack.getItem() instanceof ItemAccessory){
    		multipass = false;
    	}
    	
        if((multipass && pass==0)||(!multipass)){
        	if(HelperUnsagaItem.getMaterial(par1ItemStack).getRenderColor().isPresent()){
        		return HelperUnsagaItem.getMaterial(par1ItemStack).getRenderColor().get();
        	}
        }
        return 0xFFFFFF;
    }
	
    public void getSubItems(Item par1, CreativeTabs tab, List subItemList)
    {
    	if(materialItem.hasSubMaterials()){
    		
    		for(Iterator<UnsagaMaterial> ite=materialItem.getSubMaterials().values().iterator();ite.hasNext();){
    			ItemStack is = new ItemStack(par1,1,0);
    			UnsagaMaterial childMaterial = ite.next();
    			initWeapon(is,childMaterial.getName(),childMaterial.getWeight());
    			
    			subItemList.add(is);
    		}
    	}else{
    		
    		ItemStack is = new ItemStack(par1,1,0);
			initWeapon(is,materialItem.getName(),materialItem.getWeight());
			
    		subItemList.add(is);
    	}
        
    }
    

    public static UnsagaMaterial getSubMaterial(ItemStack is){
    	return UtilNBT.hasKey(is, "material")?Unsaga.materialManager.getMaterial(UtilNBT.readFreeStrTag(is, "material")):null;
    }


    //getMaterial(From ItemStack) if material is MaterialList.failed ,returns Material from NBT.
    public static UnsagaMaterial getMaterial(ItemStack is){
    	if(is.getItem() instanceof IUnsagaMaterialTool){
    		IUnsagaMaterialTool iu = (IUnsagaMaterialTool)is.getItem();
    		if(iu.getMaterial()==Unsaga.materialManager.failed){
    			if(getSubMaterial(is)!=null){
    				return getSubMaterial(is);
    			}else{
    				return Unsaga.materialManager.feather;
    			}
    			
    		}
    		return ((IUnsagaMaterialTool)is.getItem()).getMaterial();
    	}
    	return Unsaga.materialManager.dummy;
//    	if(UtilNBT.hasKey(is, "material")){
//    		return MaterialList.getMaterial(UtilNBT.readFreeStrTag(is, "material"));
//    	}
////    	if(is.getItem() instanceof IUnsagaWeapon){
////    		IUnsagaWeapon iu = (IUnsagaWeapon)is.getItem();
////    		return iu.unsMaterial;
////    	}
//    	return MaterialList.dummy;
    }
    

//    public static IIcon registerIcons(IIconRegister par1IconRegister,UnsagaMaterial material,String category)
//    {
//
//    	return par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+category+"_"+material.iconname);
//
////    	if(this.unsMaterial.hasSubMaterials()){
////    		for(Iterator<UnsagaMaterial> ite=unsMaterial.getSubMaterials().values().iterator();ite.hasNext();){
////    			
////    			UnsagaMaterial childMat = ite.next();
////    			this.iconMap.put(childMat.name, par1IconRegister.registerIcon(Unsaga.domain+":"+"sword_"+childMat.iconname));
////    		}
////    	}else{
////    		this.itemIcon = par1IconRegister.registerIcon(Unsaga.domain+":"+"sword_"+this.unsMaterial.iconname);
////    	}
////    	
//
//    	
//    }
    
    public void addUnsagaItemInfo(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List infoList, boolean par4) {
    	String lang = Minecraft.getMinecraft().gameSettings.language;
		if(par1ItemStack!=null){
				infoList.add(this.getMaterial(par1ItemStack).getLocalized());

			
			if(Unsaga.debug){
				infoList.add("Weight:"+HelperUnsagaItem.getCurrentWeight(par1ItemStack));
			}
			if(!Unsaga.debug){
				if(HelperUnsagaItem.getCurrentWeight(par1ItemStack)>5){
					infoList.add("W:Heavy");
				}else{
					infoList.add("W:Light");
				}
			}

			displayAbilities(par1ItemStack, par2EntityPlayer, infoList, par4);
		}
    }
    
    public void displayAbilities(ItemStack is, EntityPlayer ep, List dispList, boolean par4){
    	String lang = Minecraft.getMinecraft().gameSettings.language;
    	//最初からついてるアビリティを表示
    	if(abilities.getInheritAbilities(this.category, this.materialItem).isPresent()){
			String str = "";
			for(Iterator<Ability> ite=abilities.getInheritAbilities(this.category, this.materialItem).get().iterator();ite.hasNext();){
				Ability abi = ite.next();
				str = str + abi.getName(lang);
			}
			dispList.add(str);
		}
    	//あとから引き出したアビリティを表示
		if(HelperAbility.canItemStackGainAbility(is)){
			HelperAbility helperab = new HelperAbility(is,ep);
			if(helperab.getGainedAbilities().isPresent()){
				for(Ability abi:helperab.getGainedAbilities().get()){
					dispList.add(abi.getName(lang));
				}
			}
		}
    }
		
    
    

    public static void initWeapon(ItemStack is,String mat,int weight){
    	if(is.getItem() instanceof IUnsagaMaterialTool){
    		IUnsagaMaterialTool iu = (IUnsagaMaterialTool)is.getItem();
    		if(iu.getMaterial()==Unsaga.materialManager.failed){
    			UtilNBT.setFreeTag(is, "material", mat);
    		}
    	}
		
		UtilNBT.setFreeTag(is, "weight", weight);
		return;
    }
    
    
    public static SkillMelee getSkillMelee(SkillMelee.Type type,ItemStack par1ItemStack,EntityPlayer par2EntityPlayer,World par3World,XYZPos pos){
		HelperAbility abHelper  = new HelperAbility(par1ItemStack, par2EntityPlayer);
		SkillMelee pickedSkillEffect = null;
		if(abHelper.getGainedAbilities().isPresent()){
			for(Ability ability:abHelper.getGainedAbilities().get()){
				if(ability instanceof Skill){
					Skill skill = (Skill)ability;
					if(skill.getSkillEffect() instanceof SkillMelee){
						SkillMelee effect = (SkillMelee) skill.getSkillEffect();
						if(effect.getType()==type && effect.canInvoke(par3World, par2EntityPlayer, par1ItemStack, pos)){
							pickedSkillEffect = effect;
						}
					}
					
					
				}
			}
		}
		return pickedSkillEffect;
    }

    public Multimap getItemAttributeModifiers(UUID uuid,float weaponDamage)
    {
        Multimap multimap = HashMultimap.create();//putメソッドを使う際に同じキーの要素は登録されないため．ItemSwordのものを持ってきてはいけない．
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(uuid, "Weapon modifier", (double)weaponDamage, 0));
        return multimap;
    }
    
	public void registerItem(Item item){
		Preconditions.checkNotNull(this.category,"not initialized??");
		Unsaga.items.putItemToMap(item,UnsagaItems.Key.makeKey(this.category, this.materialItem));
	}

}
