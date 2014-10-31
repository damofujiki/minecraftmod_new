package com.hinasch.unlsaga.event;

import java.lang.reflect.Field;

import com.hinasch.unlsaga.Unsaga;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//https://github.com/wuppy29/WuppyMods/blob/master/Harder%20Peaceful/hp_common/com/wuppy/harderpeaceful/EventManager.java
public class EventReplaceFoodStats {
	@SubscribeEvent
	public void updateFood(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityPlayer){
			Unsaga.debug("trying to replace foodstats",this.getClass());
			setFoodStats((EntityPlayer) event.entity, new FoodStatsUnsaga());
		}

	}
	
	public static void setFoodStats(EntityPlayer player, FoodStats stats) 
	{
		Field[] fields = EntityPlayer.class.getDeclaredFields();

		for (Field f : fields) 
		{
			if (f.getType() == FoodStats.class) 
			{
				f.setAccessible(true);
				try 
				{
					f.set(player, stats);
				} 
				catch (Exception e) 
				{
					System.out.println(e);
				}
			}
		}
	}
}
