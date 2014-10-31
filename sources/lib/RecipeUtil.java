package com.hinasch.lib;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.common.collect.Lists;
import com.hinasch.compressedblock.CompressedBlocks;
import com.hinasch.lib.RecipeUtil.Recipe.Shaped;
import com.hinasch.lib.RecipeUtil.Recipe.Shapeless;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeUtil {
	public static enum changeable {CHANGEABLE};
	
	public static List<Object> getRequireItemStacksFromRecipe(IRecipe recipe){
		//System.out.println(recipe);
		List<Object> itemsList = null;
		if(recipe instanceof ShapelessRecipes){
			itemsList = ((ShapelessRecipes)recipe).recipeItems;
		}
		if(recipe instanceof ShapedRecipes){
			Object[] isarray = ((ShapedRecipes)recipe).recipeItems;
			itemsList = Lists.newArrayList(isarray);
		}
		if(recipe instanceof ShapelessOreRecipe){
			itemsList = ((ShapelessOreRecipe)recipe).getInput();
		}
		if(recipe instanceof ShapedOreRecipe){
			Object[] isarray = ((ShapedOreRecipe)recipe).getInput();
			itemsList = Lists.newArrayList(isarray);
		}
		return itemsList;
	}
	
	public static void addShapelessRecipe(ItemStack output,Shapeless materials){
		GameRegistry.addShapelessRecipe(output, materials.getObj());
	}
	
	public static void addShapedRecipe(ItemStack output,Shaped recipe){
		UtilCollection.printArray(recipe.getObj(), CompressedBlocks.MODID);
		GameRegistry.addShapedRecipe(output, recipe.getObj());
	}
	
	public static ShapedOreRecipe getShapedOreRecipe(ItemStack output,Shaped recipe){
		
		return new ShapedOreRecipe(output,recipe.getObj());
	}
	
	public static ShapelessOreRecipe getShapelssOreRecipe(ItemStack output,Shapeless recipe){
		return new ShapelessOreRecipe(output,recipe.getObj());
	}
	
	public static Shaped getShaped(String recipe1,String recipe2,String recipe3){
		return new Shaped(recipe1,recipe2,recipe3);
	}
	public static class Recipe{
		protected Object[] recipe;

		public static class Shapeless extends Recipe{

			public Shapeless(Object... inputs) {
				super(inputs);
			
			}
			
			@Deprecated
			public Shapeless addAsociation(char chr,ItemStack is){
				List list = Lists.newArrayList(this.recipe);
				list.add(chr);
				list.add(is);
				this.recipe = list.toArray(new Object[]{});
				return this;
			}
			
			@Override
			public Shapeless getChangedRecipe(Object obj){
				Object[] reci = this.recipe.clone();
				for(int i=0;i<reci.length;i++){
					if(reci[i]==RecipeUtil.changeable.CHANGEABLE){
						reci[i] = obj;
					}
				}
				return new Shapeless(reci);
			}
		}
		
		public static class Shaped extends Recipe{
			public Shaped(Object... inputs) {
				super(inputs);
			
			}
			
			public Shaped addAsociation(char chr,ItemStack is){
				List list = Lists.newArrayList(this.recipe);
				list.add(chr);
				list.add(is);
				this.recipe = list.toArray();
				return this;
			}
			@Override
			public Shaped getChangedRecipe(Object obj){
				Object[] reci = this.recipe.clone();
				for(int i=0;i<reci.length;i++){
					if(reci[i]==RecipeUtil.changeable.CHANGEABLE){
						reci[i] = obj;
					}
				}
				return new Shaped(reci);
			}
		}
		
		
		public static Shapeless getShapelss(Object... inputs){
			return new Shapeless(inputs);
		}

		public Recipe(Object... recipes){
			this.recipe = recipes;
		}
		
		public Object[] getObj(){
			return this.recipe;
		}
		
		@Override
		public String toString(){
			StringBuilder builder = new StringBuilder();
			for(Object obj:this.recipe){
				boolean flag = false;
				if(obj instanceof String){
					builder.append("[String]"+((String)obj).toString());
					flag = true;
				}
				if(obj instanceof Character){
					builder.append("[Character]"+((Character)obj).toString());
					flag = true;
				}
				
				if(!flag){
					builder.append(obj.toString());
				}
				
				builder.append("/");
			}
			
			return new String(builder);
		}
		public Recipe getChangedRecipe(Object obj){
			Object[] reci = this.recipe.clone();
			for(int i=0;i<reci.length;i++){
				if(reci[i]==RecipeUtil.changeable.CHANGEABLE){
					reci[i] = obj;
				}
			}
			return new Recipe(reci);
		}
	}
	public static class Recipes{
		
		public static Shaped getPickaxe(){
			Shaped obj = new Shaped("III"," S "," S ",
					Character.valueOf('S'),Items.stick,
					Character.valueOf('I'),RecipeUtil.changeable.CHANGEABLE);
			return obj;
		}
		
		public static Shaped getFilled(){
			Shaped obj = new Shaped("SSS","SSS","SSS",
					Character.valueOf('S'),RecipeUtil.changeable.CHANGEABLE);
			return obj;
		}
		
		public static Shapeless getOne(){
			Shapeless obj = new Shapeless(RecipeUtil.changeable.CHANGEABLE);
			return obj;
		}
	}
}
