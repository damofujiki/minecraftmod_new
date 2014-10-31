package com.hinasch.unlsaga.item.armor;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.init.UnsagaItems;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemAccessory extends Item implements IAbility,IUnsagaMaterialTool{

	protected UnsagaMaterial material;
	protected String defaultIcon;
	protected HelperUnsagaItem helper;
	
	public ItemAccessory(UnsagaMaterial par2) {
		super();
		this.material = par2;
        this.maxStackSize = 1;
		this.setMaxDamage(par2.getToolMaterial().getMaxUses());
		this.defaultIcon = "ring";
		this.helper = new HelperUnsagaItem(this.material,this.itemIcon, ToolCategory.ACCESSORY);
		// TODO 自動生成されたコンストラクター・スタブ
		Unsaga.items.putItemToMap(this,UnsagaItems.Key.makeKey(this.getCategory(), par2));
	}

	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
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
	public void registerIcons(IIconRegister par1IconRegister)
	{
		if(this.material.getSpecialIcon(ToolCategory.ACCESSORY).isPresent()){
			this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+this.material.getSpecialIcon(ToolCategory.ACCESSORY).get());
			return;
		}
		this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+this.defaultIcon);
	}
	
//    @Override
//    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//    	helper.getSubItems(par1, par2CreativeTabs, par3List);
//        
//    }

	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.ACCESSORY;
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {

        if(par3EntityPlayer.isSneaking()){
	    	  EntityClientPlayerMP clientPlayer = (EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer;

	    	  if(Minecraft.getMinecraft().currentScreen !=null)return par1ItemStack;
	    	  PacketGuiOpen pg = new PacketGuiOpen(Unsaga.guiNumber.EQUIP);
	    	  Unsaga.packetPipeline.sendToServer(pg);
        }else{
        	if(ExtendedPlayerData.getData(par3EntityPlayer).getEmptyAccessorySlot().isPresent()){
        		int slot = ExtendedPlayerData.getData(par3EntityPlayer).getEmptyAccessorySlot().get();
        		ExtendedPlayerData.getData(par3EntityPlayer).setAccessory(slot, par1ItemStack.copy());
        		par1ItemStack.stackSize --;
        	}
        }

        return par1ItemStack;
    }

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.material;
	}
}
