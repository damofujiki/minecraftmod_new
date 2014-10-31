package com.maidvillager;

import net.minecraft.util.ResourceLocation;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = mod_MaidVillager.MODID, name = mod_MaidVillager.NAME, version=mod_MaidVillager.VERSION)
public class mod_MaidVillager {
	@SidedProxy(modId= mod_MaidVillager.MODID,clientSide = "com.maidvillager.client.ClientProxy", serverSide = "com.maidvillager.CommonProxy")
	public static CommonProxy proxy;
	@Instance(mod_MaidVillager.MODID)
	public static mod_MaidVillager instance;
	
	public static final String MODID = "maidvillager";
	public static final String NAME = "Maid Villager";
	public static final String VERSION = "1.0";
	

	public static double changeMaidSize = 1.0;
	public static double changeChildMaidSize = 0.5;

	//public static String entityName = "Villager";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
//		for(int i=0;i<5;i++){
//			VillagerRegistry.instance().registerVillagerSkin(i,this.getResource(i));
//		}
		proxy.registerRenderer();
	}
	
	public static ResourceLocation getResource(int id){
		String path = getTexture(id);
		return new ResourceLocation(MODID,path);
	}
    public static String getTexture(int id)
    {
        switch (id)
        {
            case 0:
                return "textures/entity/farmer.png";

            case 1:
                return "textures/entity/librarian.png";

            case 2:
                return "textures/entity/priest.png";

            case 3:
                return "textures/entity/smith.png";

            case 4:
                return "textures/entity/butcher.png";

            case 155:
            	return "textures/entity/carrier.png";
            case 300:
            	return "textures/entity/builder.png";
            default:
                return "textures/entity/farmer.png";
        }
    }
        
	public static void debug(Object... par1){



		String str = "[MaidVillagerForge]";
		for(Object obj:par1){
			if(obj!=null){
				Class clas = obj.getClass();
				str += clas.cast(obj).toString()+":";
			}else{
				str += "Null!";
			}

		}
		if(Unsaga.debug){
			System.out.println(str);
		}
	}
//	@Override
//	public void modsLoaded() {
//		// Entity�u��
//        Entity entity = EntityList.createEntityByName(entityName, null);
//        int id = EntityList.getEntityID(entity);
//        
//        ModLoader.registerEntityID(EntityMaidVillager.class, entityName, id);
//	}
//
//	// ���f�������A�����_�����O
//	@Override
//	public void addRenderer(Map map){
//		super.addRenderer(map);
//		map.put(net.minecraft.src.EntityMaidVillager.class, new RenderMaidVillager(new ModelMaidVillager(), 0.5F));
//	}

}
