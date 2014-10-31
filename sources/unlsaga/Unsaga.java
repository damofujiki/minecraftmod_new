package com.hinasch.unlsaga;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import com.hinasch.lib.DebugLog;
import com.hinasch.lib.SimpleCreativeTab;
import com.hinasch.unlsaga.ability.AbilityRegistry;
import com.hinasch.unlsaga.block.BlockDataUnsaga;
import com.hinasch.unlsaga.block.WorldGeneratorUnsaga;
import com.hinasch.unlsaga.command.CommandAbility;
import com.hinasch.unlsaga.debuff.Debuffs;
import com.hinasch.unlsaga.entity.EntityDataManager;
import com.hinasch.unlsaga.event.EventManager;
import com.hinasch.unlsaga.event.extendeddata.ExtendedDataLP.LPManager;
import com.hinasch.unlsaga.init.UnsagaBlocks;
import com.hinasch.unlsaga.init.UnsagaConfigs;
import com.hinasch.unlsaga.init.UnsagaItems;
import com.hinasch.unlsaga.init.UnsagaRecipes;
import com.hinasch.unlsaga.item.NoFunctionItems;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.material.UnsagaMaterials;
import com.hinasch.unlsaga.network.CommonProxy;
import com.hinasch.unlsaga.network.packet.PacketClientThunder;
import com.hinasch.unlsaga.network.packet.PacketClientThunder.PacketClientThunderHandler;
import com.hinasch.unlsaga.network.packet.PacketGuiButton;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew.PacketGuiButtonHandler;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;
import com.hinasch.unlsaga.network.packet.PacketLPNew;
import com.hinasch.unlsaga.network.packet.PacketLPNew.PacketLPHandler;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.network.packet.PacketParticle.PacketParticleHandler;
import com.hinasch.unlsaga.network.packet.PacketPipeline;
import com.hinasch.unlsaga.network.packet.PacketSkill;
import com.hinasch.unlsaga.network.packet.PacketSound;
import com.hinasch.unlsaga.network.packet.PacketSyncDebuff;
import com.hinasch.unlsaga.network.packet.PacketSyncDebuff.PacketSyncDebuffHandler;
import com.hinasch.unlsaga.network.packet.PacketSyncGui;
import com.hinasch.unlsaga.network.packet.PacketSyncTag;
import com.hinasch.unlsaga.stat.UnsagaAchievements;
import com.hinasch.unlsaga.util.ScannerPool;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsaga.villager.bartering.MerchandiseLibrary;
import com.hinasch.unlsaga.villager.smith.MaterialLibrary;
import com.hinasch.unlsagamagic.UnsagaMagic;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Unsaga.MODID, name = Unsaga.NAME, version=Unsaga.VERSION,guiFactory="com.hinasch.unlsaga.init.ModGuiFactoryUnsaga")
public class Unsaga {
	@SidedProxy(modId= Unsaga.MODID,clientSide = "com.hinasch.unlsaga.client.ClientProxy", serverSide = "com.hinasch.unlsaga.network.CommonProxy")
	public static CommonProxy proxy;
	@Instance(Unsaga.MODID)
	public static Unsaga instance;
	public static final String MODID = "hinasch.unsaga";
	public static final String NAME = "Unsaga Mod";
	public static final String VERSION = "1.0 MC1.7.2";
	public static final String DOMAIN = "hinasch.unlsaga";
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	public static final ScannerPool scannerPool = new ScannerPool();

	public static boolean debug = true;


	public static Translation translation;

	public static class guiNumber{
		public static final int EQUIP = 1;
		public static final int SMITH= 2;
		public static final int BARTERING = 3;
		public static final int BLENDER = 4;
		public static final int CHEST = 5;
		public static final int SKILLPANEL = 6;
		public static final int CARRIER = 7;
	}




	public static AbilityRegistry abilityManager;
	public static CreativeTabs tabMain;
	public static CreativeTabs tabSkillPanels;

	public static final SimpleNetworkWrapper packetDispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static MaterialLibrary materialLibrary;
	public static MerchandiseLibrary merchandiseLibrary;
	public static EventManager eventManager ;
	public static Debuffs debuffManager;
	public static UnsagaBlocks blocks;
	public static UnsagaItems items;
	public static UnsagaRecipes recipes;
	public static UnsagaConfigs configs;
	public static LPManager lpManager;
	public static Configuration configFile;
	public static UnsagaMaterials materialManager;
	public static NoFunctionItems noFunctionItems;
	public static BlockDataUnsaga blockOreData;
	public static EntityDataManager entitiesData;
	public static UnsagaMagic magic = UnsagaMagic.getInstance();
	public static UnsagaAchievements achievements;
	public static SkillPanels skillPanels;
	public static DebugLog logger;
	

	@EventHandler
	public void registerCommands(FMLServerStartingEvent  e){
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager manager = server.getCommandManager();
		ServerCommandManager smanager = (ServerCommandManager)manager;
		smanager.registerCommand(new CommandAbility());
	}
	//protected static Optional<UnsagaMagicHandler> module = Optional.absent();
	//public static LPHandlerEmpty lpHandler = new LPHandlerEmpty();
	//基本情報のロード、イベントのレジスターなど
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		this.logger = new DebugLog(NAME);
		//config.buildConfiguration(event.getSuggestedConfigurationFile());

		this.configFile = new Configuration(event.getSuggestedConfigurationFile());

		this.configFile.load();

		debug = false;
		this.configs = new UnsagaConfigs();
		this.configs.init(this.configFile);
		checkLoadedMods();
		this.logger.setDebug(debug);
		
		this.materialManager = new UnsagaMaterials();
		this.materialManager.init();

		this.noFunctionItems = new NoFunctionItems();
		this.blockOreData = new BlockDataUnsaga();

		this.blocks = new UnsagaBlocks();
		this.items = new UnsagaItems();
		this.recipes = new UnsagaRecipes();

		this.merchandiseLibrary  = new MerchandiseLibrary();
		materialLibrary = new MaterialLibrary();
		tabMain = SimpleCreativeTab.getSimpleCreativeTab("tabUnsaga");
		tabSkillPanels = SimpleCreativeTab.getSimpleCreativeTab("tabSkillPanels");
		
		
		this.translation = Translation.getInstance();
		abilityManager = AbilityRegistry.getInstance();
		debuffManager = new Debuffs();
		debuffManager.init();

		
		blocks.loadConfig(this.configFile);
		blocks.registerBlocksAndTileEntity();

		this.skillPanels = new SkillPanels();
		this.skillPanels.init();

		items.loadConfig(configFile);
		items.register();
		SimpleCreativeTab.setIconItem(tabMain, items.getItemStack(ToolCategory.AXE, Unsaga.materialManager.damascus,1,0).getItem());
		SimpleCreativeTab.setIconItem(tabSkillPanels, items.skillPanels);

			Unsaga.magic.init();
			Unsaga.magic.initItem(configFile);
		

		noFunctionItems.load();

		configFile.save();

		this.entitiesData = new EntityDataManager();
		this.entitiesData.registerEntities();
		proxy.registerRenderers();
		proxy.setDebugUnsaga();


		this.eventManager = new EventManager();
		this.eventManager.register();




			Unsaga.magic.registerEvents();
		
		//(new ForgeEventRegistry()).registerEvent();

		//NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		if(this.debug){

		}

		if(this.debug){
			VillagerRegistry.instance().registerVillagerId(this.configs.villagerID_Carrier);
			VillagerRegistry.instance().registerVillagerSkin(this.configs.villagerID_Carrier, new ResourceLocation(DOMAIN,"textures/entity/carrier.png"));
		}

		
		this.lpManager = new LPManager();
		this.achievements = new UnsagaAchievements();
		this.achievements.init();


		
		//GameRegistry.registerWorldGenerator(new DwarvenWorldGenerator());
		//GameRegistry.registerCraftingHandler(new DwarvenCraftingHandler());
	}

	//レシピやローカライズなど。
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		//MaterialLibrary.init();
		proxy.registerKeyHandler();

		noFunctionItems.registerOreDict();
		blockOreData.registerSmeltingAndAssociation();
		recipes.register();

		packetDispatcher.registerMessage(PacketClientThunderHandler.class, PacketClientThunder.class, 0, Side.CLIENT);
		packetDispatcher.registerMessage(PacketLPHandler.class, PacketLPNew.class, 1, Side.CLIENT);
		packetDispatcher.registerMessage(PacketSyncDebuffHandler.class, PacketSyncDebuff.class, 2, Side.CLIENT);
		packetDispatcher.registerMessage(PacketParticleHandler.class, PacketParticle.class, 3, Side.CLIENT);
		packetDispatcher.registerMessage(PacketGuiButtonHandler.class, PacketGuiButtonNew.class, 4, Side.SERVER);
		GameRegistry.registerWorldGenerator(new WorldGeneratorUnsaga(), 6);

		//(new OreDictRegistry()).register();
		//(new ForgeChestHooks()).addLoot();
		//(new LocalizationRegistry()).addLocalization();
		//(new RecipeRegistry()).addRecipe();
		packetPipeline.init("hinasch.unsagamod");
		packetPipeline.registerPacket(PacketGuiOpen.class);

		packetPipeline.registerPacket(PacketGuiButton.class);
		packetPipeline.registerPacket(PacketSkill.class);
		packetPipeline.registerPacket(PacketSound.class);


		packetPipeline.registerPacket(PacketSyncTag.class);

		packetPipeline.registerPacket(PacketSyncGui.class);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){

		packetPipeline.postInitialise();
	}

	public void checkLoadedMods(){

		String className1[] = {
				"hinasch.mods.Debug","hinasch.mods.unlsagamagic.UnsagaMagic"
		};
		String cn = null;
		for(int i=0;i<className1.length;i++){
			try{
				//cn = getClassName(className1[i]);
				cn = className1[i];
				System.out.println(cn);
				//cn="realterrainbiomes.mods.RTBiomesCore";
				cn = ""+Class.forName(cn);
				System.out.println(cn+"is ok.");
				switch(i){
				case 0:
					debug = true;
					break;
				case 1:
					//configs.enableSpells = true;
					//this.module = Optional.of(new UnsagaMagicHandler());
					break;
				}
			}catch(ClassNotFoundException e){

			}
		}

		


	}

	//must check configs.ismagicenabled before
//	public static UnsagaMagicHandler getModuleMagicHandler(){
//		if(Unsaga.module.isPresent()){
//			return Unsaga.module.get();
//		}
//		return null;
//	}


	public static void debug(Object par1){

		logger.log(par1);
	}

	public static void debug(Object... par1){


		logger.log(par1);
	}
	
	public static void debug(Object par1,Class parent){
		logger.log(par1, parent);
	}



}
