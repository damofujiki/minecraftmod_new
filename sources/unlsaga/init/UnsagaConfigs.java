package com.hinasch.unlsaga.init;



import net.minecraftforge.common.config.Configuration;

import com.hinasch.lib.PropertyCustom;
import com.hinasch.unlsaga.Unsaga;

public class UnsagaConfigs {

	
	public int maxPlayerLP = 8;
	public int maxMobLP = 3;
	
	public int probGenChest = 2;
	
	public int villagerID_Carrier;
	
	public boolean enableGenerateOres = true;
	public boolean enableGenerateChestOverworld = true;
	public boolean enableLP = false;
	public boolean isDecipherAtSleep = true;
	protected boolean isMagicEnabled = false;
	
	protected PropertyCustom props;
	protected Configuration configFile;
	
	
	//public Module module;	
	public UnsagaConfigs(){
		//module = new Module();
	}
	
	public void init(Configuration config){
		//this.configFile = config;

		props = new PropertyCustom (new String[]{"enable.generateOres","enable.LPsystem","decipher.progress.at.sleep","villagerID_Carrier","enable.genOverworldChest"});
		props.setCategoriesAll(config.CATEGORY_GENERAL);
		props.setValuesSequentially(true,true,true,155,false);

		props.setComments("Generate Unsaga Ores.","Enable LP system..","If you set true,deciphering progresses at sleep.If you set false,progresses at taking a exp orb."
				,"Require Minecraft restart.","Generate Bonus Chests On Overworld.");
		
		props.buildProps(Unsaga.configFile);
		
		enableGenerateOres = props.getProp(0).getBoolean(true);
		enableLP = props.getProp(1).getBoolean(false);
		isDecipherAtSleep = props.getProp(2).getBoolean(true);
		this.villagerID_Carrier = props.getProp(3).getInt();
		enableGenerateChestOverworld = props.getProp(4).getBoolean(false);
	}
	
	
	public void syncConfig(){
		enableGenerateOres = props.getProp(0).getBoolean(true);
		enableLP = props.getProp(1).getBoolean(true);//Unsaga.configFile.getBoolean(props.propNames[1], props.propCategories[1], enableLP, "Enable LP System.");
		isDecipherAtSleep = props.getProp(2).getBoolean(true);
		this.villagerID_Carrier = props.getProp(3).getInt();
		enableGenerateChestOverworld = props.getProp(4).getBoolean(false);
		
		if(Unsaga.configFile.hasChanged()){
			Unsaga.configFile.save();
		}
	}
//	public class Module{
//		public boolean isLPEnabled(){
//			return Unsaga.lpHandler.isLPEnabled();
//		}
//		
//		public boolean isMagicEnabled(){
//			return isMagicEnabled;
//		}
//		
//		public void setMagicEnabled(boolean par1,Unsaga par2){
//			if(par2!=null){
//				isMagicEnabled = par1;
//			}
//		}
//	}
	

}
