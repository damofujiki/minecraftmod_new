package com.hinasch.lib;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientHelper {
	public static Minecraft mc;
	
	@SideOnly(Side.CLIENT)
	public static MovingObjectPosition getPlayerMouseOver(){
		return Minecraft.getMinecraft().objectMouseOver;
	}

	@SideOnly(Side.CLIENT)
	public static void registerKeyBinding(KeyBinding... kies){
	
		for(KeyBinding key:kies){
			ClientRegistry.registerKeyBinding(key);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static EntityPlayer getClientPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}

	@SideOnly(Side.CLIENT)
	public static MovingObjectPosition getMouseOverLongReach(){
			Entity pointedEntity = null;
			Entity newPointedEntity = null;
			EntityLivingBase pointedEntityLiving = null;
			MovingObjectPosition objectMouseOver = null;
			//Entity pointedEntity = null;
			if(ClientHelper.mc==null){
				ClientHelper.mc = FMLClientHandler.instance().getClient();
			}
	
			 if (ClientHelper.mc.renderViewEntity != null)
		        {
		            if (ClientHelper.mc.theWorld != null)
		            {
		            	newPointedEntity = null;
		                double d0 = 20.0D;//(double)mc.playerController.getBlockReachDistance();
		                objectMouseOver = ClientHelper.mc.renderViewEntity.rayTrace(d0, 1.0F);
		                double d1 = d0;
		                Vec3 vec3 = ClientHelper.mc.renderViewEntity.getPosition(1.0F);
	
	//	                if (mc.playerController.extendedReach())
	//	                {
	//	                    d0 = 20.0D;
	//	                    d1 = 20.0D;
	//	                }
	//	                else
	//	                {
	////	                    if (d0 > 3.0D)
	////	                    {
	////	                        d1 = 3.0D;
	////	                    }
	//
	//	                    d0 = d1;
	//	                }
	
		                if (objectMouseOver != null)
		                {
		                    d1 = objectMouseOver.hitVec.distanceTo(vec3);
		                }
	
		                Vec3 vec31 = ClientHelper.mc.renderViewEntity.getLook(1.0F);
		                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
		                pointedEntity = null;
		                Vec3 vec33 = null;
		                float f1 = 1.0F;
		                List list = ClientHelper.mc.theWorld.getEntitiesWithinAABBExcludingEntity(ClientHelper.mc.renderViewEntity, ClientHelper.mc.renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
		                double d2 = d1;
	
		                for (int i = 0; i < list.size(); ++i)
		                {
		                    Entity entity = (Entity)list.get(i);
	
		                    if (entity.canBeCollidedWith())
		                    {
		                        float f2 = entity.getCollisionBorderSize();
		                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
		                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
	
		                        if (axisalignedbb.isVecInside(vec3))
		                        {
		                            if (0.0D < d2 || d2 == 0.0D)
		                            {
		                                pointedEntity = entity;
		                                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
		                                d2 = 0.0D;
		                            }
		                        }
		                        else if (movingobjectposition != null)
		                        {
		                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);
	
		                            if (d3 < d2 || d2 == 0.0D)
		                            {
		                                if (entity == ClientHelper.mc.renderViewEntity.ridingEntity && !entity.canRiderInteract())
		                                {
		                                    if (d2 == 0.0D)
		                                    {
		                                        pointedEntity = entity;
		                                        vec33 = movingobjectposition.hitVec;
		                                    }
		                                }
		                                else
		                                {
		                                    pointedEntity = entity;
		                                    vec33 = movingobjectposition.hitVec;
		                                    d2 = d3;
		                                }
		                            }
		                        }
		                    }
		                }
	
		                if (pointedEntity != null && (d2 < d1 || objectMouseOver == null))
		                {
		                    objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
	
		                    if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame)
		                    {
		                    	newPointedEntity = pointedEntity;
		                    }
		                }
		            }
		        }
	
			return objectMouseOver;
		}

}
