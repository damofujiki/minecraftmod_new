package com.hinasch.unlsaga.item.weapon;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.ability.skill.effect.SkillMelee;
import com.hinasch.unlsaga.ability.skill.effect.SkillSword;
import com.hinasch.unlsaga.ability.skill.effect.SkillSword.SkillGust;
import com.hinasch.unlsaga.item.weapon.base.ItemSwordBase;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemSwordUnsaga extends ItemSwordBase{


	public static final String GUSTKEY = "unsaga.gust";
	public static SkillGust skillGust = (SkillGust) SkillSword.getInstance().gust;
	
	public ItemSwordUnsaga(UnsagaMaterial mat) {
		super(mat);
		//Unsaga.proxy.registerSpecialRenderer(this);
		this.helper.registerItem(this);

	}

	@Override
	public void onPlayerStoppedUsing(ItemStack is, World world, EntityPlayer ep, int par4)
	{
		int j = this.getMaxItemUseDuration(is) - par4;
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.STOPPED_USING, is, ep, world, null);
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(world, ep, pickedSkillEffect.getSkill(), is);
			if(helper!=null){
				helper.setCharge(j);
				helper.doSkill();
				
			}
		}
	}
	

	public static void hitExplodeByVandalize(LivingHurtEvent e){
		if(e.source.isExplosion()){
			if(e.source.getEntity() instanceof EntityLivingBase){
				EntityLivingBase el = (EntityLivingBase)e.source.getEntity();
				if(el.getHeldItem()!=null){
					if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.vandalize, el.getHeldItem())){
						e.ammount += Unsaga.abilityManager.vandalize.getStrHurtHP();
						//Unsaga.lpHandler.tryHurtLP(e.entityLiving, Unsaga.abilityManager.vandalize.getStrHurtLP());
						//Unsaga.debug("ヴァンダライズ炸裂！");
					}
				}

			}
		}
	}
	
	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.ENTITY_LEFTCLICK, stack, player, player.worldObj, null);
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(player.worldObj, player, pickedSkillEffect.getSkill(), stack);
			if(helper!=null){
				helper.setTarget((EntityLivingBase) entity);
				helper.doSkill();
				
			}
			return true;
		}
        return false;
    }

    
    
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.smash, par1ItemStack)){
    		return EnumAction.bow;
    	}
    	if(skillGust.isGust(par1ItemStack)){
    		return EnumAction.bow;
    	}
    	return super.getItemUseAction(par1ItemStack);
    }
    
    
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.RIGHTCLICK, par1ItemStack, par3EntityPlayer, par2World, null);
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			if(helper!=null){
				helper.doSkill();
				
			}
		}
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.gust,par1ItemStack) && par3EntityPlayer.isSneaking()){
			
			skillGust.setGust(par1ItemStack, true);
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.smash,par1ItemStack)){
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}
		return par1ItemStack;
	}
    


	
	@Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	if(par3Entity instanceof EntityPlayer){
    		EntityPlayer ep = (EntityPlayer)par3Entity;
    		if(skillGust.isGust(par1ItemStack) && !ep.isSneaking()){
    			skillGust.setGust(par1ItemStack, false);
    		}
    	}
    }

}
