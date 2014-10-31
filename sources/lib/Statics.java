package com.hinasch.lib;

import java.util.HashMap;

public class Statics {

	public static final String soundXP = "random.orb";
	public static final String soundAnvilUse = "random.anvil_use";
	public static final String soundEnderPortal = "mob.endermen.portal";
	public static final String soundExplode = "random.explode";
	public static final String soundShoot = "mob.wither.shoot";
	public static final String soundFireBall = "mob.ghast.fireball";
	public static final String soundBow = "random.bow";
	
	public static final String particlePortal = "portal";
	public static final String particleHeart = "heart";
	public static final String particleHappy = "happyVillager";
	public static final String particleMobSpell = "mobSpell";
	public static final String particleSpell = "spell";
	public static final String particleLava = "lava";
	public static final String particleFlame = "flame";
	public static final String particleCloud = "cloud";
	public static final String particleExplode = "explode";
	public static final String particleBubble = "bubble";
	public static final String particleReddust = "reddust";
	public static final String particleCrit = "crit";
	public static HashMap<Integer,String> particleMap;
	public static HashMap<Integer,String> soundMap;
	
	
	public static int getParticleNumber(String key){
		for(Integer num:particleMap.keySet()){
			if(key.equals(particleMap.get(num))){
				return num;
			}
		}
		return 1;
	}
	static{
		particleMap = new HashMap();
		particleMap.put(1, "portal");
		particleMap.put(2, "heart");
		particleMap.put(3, "happyVillager");
		particleMap.put(4, "mobSpell");
		particleMap.put(5, "spell");
		particleMap.put(6, "lava");
		particleMap.put(7, "flame");
		particleMap.put(8, "cloud");
		particleMap.put(9, "explode");
		particleMap.put(10,particleBubble);
		particleMap.put(11,particleReddust);
		particleMap.put(12, particleCrit);
		soundMap = new HashMap();
		soundMap.put(1, "mob.endermen.portal");
		soundMap.put(2, "mob.ghast.fireball");
		soundMap.put(3, "random.explode");
	}
}
