package com.hinasch.unlsaga.item.weapon;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.skill.effect.InvokeSkill;
import com.hinasch.unlsaga.ability.skill.effect.SkillBow;
import com.hinasch.unlsaga.ability.skill.effect.SkillBow.SkillBowBase;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateBow;
import com.hinasch.unlsaga.item.weapon.base.ItemBowBase;
import com.hinasch.unlsaga.material.UnsagaMaterial;

public class ItemBowUnsaga extends ItemBowBase
{


	public final static String ARROW_ZAPPER = "zapper";
	public final static String ARROW_EXORCIST = "exorcist";
	public final static String ARROW_STITCH = "stitch";
	public final static String ARROW_PHOENIX = "phoenix";
    public ItemBowUnsaga(UnsagaMaterial material)
    {
        super(material);
		//Unsaga.proxy.registerSpecialRenderer(this);
		this.helper.registerItem(this);
    }


    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer player, int par4)
    {
        int j = this.getMaxItemUseDuration(par1ItemStack) - par4;

        
        InvokeSkill skilleffect = null;
        ArrowLooseEvent event = new ArrowLooseEvent(player, par1ItemStack, j);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
        {
            return;
        }
        j = event.charge;

        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (flag || player.inventory.hasItem(Items.arrow))
        {
            float f = (float)j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double)f < 0.1D)
            {
                return;
            }

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.doubleShot,par1ItemStack ) && player.isSneaking()){
        		Unsaga.debug("呼ばれてますonstopping",this.getClass());
            	LivingDebuff.addLivingDebuff(player, new LivingStateBow(Unsaga.debuffManager.bowDouble,10,false,2,"double", f));
            	return;
            }
            if(HelperAbility.hasAbilityFromItemStack(Unsaga.abilityManager.tripleShot,par1ItemStack ) && player.isSneaking()){
            	LivingDebuff.addLivingDebuff(player, new LivingStateBow(Unsaga.debuffManager.bowDouble,10,false,3,"triple", f));
            	return;
            }

            EntityArrow entityarrow = new EntityArrow(par2World, player, f * 2.0F);

            String addTagKey = "";
            for(String key:SkillBow.getAssociatedMap().keySet()){
            	SkillBowBase skillBow = SkillBow.getSkillAssociatedWithTag(key);
            	if(skillBow!=null){
                    if(HelperAbility.hasAbilityFromItemStack(skillBow.getSkill(),par1ItemStack ) && player.isSneaking()){
                    	skilleffect = new InvokeSkill(par2World, player, skillBow.getSkill(), par1ItemStack);
                    	skilleffect.setCharge(f);
                    }
            	}
 
            }
//            if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.zapper,par1ItemStack ) && player.isSneaking()){
//            	skilleffect = new InvokeSkill(par2World, player, AbilityRegistry.zapper, par1ItemStack);
//            	skilleffect.setCharge(f);
//            }
//            if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.exorcist,par1ItemStack ) && player.isSneaking()){
//            	skilleffect = new InvokeSkill(par2World, player, AbilityRegistry.exorcist, par1ItemStack);
//            	skilleffect.setCharge(f);
//            }
//            if(HelperAbility.hasAbilityFromItemStack(AbilityRegistry.shadowStitching,par1ItemStack ) && player.isSneaking()){
//            	skilleffect = new InvokeSkill(par2World, player, AbilityRegistry.shadowStitching, par1ItemStack);
//            	skilleffect.setCharge(f);
//            }
            if (f == 1.0F)
            {
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            
            k += this.getStrengthModifier(par1ItemStack);

            if (k > 0)
            {
            	if(skilleffect!=null){
            		entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + (double)skilleffect.getAttackDamage());
            	}else{
            		entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
            	}
                
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (l > 0)
            {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                entityarrow.setFire(100);
            }

//            if(skilleffect.getSkill()==AbilityRegistry.shadowStitching){
//            	par1ItemStack.damageItem(skilleffect.getDamageItem(), player);
//            }else{
            	par1ItemStack.damageItem(1, player);
            //}
            
            par2World.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag)
            {
                entityarrow.canBePickedUp = 2;
            }
            else
            {
                player.inventory.consumeInventoryItem(Items.arrow);
            }

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(entityarrow);
                if(skilleffect!=null){
                	if(skilleffect.getEffect() instanceof SkillBowBase){
                		if(skilleffect.doSkill()){
                    		SkillBowBase skillBow = (SkillBowBase) skilleffect.getEffect();
                    		skillBow.postShoot(entityarrow); //ここで技のタグを設定
                		}

                	}
                }
            }
        }
    }


}
