package com.hinasch.lib;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class VecUtil {


	//ブレを得る
	public static Vec3 getShake(Vec3 motion,Random rand,int upDownMin,int upDownMax,int leftRightMin,int leftRightMax){
		
		motion.rotateAroundX((float)Math.toRadians(rand.nextInt(leftRightMax)-leftRightMin));
		motion.rotateAroundZ((float)Math.toRadians(rand.nextInt(leftRightMax)-leftRightMin));
		motion.rotateAroundY((float)Math.toRadians(rand.nextInt(upDownMax)-upDownMin));
		return motion;
	}
	
	
	public static Vec3 getVecFromEntityMotion(World world,Entity ent){
		Vec3 vec = Vec3.createVectorHelper(ent.motionX, ent.motionY, ent.motionZ);
		return vec;
	}
}
