package com.maidvillager.client;

import net.minecraft.entity.passive.EntityVillager;

import com.maidvillager.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerRenderer(){
		RenderingRegistry.registerEntityRenderingHandler(EntityVillager.class, new RenderMaidVillager(new ModelMaidVillager(), 0.5F));
	}
}
