package com.hinasch.unlsaga.util;

import java.util.EnumSet;

import com.hinasch.lib.Statics;
import com.hinasch.unlsaga.util.translation.Translation;

public class FiveElements {

	public static enum Enums {
		FIRE,EARTH,WOOD,METAL,WATER,FORBIDDEN;
	
		
	
		public String getUnlocalized(){
		
			switch(this){
			case FIRE:return "element.fire";
			case EARTH:return "element.earth";
			case WOOD:return "element.wood";
			case METAL:return "element.metal";
			case WATER:return "element.water";
			case FORBIDDEN:return "element.forbidden";
			}
	
			return "";
		}
		
		public String getLocalized(){
			return Translation.localize(this.getUnlocalized());
		}
		
		public int getElementColor(){
			switch(this){
			case FIRE:return 0xff0000;
			case EARTH:return 0x8b4513;
			case WOOD:return 0x6b8e23;
			case METAL:return 0xffff00;
			case WATER:return 0x4169e1;
			case FORBIDDEN:return 0x800080;
			}
			return 0xffffff;
		}
		
		public int getElementParticle(){
			switch(this){
			case FIRE:return Statics.getParticleNumber(Statics.particleFlame);
			case EARTH:return 200;
			case WOOD:return 201;
			case METAL:return Statics.getParticleNumber(Statics.particleReddust);
			case WATER:return 202;
			case FORBIDDEN:return Statics.getParticleNumber(Statics.particlePortal);
			}
			return -1;
		}
	
	}

	public static EnumSet<Enums> enumElements = EnumSet.of(Enums.FIRE,Enums.EARTH,Enums.WOOD,Enums.METAL,Enums.WATER,Enums.FORBIDDEN);

}
