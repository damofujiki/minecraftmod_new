package com.hinasch.lib;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

//http://minecraftjp.info/modding/index.php/%E3%83%91%E3%83%BC%E3%83%86%E3%82%A3%E3%82%AF%E3%83%AB%E3%81%AE%E8%BF%BD%E5%8A%A0
public abstract class ParticleHandler {
	
	protected String path;
	protected String[] iiconNames;
	protected IIcon iicons[];
	protected DebugLog logger;
	public static ParticleHandler INSTANCE;
	
	protected ParticleHandler(){
		
	}
	protected ParticleHandler(String path,String... iconnames){
		this.path  = path;
		this.iiconNames = iconnames;
	}
	

	public ParticleHandler setIconnames(String... icons){
		this.iiconNames = icons;
		return this;
	}
	
	public ParticleHandler setPath(String path){
		this.path = path;
		return this;
	}
//	public static ParticleHandler getInstance(){
//		if(INSTANCE==null){
//			INSTANCE = new ParticleHandler();
//		}
//		return INSTANCE;
//	}
	//@SubscribeEvent
	
	
	
	abstract public void registerParticlesEvent(TextureStitchEvent.Pre e);
//{
//		if(e.map.getTextureType()==1){
//			
//			this.getInstance().registerIcons(e.map);
//		}
//	}
	
	

	public void registerIcons(IIconRegister par1IconRegister) {
		iicons = new IIcon[iiconNames.length];
		for(int i = 0; i < iicons.length; ++i) {
			iicons[i] = par1IconRegister.registerIcon(this.path+iiconNames[i]);
			if(this.logger!=null){
				this.logger.log("register particle:"+iicons[i]);
			}
		}
	}
	

	public IIcon getIcon(String iiconName) {
		for(int i = 0; i < iiconNames.length; ++i) {
			if(iiconName.equalsIgnoreCase(iiconNames[i])) {
				
				return iicons[i];
			}
		}
		return null;
	}
}
