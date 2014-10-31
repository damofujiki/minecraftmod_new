package com.hinasch.unlsaga.init;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.hinasch.lib.HelperInventory;
import com.hinasch.lib.RecipeUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.item.UnsagaEnum;
import com.hinasch.unlsaga.item.misc.ItemSkillBook;

import cpw.mods.fml.common.registry.GameRegistry;

public class UnsagaRecipes {

	public void register(){
		Unsaga.items.pickaxeInitializer.regsiterRecipes(RecipeUtil.Recipes.getPickaxe(), UnsagaEnum.ToolCategory.PICKAXE);
		GameRegistry.addShapelessRecipe(new ItemStack(Unsaga.items.ammo,4), new Object[]{Items.paper,Items.gunpowder,Items.iron_ingot});
		GameRegistry.addRecipe(new RecipeSkillBookCloning());
	}
	
	public static class RecipeSkillBookCloning implements IRecipe{

		protected HelperInventory hi;
		protected final ItemStack isSkillBook = new ItemStack(Unsaga.items.skillBook);
		
		@Override
		public boolean matches(InventoryCrafting invCraft, World world) {
			boolean hasSkillBook = false;
			boolean hasSkillBookWritten = false;
			hi = new HelperInventory(invCraft);
			
			Map<Integer,ItemStack> items = hi.getInvItems(isSkillBook, true);
			if(!items.isEmpty() && items.keySet().size()==2){
				for(ItemStack is:items.values()){
					if(ItemSkillBook.readSkill(is).isPresent()){
						hasSkillBookWritten = true;
					}else{
						hasSkillBook = true;
					}
				}
			}

//			for(int i=0;i<invCraft.getSizeInventory();i++){
//				if(invCraft.getStackInSlot(i)!=null && invCraft.getStackInSlot(i).getItem() instanceof ItemSkillBook){
//					ItemStack book = invCraft.getStackInSlot(i);
//					if(ItemSkillBook.readSkill(book).isPresent()){
//						hasSkillBookWritten = true;
//					}else{
//						hasSkillBook = true;
//					}
//				}
//			}
			if(hasSkillBook && hasSkillBookWritten){
				return true;
			}
			return false;
		}

		@Override
		public ItemStack getCraftingResult(InventoryCrafting invCraft) {
			Map<Integer,ItemStack> items = hi.getInvItems(isSkillBook, true);
			if(!items.isEmpty()){
				for(ItemStack is:items.values()){
					if(ItemSkillBook.readSkill(is).isPresent()){
						Skill skill = (Skill) ItemSkillBook.readSkill(is).get();
						ItemStack newStack = isSkillBook.copy();
						ItemSkillBook.writeSkill(newStack,skill);
						newStack.stackSize = 2;
						return newStack;
					}
				}
			}
			
			
//			for(int i=0;i<invCraft.getSizeInventory();i++){
//				if(invCraft.getStackInSlot(i)!=null && invCraft.getStackInSlot(i).getItem() instanceof ItemSkillBook){
//					ItemStack book = invCraft.getStackInSlot(i);
//					if(ItemSkillBook.readSkill(book).isPresent()){
//					
//
//					}
//				}
//			}
			return null;
		}

		@Override
		public int getRecipeSize() {
			// TODO 自動生成されたメソッド・スタブ
			return 9;
		}

		@Override
		public ItemStack getRecipeOutput() {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
		
	}
}
