package com.hinasch.unlsaga.client;

import net.minecraftforge.client.event.TextureStitchEvent;

import com.hinasch.lib.ParticleHandler;
import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class ParticlesUnsaga extends ParticleHandler{

	
	public static ParticlesUnsaga INSTANCE;
	
	
	public ParticleHandler init(){
		this.setIconnames("stone","leave","bubble");
		this.setPath(Unsaga.DOMAIN+":particles/");
		return this;
	}
	
	public static ParticlesUnsaga getInstance(){
		if(INSTANCE==null){
			INSTANCE = new ParticlesUnsaga();
		}
		return INSTANCE;
	}
	
	@Override
	@SubscribeEvent
	public void registerParticlesEvent(TextureStitchEvent.Pre e){
		if(e.map.getTextureType()==1){
			
			this.getInstance().init().registerIcons(e.map);
		}
	}

}
