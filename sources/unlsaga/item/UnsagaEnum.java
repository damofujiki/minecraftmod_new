package com.hinasch.unlsaga.item;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hinasch.lib.UtilCollection;
import com.hinasch.unlsaga.util.translation.Translation;


public class UnsagaEnum {

	public static enum ToolCategory {
		SWORD,STAFF,SPEAR,BOW,AXE,ARMOR,HELMET,LEGGINS,BOOTS,ACCESSORY,PICKAXE,GUN;
	
		public static final Set<String> weaponSet = toString(Sets.immutableEnumSet(ToolCategory.SWORD,ToolCategory.STAFF,ToolCategory.SPEAR,ToolCategory.BOW,ToolCategory.AXE));
		public static final Set<String> armorSet = toString(Sets.immutableEnumSet(ToolCategory.HELMET,ToolCategory.LEGGINS,ToolCategory.ARMOR,ToolCategory.BOOTS));
		public static final Set<String> merchandiseSet = toString(Sets.immutableEnumSet(ToolCategory.SWORD,ToolCategory.STAFF,ToolCategory.SPEAR,ToolCategory.BOW,ToolCategory.AXE,ToolCategory.ARMOR,ToolCategory.HELMET,ToolCategory.LEGGINS,ToolCategory.BOOTS,ToolCategory.ACCESSORY));
		public static final List<ToolCategory> toolArray = Lists.newArrayList(ToolCategory.SWORD,ToolCategory.STAFF,ToolCategory.SPEAR,ToolCategory.BOW,ToolCategory.AXE,ToolCategory.ACCESSORY);
		
		public static HashSet<String> toString(ImmutableSet<ToolCategory> immutableSet){
			HashSet<String> newSet = new HashSet();
			for(Iterator<ToolCategory> ite=immutableSet.iterator();ite.hasNext();){
				newSet.add(ite.next().toString());
			}
			return newSet;
		}
		
		public String getLocalized(){
			return Translation.localize("word."+this.toString().toLowerCase());
		}
		
		public static boolean categoryContains(ToolCategory cate,Collection colle){
			boolean isString = false;
			for(Iterator ite=colle.iterator();ite.hasNext();){
				if(ite.next() instanceof String){
					isString = true;
				}
			}
			if(isString){
				return UtilCollection.collectionContains(cate.toString(), colle);
			}
			return UtilCollection.collectionContains(cate, colle);
		}
	}
	
	
}
	


