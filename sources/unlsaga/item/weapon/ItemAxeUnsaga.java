package com.hinasch.unlsaga.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hinasch.lib.Statics;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.ability.skill.effect.SkillMelee;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingState;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateFlyingAxe;
import com.hinasch.unlsaga.entity.projectile.EntityFlyingAxeNew;
import com.hinasch.unlsaga.item.weapon.base.ItemAxeBase;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemAxeUnsaga extends ItemAxeBase{

	
	public ItemAxeUnsaga(UnsagaMaterial mat) {
		super(mat);
		//Unsaga.proxy.registerSpecialRenderer(this);
		this.helper.registerItem(this);
		
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{

		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack)){

			return EnumAction.bow;
		}
		return EnumAction.none;
	}


	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack)){
			return 72000;
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if(this.unsMaterial==Unsaga.materialManager.failed){
			UnsagaMaterial unsuited = Unsaga.items.getRandomMaterial(Unsaga.items.getUnsuitedMaterials(this.getCategory()), this.itemRand);
			HelperUnsagaItem.initWeapon(par1ItemStack, unsuited.name, unsuited.weight);
		}
		if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack)){
			Unsaga.debug("トマホーク覚えてる");
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.RIGHTCLICK, par1ItemStack, par3EntityPlayer, par2World, XYZPos.entityPosToXYZ(par3EntityPlayer));
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par2World, par3EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			if(helper!=null){
				helper.doSkill();
			}
		}

		
		
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.skyDrive, par1ItemStack) && !par3EntityPlayer.onGround
//				&& !LivingDebuff.isCooling(par3EntityPlayer)){
//			if(!LivingDebuff.hasDebuff(par3EntityPlayer, Debuffs.flyingAxe)){
//				this.setReadyToSkyDrive(par3EntityPlayer);
//			}
//			if(LivingDebuff.hasDebuff(par3EntityPlayer, Debuffs.flyingAxe) && par3EntityPlayer.isSneaking()){
//				LockOnHelper.searchEntityNear(par3EntityPlayer, Debuffs.weaponTarget);
//				ItemStack copyaxe = par1ItemStack.copy();
//				InvokeSkill helper = new InvokeSkill(par2World,par3EntityPlayer,AbilityRegistry.skyDrive,copyaxe);
//				EntityLivingBase target = null;
//				
//				if(LivingDebuff.getLivingDebuff(par3EntityPlayer, Debuffs.weaponTarget).isPresent()){
//					LivingStateTarget state = (LivingStateTarget)LivingDebuff.getLivingDebuff(par3EntityPlayer, Debuffs.weaponTarget).get();
//					target = (EntityLivingBase) par2World.getEntityByID(state.targetid);
//				}
//				if(target!=null){
//					helper.setTarget(target);
//				}
//				--par1ItemStack.stackSize;
//				helper.doSkill();
//			}
//
//
//		}


		return par1ItemStack;
	}
	

    
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if(par1ItemStack==null){
			return false;
		}

//		HelperAbility abHelper  = new HelperAbility(par1ItemStack, par2EntityPlayer);
//		SkillMelee pickedSkillEffect = null;
//		if(abHelper.getGainedAbilities().isPresent()){
//			for(Ability ability:abHelper.getGainedAbilities().get()){
//				if(ability instanceof Skill){
//					Skill skill = (Skill)ability;
//					if(skill.getSkillEffect() instanceof SkillMelee){
//						SkillMelee effect = (SkillMelee) skill.getSkillEffect();
//						if(effect.getType()==SkillMelee.Type.USE && effect.canInvoke(par3World, par2EntityPlayer, par1ItemStack, new XYZPos(par4,par5,par6))){
//							pickedSkillEffect = effect;
//						}
//					}
//					
//					
//				}
//			}
//		}
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.USE, par1ItemStack, par2EntityPlayer, par3World, new XYZPos(par4,par5,par6));
		if(pickedSkillEffect!=null){
			InvokeSkill helper = new InvokeSkill(par3World, par2EntityPlayer, pickedSkillEffect.getSkill(), par1ItemStack);
			helper.setUsePoint(new XYZPos(par4,par5,par6));
			if(helper!=null){
				helper.doSkill();
			}
		}
//		InvokeSkill helper = null;
//		boolean requirePrepare = false;
//		Set<SkillMelee> skillHashSet = new HashSet(SkillAxe.skillAxeSet);
//		SkillMelee pickedSkill = null;
//		for(SkillMelee skillMelee:skillHashSet){
//			Unsaga.debug("Skill Melee:"+skillMelee.getSkill()+" Client:"+par3World.isRemote);
//			
//			if(skillMelee!=null && skillMelee.getType()==SkillMelee.Type.USE && skillMelee.canInvoke(par3World, par2EntityPlayer, par1ItemStack, new XYZPos(par4,par5,par6))){
//
//				pickedSkill = skillMelee;
//				
//			}
//		}
//		Unsaga.debug(pickedSkill);
//		Unsaga.debug(pickedSkill.getSkill());
//		if(pickedSkill!=null){
//			helper = new InvokeSkill(par3World,par2EntityPlayer,pickedSkill.getSkill(),par1ItemStack);
//
//			if(helper!=null){
//				helper.setUsePoint(new XYZPos(par4,par5,par6));
//				if(pickedSkill.isRequirePrepare()){
//					helper.prepareSkill();
//				}else{
//					helper.doSkill();
//				}
//			}
//		}

		
//		if(par2EntityPlayer.isSneaking() && HelperAbility.hasAbilityFromItemStack(AbilityRegistry.woodChopper, par1ItemStack)){
//			par2EntityPlayer.swingItem();
//			InvokeSkill helper = new InvokeSkill(par3World,par2EntityPlayer,AbilityRegistry.woodChopper,par1ItemStack);
//			helper.setUsePoint(new XYZPos(par4,par5,par6));
//			helper.doSkill();
//
//
//		}
//		
//		if(par2EntityPlayer.isSneaking() && !par2EntityPlayer.onGround && HelperAbility.hasAbilityFromItemStack(AbilityRegistry.woodBreakerPhoenix, par1ItemStack)){
//			par2EntityPlayer.swingItem();
//			PacketSkill ps = new PacketSkill(PacketSkill.PACKETID.WOODBREAKER,new XYZPos(par4,par5,par6));
//			Unsaga.packetPipeline.sendToServer(ps);
//			
//		}


		return false;
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		//HelperSkill helper = new HelperSkill(stack,player);
//		if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.fujiView, stack) && player.isSneaking() && !LivingDebuff.hasDebuff(player, Debuffs.cooling)){
//			SkillAxe fujiView = new SkillAxe();
//			if(!player.worldObj.isRemote && entity instanceof EntityLivingBase){
//				InvokeSkill helper = new InvokeSkill(player.worldObj,player,AbilityRegistry.fujiView,stack);
//				helper.setTarget((EntityLivingBase) entity);
//				helper.doSkill();
//			}
//
//
//		}
		
		
		SkillMelee pickedSkillEffect = HelperUnsagaItem.getSkillMelee(SkillMelee.Type.ENTITY_LEFTCLICK, stack, player, player.worldObj, XYZPos.entityPosToXYZ(player));
		if(pickedSkillEffect!=null && entity instanceof EntityLivingBase){
			InvokeSkill helper = new InvokeSkill(player.worldObj, player, pickedSkillEffect.getSkill(), stack);
			helper.setTarget((EntityLivingBase) entity);
			if(helper!=null){
				if(pickedSkillEffect.isRequirePrepare()){
					helper.prepareSkill();
				}else{
					helper.doSkill();
				}
			}
		}
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	{
		int j = this.getMaxItemUseDuration(par1ItemStack) - par4;

		if(par1ItemStack==null)return;

		if (HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tomahawk, par1ItemStack))
		{
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if (f < 0.1D)
			{
				return;
			}

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			par1ItemStack.damageItem(Unsaga.abilityManager.tomahawk.getDamageForWeapon(), par3EntityPlayer);
			EntityFlyingAxeNew entityflyingaxe = new EntityFlyingAxeNew(par2World, par3EntityPlayer, f*1.0F,par1ItemStack,false);
			entityflyingaxe.setDamage((float) par3EntityPlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());

			par2World.playSoundAtEntity(par3EntityPlayer, Statics.soundBow, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);



			if (!par2World.isRemote)
			{
				
				if(entityflyingaxe.getAxeItemStack()!=null){
					par2World.spawnEntityInWorld(entityflyingaxe);
					ItemStack aitemstack = null;
					par3EntityPlayer.inventory.mainInventory[par3EntityPlayer.inventory.currentItem] = aitemstack;
				}

			}


			//par3EntityPlayer.inventory.consumeInventoryItem(par3EntityPlayer.inventory.currentItem);



		}
	}



	protected void setReadyToSkyDrive(EntityPlayer ep){
		ep.motionY += 1.0;
		LivingDebuff.addLivingDebuff(ep, new LivingStateFlyingAxe(Unsaga.debuffManager.flyingAxe,30,(int)this.unsMaterial.getToolMaterial().getDamageVsEntity()));
		LivingDebuff.addLivingDebuff(ep, new LivingState(Unsaga.debuffManager.antiFallDamage,30,true));
	}
}
