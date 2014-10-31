package com.hinasch.unlsaga.item.weapon.base;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBowBase extends ItemBow implements IUnsagaMaterialTool,IAbility
{
    public static final String[] bowPullIconNameArray = new String[] {"pull0", "pull1", "pull2"};
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;
    protected UnsagaMaterial material;

    public ItemBowBase(UnsagaMaterial material)
    {
        super();
        this.maxStackSize = 1;
        this.material = material;
        this.helper.init(material, itemIcon, getCategory());
    }
    
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
    	return helper.getColorFromItemStack(par1ItemStack, par2);
    }
    
    
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (usingItem != null && usingItem.getItem() == this)
        {
            int j = stack.getMaxItemUseDuration() - useRemaining;

            if (j >= 18)
            {
                return this.iconArray[2];
            }

            if (j > 13)
            {
                return this.iconArray[1];
            }

            if (j > 0)
            {
                return this.iconArray[0];
            }
        }
        
        return this.getIcon(stack, renderPass);
    }
    
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
//    @Override
//    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//    	helper.getSubItems(par1, par2CreativeTabs, par3List);
//        
//    }
    
    public int getStrengthModifier(ItemStack is){
    	UnsagaMaterial material = HelperUnsagaItem.getMaterial(is);
    	if(material==Unsaga.materialManager.dummy){
    		return 0;
    	}
    	return material.getBowModifier();
    }



    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
        {
            return event.result;
        }

        if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(Items.arrow))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }



    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"bow_white_standby");
        this.iconArray = new IIcon[bowPullIconNameArray.length];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"bow_white_" + bowPullIconNameArray[i]);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * used to cycle through icons based on their used duration, i.e. for the bow
     */
    public IIcon getItemIconForUseDuration(int par1)
    {
        return this.iconArray[par1];
    }
    
	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.BOW;
	}


	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.material;
	}
}
