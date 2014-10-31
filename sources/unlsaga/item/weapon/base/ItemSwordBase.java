package com.hinasch.unlsaga.item.weapon.base;


import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;

import com.google.common.collect.Multimap;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemSwordBase extends ItemSword implements IUnsagaMaterialTool,IAbility{


			

	protected final HashMap<String,Icon> iconMap = new HashMap();
	protected final IIcon[] icons;
	public final UnsagaMaterial unsMaterial;
	public final float weapondamage;
	
	public ItemSwordBase(UnsagaMaterial mat) {
		super(mat.getToolMaterial());
		this.weapondamage = 4.0F + mat.getToolMaterial().getDamageVsEntity();
		this.icons = new IIcon[2];
		this.unsMaterial = mat;
		this.helper.init(unsMaterial, itemIcon, getCategory());

	}

        
    
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.SWORD;
	}
    
	@Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
		return helper.getColorFromItemStack(par1ItemStack, par2);
    }

    
	@Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
		if(par2==0){
			return this.icons[0];
		}
        return this.icons[1];
    }
    
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
    //thanx:http://forum.minecraftuser.jp/viewtopic.php?f=21&t=9494&start=300
	@Override
    public Multimap getItemAttributeModifiers()
    {
        return helper.getItemAttributeModifiers(field_111210_e, weapondamage);
    }
    
    
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	if(HelperUnsagaItem.getCurrentWeight(par1ItemStack)>5){
    		return EnumAction.none;
    	}
        return EnumAction.block;
    }


	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unsMaterial;
	}


	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
    	//this.itemIcon = par1IconRegister.registerIcon(Unsaga.domain+":"+"sword_"+this.unsMaterial.iconname);

    	this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"sword");
    	this.icons[0] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"sword_1");
    	this.icons[1] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"sword_2");
    	//this.itemIcon = HelperUnsagaWeapon.registerIcons(par1IconRegister, unsMaterial, "sword");
//    	if(this.unsMaterial.hasSubMaterials()){
//    		for(Iterator<UnsagaMaterial> ite=unsMaterial.getSubMaterials().values().iterator();ite.hasNext();){
//    			
//    			UnsagaMaterial childMat = ite.next();
//    			this.iconMap.put(childMat.name, par1IconRegister.registerIcon(Unsaga.domain+":"+"sword_"+childMat.iconname));
//    		}
//    	}else{
//    		this.itemIcon = par1IconRegister.registerIcon(Unsaga.domain+":"+"sword_"+this.unsMaterial.iconname);
//    	}
//    	

    	
    }

	@Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
