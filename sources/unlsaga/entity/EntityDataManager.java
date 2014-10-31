package com.hinasch.unlsaga.entity;

import java.util.ArrayList;
import java.util.List;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.entity.projectile.EntityArrowUnsaga;
import com.hinasch.unlsaga.entity.projectile.EntityBoulderNew;
import com.hinasch.unlsaga.entity.projectile.EntityBullet;
import com.hinasch.unlsaga.entity.projectile.EntityFireArrowNew;
import com.hinasch.unlsaga.entity.projectile.EntityFlyingAxeNew;
import com.hinasch.unlsaga.entity.projectile.EntitySolutionLiquid;

import net.minecraft.entity.Entity;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityDataManager {


	public EntityDataUnsaga arrow = new EntityDataUnsaga(1,"arrow");
	public EntityDataUnsaga barrett = new EntityDataUnsaga(2,"barrett");
	public EntityDataUnsaga flyingAxe = new EntityDataUnsaga(4,"flyingAxe");
	public EntityDataUnsaga fireArrow = new EntityDataUnsaga(3,"fireArrow");
	public EntityDataUnsaga boulder = new EntityDataUnsaga(6,"boulder");
	public EntityDataUnsaga solutionLiquid = new EntityDataUnsaga(7,"solutionLiquid");
	public EntityDataUnsaga treasureSlime = new EntityDataUnsaga(8,"treasureSlime");
	public EntityDataUnsaga golem = new EntityDataUnsaga(9,"golem");
	

	
	public static List<Class<? extends Entity>> entitySpawnableOnDebug;
	

	public EntityDataManager(){
		this.entitySpawnableOnDebug = new ArrayList();
	}
	
	public class EntityDataUnsaga{
		public final int id;
		public final String name;
		public EntityDataUnsaga(int id,String name){
			this.id = id;
			this.name = name;
		}
	}
	public void registerEntities(){
		EntityRegistry.registerModEntity(EntityArrowUnsaga.class, arrow.name, arrow.id, Unsaga.instance, 250, 5, true);
		EntityRegistry.registerModEntity(EntityBullet.class,barrett.name, barrett.id, Unsaga.instance, 250, 5, true);
		//fireball 3
		EntityRegistry.registerModEntity(EntityBoulderNew.class, boulder.name, boulder.id, Unsaga.instance, 250, 6, true);
		EntityRegistry.registerModEntity(EntityFireArrowNew.class, fireArrow.name, fireArrow.id, Unsaga.instance, 250, 6, true);
		EntityRegistry.registerModEntity(EntityFlyingAxeNew.class, flyingAxe.name, flyingAxe.id, Unsaga.instance, 250, 5, true);
		EntityRegistry.registerModEntity(EntityTreasureSlime.class, treasureSlime.name, treasureSlime.id, Unsaga.instance, 250, 5, true);
		EntityRegistry.registerModEntity(EntityGolemUnsaga.class, golem.name, golem.id, Unsaga.instance, 250, 5, true);
		EntityRegistry.registerModEntity(EntitySolutionLiquid.class, solutionLiquid.name, solutionLiquid.id, Unsaga.instance, 250, 6, true);

		addSpawnableInCreative(EntityTreasureSlime.class);
		addSpawnableInCreative(EntityGolemUnsaga.class);
	}
	
	public static void addSpawnableInCreative(Class<? extends Entity> entity){
		entitySpawnableOnDebug.add(entity);
	}
	

}
