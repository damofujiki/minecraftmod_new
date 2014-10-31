package com.hinasch.unlsaga.ability;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;

public class GainProbFromEnemy {

	
	protected static Map<Class,Integer> gainProbList = new HashMap();
	
	public static int getProbGainFromEntity(Entity entity){
		if(gainProbList.containsKey(entity.getClass())){
			return gainProbList.get(entity.getClass());
		}
		return 10;
	}
	static{
		gainProbList.put(EntityEnderman.class, 30);
		gainProbList.put(EntityZombie.class, 10);
		gainProbList.put(EntityPigZombie.class, 25);
		gainProbList.put(EntityDragon.class, 60);
		gainProbList.put(EntityDragon.class, 60);
		gainProbList.put(EntityCreeper.class, 15);
		gainProbList.put(EntitySpider.class, 10);
		gainProbList.put(EntityCaveSpider.class, 15);
		gainProbList.put(EntitySkeleton.class, 15);
		gainProbList.put(EntitySlime.class, 5);
		gainProbList.put(EntityMagmaCube.class, 15);
		gainProbList.put(EntityGhast.class, 20);
	}
}
