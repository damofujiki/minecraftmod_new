package com.hinasch.unlsagamagic;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

import com.hinasch.lib.SimpleCreativeTab;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.villager.bartering.MerchandiseLibraryBook;
import com.hinasch.unlsagamagic.block.BlockFireWall;
import com.hinasch.unlsagamagic.element.ElementLibrary;
import com.hinasch.unlsagamagic.element.WorldElements;
import com.hinasch.unlsagamagic.event.EventManager;
import com.hinasch.unlsagamagic.item.ItemTablet;
import com.hinasch.unlsagamagic.item.UnsagaMagicItems;
import com.hinasch.unlsagamagic.spell.HelperDeciphProcess;
import com.hinasch.unlsagamagic.spell.Spells;
import com.hinasch.unlsagamagic.tileentity.TileEntityFireWall;

import cpw.mods.fml.common.registry.GameRegistry;


public class UnsagaMagic {

	public static UnsagaMagic instance;
	public WorldElements elementCalculator;
	public UnsagaMagicItems items;
	public EventManager events;
	
	public ElementLibrary elementLibrary;
	public HelperDeciphProcess deciphProcess;
	public Block blockFireWall;

	public Spells spellManager;
	
	public CreativeTabs tabMagic;
	

	
	protected UnsagaMagic(){
		
	}
	
	public static UnsagaMagic getInstance(){
		if(instance==null){
			instance = new UnsagaMagic();
		}
		return instance;
	}
	
	public void init(){
		
		this.elementLibrary = new ElementLibrary();
		this.elementCalculator = WorldElements.getInstance();
		this.items = new UnsagaMagicItems();
		this.tabMagic = SimpleCreativeTab.getSimpleCreativeTab("tabUnsagaMagic");
		spellManager = Spells.getInstance();
		spellManager.init();

		this.deciphProcess = new HelperDeciphProcess();
		this.events = new EventManager();
	}
	
	public void registerEvents(){

		this.events.register();
	}
	

	public void initItem(Configuration config) {
		
//		PropertyCustom prop = new PropertyCustom(new String[]{"itemID.tablet","itemID.spellBook","itemID.Blender"});
//		PropertyCustom prop2 = new PropertyCustom(new String[]{"blockID.FireWall"});
//		
//		prop.setValues(new Integer[]{1603,1604,1605});
//		prop2.setValues(new Integer[]{1606});
//
//		prop.setCategoriesAll(config.CATEGORY_GENERAL);
//		prop2.setCategoriesAll(config.CATEGORY_GENERAL);
//
//		prop.buildProps(config);
//		prop2.buildProps(config);

//		itemIDMagicTablet = prop.getProp(0).getInt();
//		itemIDSpellBook = prop.getProp(1).getInt();
//		itemIDBlender = prop.getProp(2).getInt();
//		blockIDFireWall = prop2.getProp(0).getInt();
		

		

		blockFireWall = new BlockFireWall().setCreativeTab(tabMagic).setBlockName("unsaga.firewall").setLightLevel(1.0F);
		
		this.items.register();

		
		GameRegistry.registerTileEntity(TileEntityFireWall.class, "unsaga.firewall");
		GameRegistry.registerBlock(blockFireWall,"blockFireWall");
		
		Unsaga.merchandiseLibrary.addShelf(new MerchandiseLibraryBook(items.magicTablet,900));
		Unsaga.merchandiseLibrary.addShelf(new MerchandiseLibraryBook(items.spellBook,300));
		Unsaga.merchandiseLibrary.addShelf(new MerchandiseLibraryBook(items.blender,2000));
		

		SimpleCreativeTab.setIconItem(tabMagic,  ItemTablet.getDisplayMagicTablet(tabMagic).getItem());
	}
}
