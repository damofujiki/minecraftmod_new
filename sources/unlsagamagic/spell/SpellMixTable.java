package com.hinasch.unlsagamagic.spell;

import java.util.HashMap;
import java.util.Map;

import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsaga.util.translation.Translation;






public class SpellMixTable {

	protected Map<FiveElements.Enums,Float> elementMap;
	
	public SpellMixTable(){
		this.elementMap = new HashMap();
		for(FiveElements.Enums element:FiveElements.enumElements){
			this.elementMap.put(element, 0F);
		}
	}
	
	public SpellMixTable(float fire,float earth,float metal,float water,float wood,float forbidden){
		this.elementMap = new HashMap();
		this.elementMap.put(FiveElements.Enums.FIRE, fire);
		this.elementMap.put(FiveElements.Enums.EARTH, earth);
		this.elementMap.put(FiveElements.Enums.METAL, metal);
		this.elementMap.put(FiveElements.Enums.WATER, water);
		this.elementMap.put(FiveElements.Enums.WOOD, wood);
		this.elementMap.put(FiveElements.Enums.FORBIDDEN, forbidden);
	}
	
	public SpellMixTable(FiveElements.Enums element,float par1){
		this.elementMap = new HashMap();
		for(FiveElements.Enums ele:FiveElements.enumElements){
			this.elementMap.put(ele, 0F);
		}
		this.elementMap.put(element, par1);
	}
	
	
	public void add(SpellMixTable table){
		this.add(FiveElements.Enums.FIRE, table.get(FiveElements.Enums.FIRE));
		this.add(FiveElements.Enums.WOOD, table.get(FiveElements.Enums.WOOD));
		this.add(FiveElements.Enums.EARTH, table.get(FiveElements.Enums.EARTH));
		this.add(FiveElements.Enums.METAL, table.get(FiveElements.Enums.METAL));
		this.add(FiveElements.Enums.WATER, table.get(FiveElements.Enums.WATER));
		this.add(FiveElements.Enums.FORBIDDEN, table.get(FiveElements.Enums.FORBIDDEN));

	}
	

	public boolean isBiggerThan(SpellMixTable table){
		int flag = 0;
		if(this.get(FiveElements.Enums.FIRE)>=table.get(FiveElements.Enums.FIRE))flag+=1;
		if(this.get(FiveElements.Enums.WOOD)>=table.get(FiveElements.Enums.WOOD))flag+=1;
		if(this.get(FiveElements.Enums.EARTH)>=table.get(FiveElements.Enums.EARTH))flag+=1;
		if(this.get(FiveElements.Enums.METAL)>=table.get(FiveElements.Enums.METAL))flag+=1;
		if(this.get(FiveElements.Enums.WATER)>=table.get(FiveElements.Enums.WATER))flag+=1;
		if(this.get(FiveElements.Enums.FORBIDDEN)>=table.get(FiveElements.Enums.FORBIDDEN))flag+=1;
		//Unsaga.debug(flag);
		if(flag>=6){
			return true;
		}
		return false;
	}
	

	
	public float get(FiveElements.Enums element){
		return this.elementMap.get(element);
	}
	
	public int getInt(FiveElements.Enums element){
		return Math.round(this.elementMap.get(element));
	}

	public void add(FiveElements.Enums element,float par1){
		float var1 = this.get(element);
		this.elementMap.put(element, var1+par1);
	}
	
	public void put(FiveElements.Enums element,float par1){
		this.elementMap.put(element, par1);
	}
	
	public void cut(int min,int max){
		for(FiveElements.Enums element:FiveElements.enumElements){
			if((int)this.get(element)<min){
				this.put(element,(float) min);
			}
			if((int)this.get(element)>max){
				this.put(element,(float) max);
			}
		}
	}
	
	public void reset(){
		for(FiveElements.Enums element:FiveElements.enumElements){
			this.put(element, 0F);
		}
	}
	
	
	@Override
	public String toString(){
		String str = Translation.localize("gui.blender.elements");
		String formatted = String.format(str, this.getInt(FiveElements.Enums.FIRE),this.getInt(FiveElements.Enums.METAL),this.getInt(FiveElements.Enums.WOOD)
				,this.getInt(FiveElements.Enums.WATER),this.getInt(FiveElements.Enums.EARTH),this.getInt(FiveElements.Enums.FORBIDDEN));
//		StringBuilder builder = new StringBuilder();
//		for(EnumElement element:UnsagaElement.enumElements){
//			builder.append(element.toString()+":"+this.get(element)+" ");
//		}
		return formatted;
	}
	

	public String getPercentage(){
		String str = Translation.localize("gui.blender.elements");
		String formatted = String.format(str, this.getInt(FiveElements.Enums.FIRE),this.getInt(FiveElements.Enums.METAL),this.getInt(FiveElements.Enums.WOOD)
				,this.getInt(FiveElements.Enums.WATER),this.getInt(FiveElements.Enums.EARTH),this.getInt(FiveElements.Enums.FORBIDDEN));
//		StringBuilder builder = new StringBuilder();
//		for(EnumElement element:UnsagaElement.enumElements){
//			builder.append(element.toString()+":"+this.get(element)+" ");
//		}
		return formatted;
	}
}
