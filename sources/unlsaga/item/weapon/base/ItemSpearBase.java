package com.hinasch.unlsaga.item.weapon.base;


import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.item.IExtendedReach;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;

public class ItemSpearBase extends ItemSword implements IUnsagaMaterialTool,IExtendedReach,IAbility {

	public final UnsagaMaterial unsMaterial;
	protected final HashMap<String,Icon> iconMap = new HashMap();

	protected IIcon[] icons;

	protected final float weaponDamage;


	public ItemSpearBase(UnsagaMaterial material) {
		super(material.getToolMaterial());
		this.unsMaterial = material;
		this.weaponDamage = 3.0F + material.getToolMaterial().getDamageVsEntity();
		this.setMaxDamage((int)((float)material.getToolMaterial().getMaxUses()*0.8F));
		this.icons = new IIcon[2];
		this.helper.init(unsMaterial, this.itemIcon, getCategory());
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;

	}

	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
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
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return helper.getColorFromItemStack(par1ItemStack, par2);
	}
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"spear");
		this.icons[0] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"spear_1");
		this.icons[1] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"spear_2");
	}


	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack!=null){
			if(par3Entity instanceof EntityPlayer){
				EntityPlayer ep = (EntityPlayer)par3Entity;
				if(ep.swingProgressInt==1){
					ItemStack is = ep.getHeldItem();
					if((is!=null) && (is.getItem() instanceof IExtendedReach)){

						this.doSpearAttack(is, ep, par2World);
					}


					//UtilSkill.setFreeStateNBT(par1ItemStack, "attack", 0);
				}
			}

		}

	}

	protected void doSpearAttack(ItemStack is,EntityPlayer ep,World par2World){
		float reach = ((IExtendedReach) is.getItem()).getReach();
		MovingObjectPosition mop = com.hinasch.lib.ClientHelper.getMouseOverLongReach();
		Unsaga.debug(mop);
		if(mop!=null){
			if(mop.entityHit!=null){
				Unsaga.debug(mop);
				float dis = ep.getDistanceToEntity(mop.entityHit);
				if(dis<reach){

					if(mop.entityHit.hurtResistantTime==0 ){
						AxisAlignedBB ab = mop.entityHit.boundingBox;
						List<Entity> list = par2World.getEntitiesWithinAABB(Entity.class, ab);

						if(!list.isEmpty()){

							Entity hurtEnt = list.get(0);
							//ep.attackTargetEntityWithCurrentItem(hurtEnt);
							if(hurtEnt!=ep){

								ep.attackTargetEntityWithCurrentItem(hurtEnt);
							}
						}

					}
				}
			}
		}
	}
	@Override
	public float getReach() {
		// TODO Auto-generated method stub
		return 8.0F;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.SPEAR;
	}


	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unsMaterial;
	}

	//thanx:http://forum.minecraftuser.jp/viewtopic.php?f=21&t=9494&start=300
	@Override
	public Multimap getItemAttributeModifiers()
	{
		return helper.getItemAttributeModifiers(field_111210_e, this.weaponDamage);
	}


}
