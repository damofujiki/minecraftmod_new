package com.hinasch.unlsaga.villager.bartering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;

import com.google.common.base.Optional;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.RecipeUtil;
import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.ability.skill.HelperSkill;
import com.hinasch.unlsaga.init.UnsagaItems.EnumSelecterItem;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.armor.ItemAccessory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;

public class MerchandiseInfo {

	protected static Map<Class,Float> classMap;
	protected ItemStack is;
	protected MaterialInfo info;
	protected static WeightedRandomNumber[] wr;
	
	protected static final String PRICE_TAG = "Bartering.Price";
	public MerchandiseInfo(ItemStack is){
		this.is = is;
		this.info = new MaterialInfo(is);
	}
	
	public int getPrice(){
		return getPrice(this.is);
	}
	
	public static ItemStack getRandomMaterialItemStack(Random rand){
		ArrayList<ItemStack> itemstackList = new ArrayList();
		for(Iterator<UnsagaMaterial> ite=Unsaga.materialManager.allMaterials.values().iterator();ite.hasNext();){
			UnsagaMaterial mate = ite.next();
			if(mate.getAssociatedItemStack().isPresent()){
				itemstackList.add(mate.getAssociatedItemStack().get());
			}
		}
		return itemstackList.get(rand.nextInt(itemstackList.size()));
	}
	

	public static ItemStack getRandomMerchandise(Random rand,int popularity){
		int merchantrank = getRankFromPopularity(popularity);
		if(rand.nextInt(3)<=1){
			return getRandomMaterialItemStack(rand);
		}
		int min = 0;
		int max = merchantrank + 2;
		ItemStack ms = Unsaga.items.getRandomWeapon(rand, 0,max, EnumSelecterItem.MERCHANDISE);
		//Random Enchant
		if(rand.nextInt(6)<=1){
			WeightedRandomNumber wrs = (WeightedRandomNumber) WeightedRandom.getRandomItem(rand, wr);

			EnchantmentHelper.addRandomEnchantment(rand, ms, wrs.number);
			//EnchantmentData enchant = new EnchantmentData(Enchantment.fortune,1);
		}
		//Random Ability
		if(rand.nextInt(6)<=1){
			if(ms.getItem() instanceof IUnsagaMaterialTool && ms.getItem() instanceof IAbility){
				IUnsagaMaterialTool ium = (IUnsagaMaterialTool) ms.getItem();
				HelperAbility ha = null;
				if(ToolCategory.categoryContains(ium.getCategory(), ToolCategory.weaponSet)){
					ha = new HelperSkill(ms, null);
				}else{
					ha = new HelperAbility(ms,null);
				}
				if(ha!=null){
					ha.gainSomeAbility(rand);
				}
			}
		}
		return ms;
	}
	
	
	public static int getPrice(ItemStack is){
		int price = 0;
		price = Unsaga.merchandiseLibrary.getPriceFromUnsagaMaterialItem(is);
		
		
		
		float per = 1.0F;

		

		
//		if(classMap.containsKey(is.getItem().getClass())){
//			price = (int)((float)price * classMap.get(is.getItem().getClass()));
//			per = (is.getMaxDamage()-is.getItemDamage())/HSLibs.exceptZero(is.getMaxDamage());
//			price = (int)((float)price*per);
//			if(price<10){
//				price = 10;
//			}
//		}
		if(is.isItemEnchanted()){
			Map enchantmap = EnchantmentHelper.getEnchantments(is);
			int maxlv = 0;
			int lv = 0;
			for(Iterator<Integer> ite=enchantmap.values().iterator();ite.hasNext();){
				lv = ite.next();
				if(maxlv<=lv){
					maxlv = lv;
				}
			}
			maxlv += 1;
			int enchantmentvalue = (int)((float)price*0.5F*(float)maxlv);
			enchantmentvalue *= enchantmap.size();
			price += enchantmentvalue;
			
		}

		if(HelperAbility.getGainedAbilitiesInteger(is).isPresent()){
			int size = HelperAbility.getGainedAbilitiesInteger(is).get().size();
			if(size>0){
				price = price + (int)((float)price*0.4F*(float)size);
			}
			
		}
		boolean flag  = false;
		if(Unsaga.merchandiseLibrary.find(is).isPresent() && !flag){
			MerchandiseLibraryBook book = (MerchandiseLibraryBook) Unsaga.merchandiseLibrary.find(is).get();
			price = book.price;
			flag = true;
		}
//		if(HSLibs.getToolMaterialNameFromTool(is.getItem())!=null && !flag){
//			String str = HSLibs.getToolMaterialNameFromTool(is.getItem());
//			if(Unsaga.merchandiseFactory.findPrice(str).isPresent()){
//				price = Unsaga.merchandiseFactory.findPrice(str).get();
//				flag = true;
//			}
//		}
		
		if(!flag && supposeAsFood(is).isPresent()){
			price = supposeAsFood(is).get();
		}
		if(!flag && supposeFromRecipe(is).isPresent()){
			
			price = supposeFromRecipe(is).get();
		}
		
//		if(is.getItem() instanceof ItemTool){
//			String str = ((ItemTool)is.getItem()).getToolMaterialName();
//			if(Unsaga.merchandiseFactory.findPrice(str).isPresent()){
//				price = Unsaga.merchandiseFactory.findPrice(str).get();
//			}
//		}
		
		for(Class cl:classMap.keySet()){
			Unsaga.debug("クラスを調べます");
			if(ItemUtil.isSameClass(is, cl)){
				Unsaga.debug("クラスが一致しました");
				price = (int)((float)price * classMap.get(cl));

				if(price<10){
					price = 10;
				}
			}
		}

		if(is.getItem() instanceof ItemEnchantedBook){
			ItemEnchantedBook book = (ItemEnchantedBook) is.getItem();
			NBTTagList nbttaglist = book.func_92110_g(is);
			price = 500;
	        if (nbttaglist != null)
	        {
	            for (int i = 0; i < nbttaglist.tagCount(); ++i)
	            {
	                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
	                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");

	                price += (500*short2);
	               
	            }
	        }
		}
		if(is.getItem().isRepairable() && is.getItem().isDamageable()){
			per = (float)(is.getMaxDamage()-is.getItemDamage())/HSLibs.exceptZero(is.getMaxDamage());
			Unsaga.debug(price+":"+per);
			price = (int)((float)price*per);
		}
		if(is.stackSize>1){
			price *= is.stackSize;
		}
		
		return (int)price;
	}
	
	public static void setBuyPriceTag(ItemStack is,int price){
		UtilNBT.setFreeTag(is, PRICE_TAG, price);
	}
	
	public int getBuyPriceTag(){
		return UtilNBT.readFreeTag(this.is, PRICE_TAG);
	}
	
	public int getBuyPriceTagWithDiscount(int isPriceDown,int isPriceUp){
		int priceBuy = this.getBuyPriceTag();
		if(isPriceDown>0){
			priceBuy -= priceBuy * ((float)isPriceDown * 0.05F);
		}
		if(isPriceUp>0){
			priceBuy += priceBuy * ((float)isPriceUp * 0.05F);
		}
		return priceBuy;
	}
	
	public static boolean hasBuyPriceTag(ItemStack is){
		return UtilNBT.hasKey(is, PRICE_TAG);
	}
	
	public static boolean isPossibleToSell(ItemStack is){
		MaterialInfo info = new MaterialInfo(is);
		if(info.getMaterial().isPresent())return true;
		if(Unsaga.merchandiseLibrary.find(is).isPresent())return true;
		if(supposeFromRecipe(is).isPresent())return true;
		if(getPrice(is)>0)return true;
		return false;
	}
	
	public static void removePriceTag(ItemStack is){
		if(is.getMaxStackSize()==1){
			UtilNBT.removeTag(is, PRICE_TAG);
			return;
		}

		UtilNBT.clearNBT(is);
		
	}
	
	public static Optional<Integer> supposeAsFood(ItemStack is){
		int price = 0;
		if(is!=null && is.getItem() instanceof ItemFood){
			ItemFood food = (ItemFood) is.getItem();
			price = food.func_150905_g(is) * 8;
		}
		if(price!=0){
			return Optional.of(price);
		}
		return Optional.absent();
	}

	//レシピからの推測
	public static Optional<Integer> supposeFromRecipe(ItemStack is){
		List list = CraftingManager.getInstance().getRecipeList();
		int price = 0;
		boolean flag = false;
		Unsaga.debug("レシピから推測します");
		for(Object obj:list){
			IRecipe recipe = (IRecipe)obj;
			if(recipe.getRecipeOutput()!=null && is.isItemEqual(recipe.getRecipeOutput()) && !flag){
				
				//Unsaga.debug(recipe.getRecipeOutput());
				//Unsaga.debug(is);
				//Unsaga.debug("レシピが一致するのがありました");
				//クラフト前レシピに必要なアイテム一覧を取得
				List<Object> itemsList = RecipeUtil.getRequireItemStacksFromRecipe(recipe);
				for(Object ob:itemsList){
					if(ob!=null){
						if(ob instanceof ArrayList){
							Unsaga.debug(((ArrayList) ob).get(0));
						}
						if(ob instanceof ItemStack){
							Unsaga.debug(ob);
						}
						//Unsaga.debug(ob.getClass().getSimpleName());
					}
					
				}
				//Unsaga.debug(itemsList);
				
				if(itemsList!=null && !itemsList.isEmpty()){
					Unsaga.debug("レシピ解析できました");
					//CraftingManager
					int stack = recipe.getRecipeOutput().stackSize;
					if(stack!=0){
						price = getPriceFromRecipe(itemsList)/stack;
					}
					
				}
				flag = true;
			}
		}
		if(price==0){
			return Optional.absent();
		}
		return Optional.of(price);
	}
	

	protected static int getPriceFromRecipe(List<Object> itemList){
		int price = 0;
		for(Object obj:itemList){
			if(obj instanceof ArrayList){
				List list = (ArrayList)obj;
				for(Object elm:list){
					if(elm!=null && Unsaga.merchandiseLibrary.find(elm).isPresent()){
						MerchandiseLibraryBook book = (MerchandiseLibraryBook) Unsaga.merchandiseLibrary.find(elm).get();
						price += book.price;
					}
				}

			}else{
				if(obj!=null && Unsaga.merchandiseLibrary.find(obj).isPresent()){
					MerchandiseLibraryBook book = (MerchandiseLibraryBook) Unsaga.merchandiseLibrary.find(obj).get();
					price += book.price;
				}
			}


		}
		return price;
		
	}
	public static int getRankFromPopularity(int popularity){
		int rank = 1;
		popularity = MathHelper.clamp_int(popularity, 1, 300);
		for(int i=0;i<10;i++){
			if((i+1)*(i+1)<popularity){
				rank = i;
			}
			
		}
		return rank;
	}
	
	public static class WeightedRandomNumber extends WeightedRandom.Item{

		public final int number;
		
		public WeightedRandomNumber(int weight,int num) {
			super(weight);
			this.number = num;
			
		}

	}
	
	static{
		classMap = new HashMap();
		classMap.put(ItemBow.class, 2.0F);
		classMap.put(ItemSword.class, 1.8F);
		classMap.put(ItemTool.class, 1.9F);
		classMap.put(ItemArmor.class, 2.0F);
		classMap.put(ItemHoe.class, 2.0F);
		classMap.put(ItemAccessory.class,1.6F);
		
		wr = new WeightedRandomNumber[20];
		for(int i=0;i<20;i++){
			int itemWeight = (wr.length -i)*(wr.length -i);
			wr[i] = new WeightedRandomNumber(itemWeight,i+1);
		}
	}
}
