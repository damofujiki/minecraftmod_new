package com.hinasch.unlsaga.item.misc;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;
import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.util.translation.Translation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSkillBook extends Item{

	public static final String KEY_SKILL = "unsaga.skill";
	public ItemSkillBook(){
		this.setTextureName(Unsaga.DOMAIN+":book_written");
		this.setMaxStackSize(64);
	}
	
	public static void writeSkill(ItemStack book,Ability skill){
		UtilNBT.setFreeTag(book, KEY_SKILL, skill.number);
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		if(UtilNBT.hasKey(is, KEY_SKILL)){
			list.add(Unsaga.abilityManager.getAbilityFromInt(UtilNBT.readFreeTag(is, KEY_SKILL)).getName(Translation.getLang()));
		}
	}

	
	public static Optional<Ability> readSkill(ItemStack book){
		if(UtilNBT.hasKey(book, KEY_SKILL)){
			int num = UtilNBT.readFreeTag(book, KEY_SKILL);
			Ability ability = Unsaga.abilityManager.getAbilityFromInt(num);
			return Optional.of(ability);
		}

		return Optional.absent();
		
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs p_150895_2_, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        ItemStack is = new ItemStack(item,1,0);
        ItemSkillBook.writeSkill(is, Unsaga.abilityManager.acupuncture);
        list.add(is);
    }
}
