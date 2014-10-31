package com.hinasch.unlsaga.villager.smith;

import java.util.HashSet;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ValidPayment {

	public static enum EnumPayValues {HIGH,MID,LOW};
	
	public static HashSet<ValidPayment> validPayList = new HashSet();

	public static final ValidPayment payEmerald = new ValidPayment("item",Items.emerald,EnumPayValues.HIGH);
	public static final ValidPayment payGold = new ValidPayment("item",Items.gold_ingot,EnumPayValues.MID);
	public static final ValidPayment payGoldFromKey = new ValidPayment("dict","ingotGold",EnumPayValues.MID);
	public static final ValidPayment payGoldNugget = new ValidPayment("item",Items.gold_nugget,EnumPayValues.LOW);
	
	protected final EnumPayValues value;
	protected final String key;
	protected final Item itemObj;
	protected final String oreDictKey;
	
	public ValidPayment(String par1,Object par2,EnumPayValues par3){
		if(par1.equals("item")){
			Item item = (Item)par2;
			this.itemObj = item;
		}else{
			this.itemObj = null;
		}
		this.oreDictKey = par1.equals("dict") ? (String)par2 : "";

		this.key = par1;
		this.value = par3;
		validPayList.add(this);
	}
	
	public static EnumPayValues getValueFromItemStack(ItemStack is){
		EnumPayValues enumpay = null;
		for(ValidPayment pay:validPayList){
			if(pay.compare(is)){
				enumpay = pay.value;
			}
		}
		return enumpay;
	}
	
	public boolean compare(ItemStack is){
		if(this.key.equals("item")){
			if(is.getItem() == this.itemObj){
				return true;
			}
		}
		if(this.key.equals("dict")){
			int oreid = OreDictionary.getOreID(is);
			String orekey = OreDictionary.getOreName(oreid);
			if(this.oreDictKey.equals(orekey)){
				return true;
			}
		}
		return false;
	}
	
}
