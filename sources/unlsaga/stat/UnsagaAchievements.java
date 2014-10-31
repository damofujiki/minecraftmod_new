package com.hinasch.unlsaga.stat;

import java.util.ArrayList;
import java.util.List;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsagamagic.item.ItemTablet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class UnsagaAchievements {

	public AchievementPage page;
	public List<Achievement> achieves;
	public Achievement gainSkillFirst;
	public Achievement openInv;
	public Achievement fullArmor;
	public Achievement firstSmith;
	public Achievement firstDecipher;
	public Achievement startBlend;
	public Achievement steel;
	public Achievement damascus;
	
	public UnsagaAchievements(){
		achieves = new ArrayList();

	}

	public void init(){

		this.openInv = new UnsagaAchievement(0,"openInv",2,0,
				Unsaga.items.getItemStack(ToolCategory.ACCESSORY, Unsaga.materialManager.categoryStone, 1, 0),null).registerStat().initIndependentStat();
		this.gainSkillFirst  = new UnsagaAchievement(1,"firstSkill",2,-2,
				Unsaga.items.getItemStack(ToolCategory.SWORD, Unsaga.materialManager.categoryStone, 1, 0), this.openInv).registerStat();
		this.fullArmor = new UnsagaAchievement(2,"fullArmor",0,0,
				Unsaga.items.getItemStack(ToolCategory.ARMOR, Unsaga.materialManager.categoryStone, 1, 0),this.openInv).registerStat();
		this.firstSmith = new UnsagaAchievement(3,"firstSmith",2,2,
				new ItemStack(Blocks.anvil,1),null).registerStat().initIndependentStat();;
		this.firstDecipher = new UnsagaAchievement(4,"firstDecipher",4,0,
				ItemTablet.getDisplayMagicTablet(null),this.openInv).registerStat();
		this.steel = new UnsagaAchievement(5,"steel",2,4,
				Unsaga.noFunctionItems.getItemStack(1, 0),this.firstSmith).registerStat();
		this.damascus = new UnsagaAchievement(6,"damascus",2,6,
				Unsaga.noFunctionItems.getItemStack(1, 4),this.steel).registerStat();
		this.startBlend = new UnsagaAchievement(7,"startBlend",6,0,
				ItemTablet.getDisplayMagicTablet(null),this.firstDecipher).registerStat();
		page = new AchievementPage("Unsaga Mod",this.getAchievesArray(achieves));
		AchievementPage.registerAchievementPage(page);
	}

	protected Achievement[] getAchievesArray(List<Achievement> list){
		Achievement[] array = new Achievement[list.size()];
		int index = 0;
		for(Achievement ac:list ){
			array[index] = ac;
			index += 1;
		}
		return array;
	}
	

	public static class UnsagaAchievement extends Achievement{

		public UnsagaAchievement(int id,String name,
				int x, int y, ItemStack is,
				Achievement parent) {
			super(Unsaga.MODID+"."+String.valueOf(id), "unsaga."+name, x, y, is,
					parent);

			Unsaga.achievements.achieves.add(this);
		}
		
	}
}
