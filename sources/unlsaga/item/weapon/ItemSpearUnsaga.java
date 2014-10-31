package com.hinasch.unlsaga.item.weapon;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hinasch.lib.UtilNBT;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.ability.skill.effect.SkillMelee;
import com.hinasch.unlsaga.item.weapon.base.ItemSpearBase;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemSpearUnsaga extends ItemSpearBase {

	protected final static String KEYisAiming = "unsaga.isAiming";
	protected final int SETAIMING = 0x01;
	protected final int NEUTRAL = 0x00;

	public ItemSpearUnsaga(UnsagaMaterial material) {
		super(material);
		Unsaga.proxy.registerSpearRenderer(this);
		this.helper.registerItem(this);

	}



	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.acupuncture, par1ItemStack)){
			return 160000;
		}
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		if(this.isAiming(par1ItemStack)){
			return EnumAction.bow;
		}

		return EnumAction.none;

	}

	public boolean isAiming(ItemStack par1ItemStack){
		if(UtilNBT.hasKey(par1ItemStack, KEYisAiming)){
			return UtilNBT.readFreeTagBool(par1ItemStack, KEYisAiming);
		}
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	{
		int ac = 20;
		int j = this.getMaxItemUseDuration(par1ItemStack) - par4;
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.STOPPED_USING, par1ItemStack, par3EntityPlayer, par2World, XYZPos.entityPosToXYZ(par3EntityPlayer));
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			if(helper!=null){
				helper.setCharge(j);
				helper.doSkill();
				
			}
		}
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.aiming, par1ItemStack)){
//			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer,AbilityRegistry.aiming , par1ItemStack);
//			helper.setCharge(j);
//			helper.doSkill();
//		}
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.acupuncture, par1ItemStack)){
//			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer,AbilityRegistry.acupuncture , par1ItemStack);
//			helper.setCharge(j);
//			helper.doSkill();
//		}
	}


	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{

		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.RIGHTCLICK, par1ItemStack, par3EntityPlayer, par2World, XYZPos.entityPosToXYZ(par3EntityPlayer));
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			if(helper!=null){
				helper.doSkill();
				
			}
		}
//
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.swing, par1ItemStack) && par3EntityPlayer.isSneaking()){
//			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer, AbilityRegistry.swing, par1ItemStack);
//			helper.doSkill();
//		}
		if (HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.aiming, par1ItemStack) && par3EntityPlayer.isSneaking())
		{
			UtilNBT.setFreeTag(par1ItemStack, KEYisAiming, true);

		}
		if (HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.acupuncture, par1ItemStack) && par3EntityPlayer.isSneaking())
		{
			UtilNBT.setFreeTag(par1ItemStack, KEYisAiming, true);

		}
		super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);

		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.USE, par1ItemStack, par2EntityPlayer, par3World, new XYZPos(par4,par5,par6));
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par3World, par2EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			if(helper!=null){
				helper.setUsePoint(new XYZPos(par4,par5,par6));
				helper.doSkill();
				
			}
		}
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.grassHopper, par1ItemStack)){
//			InvokeSkill helper = new InvokeSkill(par3World,par2EntityPlayer,AbilityRegistry.grassHopper,par1ItemStack);
//
//			helper.setUsePoint(new XYZPos(par4,par5,par6));
//			helper.doSkill();
//
//		}
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
		
		if(par1ItemStack!=null){
			if(par3Entity instanceof EntityPlayer){
				EntityPlayer ep = (EntityPlayer)par3Entity;
				
					ItemStack is = ep.getHeldItem();

					if(is!=null && !this.isAiming(is)){
						this.setNeutral(par1ItemStack);
					}

					
				
			}

		}
		
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}




	public static void setNeutral(ItemStack is){
		UtilNBT.setFreeTag(is, KEYisAiming, false);
	}

	public static boolean isNeutral(ItemStack is){
		if(!UtilNBT.hasKey(is, KEYisAiming)){
			return false;
		}
		return UtilNBT.readFreeTagBool(is, KEYisAiming);
	}



}
