package com.hinasch.unlsaga.material;



import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.google.common.base.Optional;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;

public class UnsagaMaterials {

	//銅鉱石などをベースに武器にしたあとその武器がベースにできない」問題を解決する
	public final HashMap<String,UnsagaMaterial> allMaterials;
	
	public final UnsagaMaterial categorywood;
	public final UnsagaMaterial wood,oak,tonerico,spruce,birch,jungleWood,acacia,darkOak;

	public final UnsagaMaterial categoryStone;
	public final UnsagaMaterial serpentine;
	public final UnsagaMaterial diorite,granite,andesite;
	
	public final UnsagaMaterial copperOre;
	public final UnsagaMaterial debris;
	public final UnsagaMaterial debris2;
	public final UnsagaMaterial quartz	;
	public final UnsagaMaterial meteorite;	
	public final UnsagaMaterial ironOre;
	
	public final UnsagaMaterial feather;

	public final UnsagaMaterial metals;
	public final UnsagaMaterial iron,copper,silver,meteoricIron,lead;
	
	public final UnsagaMaterial steels;
	public final UnsagaMaterial steel1,steel2;
	
	public final UnsagaMaterial obsidian;
	
	public final UnsagaMaterial bestial;
	public final UnsagaMaterial topaz;
	public final UnsagaMaterial carnelian;
	public final UnsagaMaterial opal;
	public final UnsagaMaterial ravenite;
	public final UnsagaMaterial lazuli;	

	public final UnsagaMaterial corundums;	
	public final UnsagaMaterial corundum1;	
	public final UnsagaMaterial corundum2;	
	public final UnsagaMaterial diamond;	
	public final UnsagaMaterial fairieSilver;		
	public final UnsagaMaterial damascus;	
	
	public final UnsagaMaterial cloth;
	public final UnsagaMaterial cotton;
	public final UnsagaMaterial silk;
	public final UnsagaMaterial velvet;
	public final UnsagaMaterial liveSilk;

	public final UnsagaMaterial angelite;
	public final UnsagaMaterial demonite;
	
	public final UnsagaMaterial leathers;
	public final UnsagaMaterial bone;
	public final UnsagaMaterial fur;
	public final UnsagaMaterial hydra;
	public final UnsagaMaterial crocodileLeather;

	public final UnsagaMaterial dragonHeart;
	public final UnsagaMaterial sivaQueen;
	
	public final UnsagaMaterial failed;
	
	public final UnsagaMaterial dummy;	
	
	public final Map<String,ToolMaterial> toolMaterialList;
	public final Map<String,ArmorMaterial> armorMaterialList ;
	
	
	
	public final ToolMaterial def_toolMaterial = EnumHelper.addToolMaterial("unsaga.default", 1, 131, 4.0F, 1.0F, 22);
	public final ArmorMaterial def_armorMaterial = EnumHelper.addArmorMaterial("unsaga.default", 14, new int[]{2, 6, 5, 2}, 6);
	
	public final ItemInitData toolMaterialData;
	
	public UnsagaMaterials(){
		allMaterials = new HashMap();
		this.toolMaterialList = new HashMap();
		this.armorMaterialList = new HashMap();
		this.toolMaterialData = new ItemInitData();
		
		categorywood = new UnsagaMaterial("categorywood",1,0).addToMap(allMaterials);
		wood = categorywood.addSubMaterial(new UnsagaMaterial("wood",1,0)).setBasePrice(35).addToMap(allMaterials);
		oak = categorywood.addSubMaterial(new UnsagaMaterial("oak",3,1)).setBasePrice(38).addToMap(allMaterials);
		tonerico = categorywood.addSubMaterial(new UnsagaMaterial("toneriko",2,7)).setBasePrice(525).addToMap(allMaterials);
		spruce = categorywood.addSubMaterial(new UnsagaMaterial("spruce",1,3)).setBasePrice(35).addToMap(allMaterials);
		jungleWood = categorywood.addSubMaterial(new UnsagaMaterial("jungleWood",1,3)).setBasePrice(35).addToMap(allMaterials);
		birch = categorywood.addSubMaterial(new UnsagaMaterial("birch",1,3)).setBasePrice(38).addToMap(allMaterials);
		acacia = categorywood.addSubMaterial(new UnsagaMaterial("acacia",1,4)).setBasePrice(40).addToMap(allMaterials);
		darkOak = categorywood.addSubMaterial(new UnsagaMaterial("darkOak",1,4)).setBasePrice(40).addToMap(allMaterials);
		
		categoryStone = new UnsagaMaterial("stone",5,1).addToMap(allMaterials);
		serpentine = categoryStone.addSubMaterial(new UnsagaMaterial("serpentine",5,3)).setBasePrice(15).addToMap(allMaterials);
		diorite = categoryStone.addSubMaterial(new UnsagaMaterial("diorite",5,4)).setBasePrice(35).addToMap(allMaterials);
		andesite = categoryStone.addSubMaterial(new UnsagaMaterial("andesite",5,4)).setBasePrice(35).addToMap(allMaterials);
		granite = categoryStone.addSubMaterial(new UnsagaMaterial("granite",5,4)).setBasePrice(35).addToMap(allMaterials);
		
		
		copperOre = categoryStone.addSubMaterial(new UnsagaMaterial("copperOre",6,3)).setBasePrice(250).addToMap(allMaterials);	
		debris = categoryStone.addSubMaterial(new UnsagaMaterial("debris",4,1)).setBasePrice(15).addToMap(allMaterials);
		debris2 = categoryStone.addSubMaterial(new UnsagaMaterial("debris2",4,1)).setBasePrice(16).addToMap(allMaterials);
		quartz = categoryStone.addSubMaterial(new UnsagaMaterial("quartz",4,5)).setBasePrice(300).addToMap(allMaterials);	
		meteorite = categoryStone.addSubMaterial(new UnsagaMaterial("meteorite",5,8)).setBasePrice(3200).addToMap(allMaterials);	
		ironOre = categoryStone.addSubMaterial(new UnsagaMaterial("ironOre",6,5)).setBasePrice(350).addToMap(allMaterials);	
		
		feather = new UnsagaMaterial("feather",0,4).setBasePrice(90).addToMap(allMaterials);

		metals = new UnsagaMaterial("metal",4,4).addToMap(allMaterials);
		iron = metals.addSubMaterial(new UnsagaMaterial("iron",5,5)).setBasePrice(500).addToMap(allMaterials);
		copper = metals.addSubMaterial(new UnsagaMaterial("copper",6,4)).setBasePrice(300).addToMap(allMaterials);
		silver = metals.addSubMaterial(new UnsagaMaterial("silver",3,5)).setBasePrice(280).addToMap(allMaterials);
		meteoricIron = metals.addSubMaterial(new UnsagaMaterial("meteoricIron",4,8)).setBasePrice(3500).addToMap(allMaterials);
		lead = metals.addSubMaterial(new UnsagaMaterial("lead",8,4)).setBasePrice(350).addToMap(allMaterials);
		
		steels = new UnsagaMaterial("steels",5,7).addToMap(allMaterials);
		steel1 = steels.addSubMaterial(new UnsagaMaterial("steel1",5,7)).setBasePrice(1800).addToMap(allMaterials);
		steel2 = steels.addSubMaterial(new UnsagaMaterial("steel2",5,8)).setBasePrice(4000).addToMap(allMaterials);
		
		obsidian = new UnsagaMaterial("obsidian",8,7).setBasePrice(500).addToMap(allMaterials);
		
		bestial = new UnsagaMaterial("bestial",3,4).addToMap(allMaterials);
		topaz = bestial.addSubMaterial(new UnsagaMaterial("topaz",3,4)).setBasePrice(200).addToMap(allMaterials);
		carnelian = bestial.addSubMaterial(new UnsagaMaterial("carnelian",3,4)).setBasePrice(210).addToMap(allMaterials);
		opal = bestial.addSubMaterial(new UnsagaMaterial("opal",3,4)).setBasePrice(208).addToMap(allMaterials);
		ravenite = bestial.addSubMaterial(new UnsagaMaterial("ravenite",3,4)).setBasePrice(205).addToMap(allMaterials);
		lazuli = bestial.addSubMaterial(new UnsagaMaterial("lazuli",3,4)).setBasePrice(214).addToMap(allMaterials);	

		corundums = new UnsagaMaterial("corundums",3,7).addToMap(allMaterials);	
		corundum1 = corundums.addSubMaterial(new UnsagaMaterial("corundum1",3,7)).setBasePrice(2000).addToMap(allMaterials);	
		corundum2 = corundums.addSubMaterial(new UnsagaMaterial("corundum2",3,7)).setBasePrice(2200).addToMap(allMaterials);	
		diamond = new UnsagaMaterial("diamond",3,8).setBasePrice(10000).addToMap(allMaterials);	
		fairieSilver = new UnsagaMaterial("fairieSilver",2,8).setBasePrice(3500).addToMap(allMaterials);		
		damascus = new UnsagaMaterial("damascus",6,9).setBasePrice(6800).addToMap(allMaterials);	
		
		cloth = new UnsagaMaterial("cloth",2,2).addToMap(allMaterials);
		cotton = cloth.addSubMaterial(new UnsagaMaterial("cotton",2,1)).setBasePrice(12).addToMap(allMaterials);
		silk = cloth.addSubMaterial(new UnsagaMaterial("silk",2,3)).setBasePrice(35).addToMap(allMaterials);
		velvet = cloth.addSubMaterial(new UnsagaMaterial("velvet",2,6)).setBasePrice(300).addToMap(allMaterials);
		liveSilk = new UnsagaMaterial("liveSilk",2,9).setBasePrice(6500).addToMap(allMaterials);

		angelite = new UnsagaMaterial("angelite",0,5).setBasePrice(500).addToMap(allMaterials);
		demonite = new UnsagaMaterial("demonite",0,5).setBasePrice(450).addToMap(allMaterials);
		
		leathers = new UnsagaMaterial("leathers",0,5).addToMap(allMaterials);
		bone = new UnsagaMaterial("bone",3,4).setBasePrice(75).addToMap(allMaterials);
		fur = leathers.addSubMaterial(new UnsagaMaterial("fur",3,2)).setBasePrice(25).addToMap(allMaterials);
		hydra = new UnsagaMaterial("hydra",5,8).setBasePrice(3000).addToMap(allMaterials);
		crocodileLeather = leathers.addSubMaterial(new UnsagaMaterial("crocodileLeather",3,5)).setBasePrice(302).addToMap(allMaterials);

		dragonHeart = new UnsagaMaterial("dragonHeart",3,100).setBasePrice(9800).addToMap(allMaterials);
		sivaQueen = new UnsagaMaterial("queenOfSiva",3,100).setBasePrice(8500).addToMap(allMaterials);

		failed = new UnsagaMaterial("failed",3,100).addToMap(allMaterials);
		
		dummy = new UnsagaMaterial("dummy",1,1).addToMap(allMaterials);	

	}
	

	//アイテムと素材に関連するデータ
	public class ItemInitData{
		//適さないリスト
		private Map<ToolCategory,List<UnsagaMaterial>> unsuitedList;
		//適すリスト
		private final Map<ToolCategory,Map<Integer,UnsagaMaterial>> availableMap;
		
		public ItemInitData(){
			this.unsuitedList = new HashMap();
			this.availableMap = new HashMap();
		}
		public void init(){

			availableMap.put(ToolCategory.SWORD, makeSetInOrder(failed,categorywood,categoryStone,bestial,copper,lead,silver,meteoricIron,iron,fairieSilver,dragonHeart,steels,bone,sivaQueen));
			availableMap.put(ToolCategory.SPEAR, makeSetInOrder(failed,categorywood,categoryStone,bestial,copper,silver,lead,meteoricIron,iron,corundums,fairieSilver,obsidian,steels,diamond,dragonHeart,bone,damascus,sivaQueen));
			availableMap.put(ToolCategory.AXE, makeSetInOrder(failed,categoryStone,bestial,copper,silver,lead,meteoricIron,iron,fairieSilver,obsidian,damascus,steels,bone,dragonHeart,sivaQueen));
			availableMap.put(ToolCategory.STAFF, makeSetInOrder(failed,categorywood,categoryStone,bestial,bone,copper,silver,meteoricIron,lead,iron,obsidian,fairieSilver,steels,diamond,corundums,damascus,dragonHeart,sivaQueen));
			availableMap.put(ToolCategory.BOW, makeSetInOrder(failed,categorywood,bone,copper,lead,meteoricIron,iron,steels,fairieSilver,damascus,dragonHeart,sivaQueen));
			availableMap.put(ToolCategory.ACCESSORY, makeSetInOrder(failed,categorywood,categoryStone,bestial,bone,metals,fairieSilver,steels,corundums,obsidian,damascus,diamond,angelite,demonite,sivaQueen));
			availableMap.put(ToolCategory.BOOTS, makeSetInOrder(failed,cloth,liveSilk,fur,silver,meteorite,hydra,fairieSilver,obsidian));
			availableMap.put(ToolCategory.LEGGINS, makeSetInOrder(failed,categoryStone,metals,steels,damascus,bestial));
			availableMap.put(ToolCategory.ARMOR, makeSetInOrder(failed,cloth,liveSilk,fur,crocodileLeather,categoryStone,metals,hydra,steels,obsidian,bestial,damascus));
			availableMap.put(ToolCategory.HELMET, makeSetInOrder(failed,categoryStone,metals,steels,damascus,fairieSilver,feather,obsidian,corundums,hydra,diamond,bestial));
		}
		
		public Map<Integer,UnsagaMaterial> getAvailableSet(ToolCategory bow){
			return availableMap.get(bow);
		}

		public List<UnsagaMaterial> getUnsuited(ToolCategory category){
			return unsuitedList.get(category);
		}

		private Map<Integer,UnsagaMaterial> makeSetInOrder(UnsagaMaterial... materials){
			Map<Integer,UnsagaMaterial> map = new HashMap();
			for(int i=0;i<materials.length;i++){
				map.put(i,materials[i]);
			}
			return map;
			
			
		}
	}

	
	public void init(){
		cloth.setArmorMaterial(Items.leather_chestplate.getArmorMaterial());
		liveSilk.setArmorMaterial(addNewArmorMaterial("liveSilk",26,new int[]{3, 8, 6, 3},30));
		fur.setArmorMaterial(ArmorMaterial.CLOTH);
		crocodileLeather.setArmorMaterial(ArmorMaterial.CLOTH);
		crocodileLeather.setSpecialArmorTexture(ToolCategory.HELMET, "headband", "armor2");
		categorywood.setToolMaterial(ToolMaterial.WOOD).setArmorMaterial(def_armorMaterial);
		categorywood.setIconKey("wood");
		tonerico.setToolMaterial(addNewToolMaterial(tonerico.name,1,120,2.0F,1.0F,30)).setBowModifier(1);
		categoryStone.setToolMaterial(ToolMaterial.STONE);
		categoryStone.setArmorMaterial(addNewArmorMaterial("armor_stone",5,new int[]{2,6,5,2},1));
		categoryStone.setIconKey("stone").setSpecialArmorTexture(ToolCategory.HELMET, "mask", "armor2");
		metals.setToolMaterial(ToolMaterial.IRON).setArmorMaterial(addNewArmorMaterial("unsaga.metal",14, new int[]{2, 6, 5, 2}, 6));;
		metals.setBowModifier(1);
		copper.setToolMaterial(addNewToolMaterial(copper.name,2, 225, 6.0F, 2.0F, 10));
		copper.setArmorMaterial(addNewArmorMaterial("unsaga.Copper",14, new int[]{2, 6, 5, 2}, 6));
		silver.setToolMaterial(addNewToolMaterial(silver.name,1, 131, 4.0F, 1.0F, 22));
		silver.setArmorMaterial(addNewArmorMaterial("unsaga.Silver",8, new int[]{2, 5, 4, 1}, 22));
		meteoricIron.setToolMaterial(addNewToolMaterial(meteoricIron.name,2, 450, 7.0F, 3.0F, 17));
		meteoricIron.setAttackModifier(3.0F);
		quartz.setAttackModifier(2.0F);
		tonerico.setAttackModifier(1.0F);
		fairieSilver.setToolMaterial(addNewToolMaterial(fairieSilver.name,2, 500, 4.0F, 2.0F, 30));
		fairieSilver.setBowModifier(2).setArmorMaterial(addNewArmorMaterial("fairieSilver",15, new int[]{2, 6, 5, 2}, 32));
		corundums.setToolMaterial(addNewToolMaterial(corundums.name,2, 1000, 7.0F, 4.0F, 20));
		corundums.setArmorMaterial(addNewArmorMaterial("corundum",20, new int[]{3, 7, 6, 3}, 18));
		bestial.setToolMaterial(addNewToolMaterial(bestial.name,1, 131, 4.0F, 1.0F, 15));
		bestial.setArmorMaterial(ArmorMaterial.IRON);

		steels.setToolMaterial(addNewToolMaterial(steels.name,2, 500, 7.0F, 3.0F, 16));
		steels.setBowModifier(2);
		damascus.setToolMaterial(addNewToolMaterial(damascus.name,3, 1400, 8.0F, 4.0F, 14));
		damascus.setBowModifier(3).setArmorMaterial(addNewArmorMaterial("unsaga.Damascus",32,new int[]{3, 8, 6, 3},9));
		damascus.addSuitedModifier(ToolCategory.AXE, 1.5F);
		damascus.addSuitedModifier(ToolCategory.SPEAR, 1.0F);
		steels.setIconKey("steel").setArmorMaterial(addNewArmorMaterial("unsaga.Steel",20, new int[]{3, 7, 6, 3}, 12));
		diamond.setToolMaterial(ToolMaterial.EMERALD).setArmorMaterial(ArmorMaterial.DIAMOND);
		obsidian.setToolMaterial(addNewToolMaterial(obsidian.name,1, 1000, 4.0F, 1.0F, 2));
		obsidian.setSpecialArmorTexture(ToolCategory.HELMET, "mask", "armor2");
		lead.setToolMaterial(addNewToolMaterial(lead.name,2, 350, 6.0F, 2.0F, 5));
		dragonHeart.setToolMaterial(addNewToolMaterial(dragonHeart.name,2, 1400, 7.0F, 3.0F, 20));
		failed.setToolMaterial(addNewToolMaterial(failed.name,1, 50, 1.0F, 1.0F, 1));
		failed.setArmorMaterial(addNewArmorMaterial("failed",5, new int[]{1, 1, 1, 1}, 1));
		sivaQueen.setToolMaterial(addNewToolMaterial(sivaQueen.getName(),3,1600,8.5F,3.0F,50));
		sivaQueen.setArmorMaterial(addNewArmorMaterial("sivaQueen",30, new int[]{3, 7, 6, 3}, 40));
		sivaQueen.setBowModifier(2);
		sivaQueen.addSuitedModifier(ToolCategory.AXE, 1.0F);
		sivaQueen.addSuitedModifier(ToolCategory.SPEAR, 1.0F);
		setIcons();
		setRenderColor();
		setItemAssociated();
		setSpecialName();
	}
	


	
	protected void setSpecialName(){
//		carnelian.setSpecialName(EnumUnsagaTools.ARMOR, "*,朱雀石の鎧");
//		topaz.setSpecialName(EnumUnsagaTools.ARMOR, "*,黄龍石の鎧");
//		opal.setSpecialName(EnumUnsagaTools.ARMOR, "*,白虎石の鎧");
//		lazuli.setSpecialName(EnumUnsagaTools.ARMOR, "*,蒼龍石の鎧");
//		ravenite.setSpecialName(EnumUnsagaTools.ARMOR, "*,玄武石の鎧");
//		meteorite.setSpecialName(EnumUnsagaTools.BOOTS, "Star Boots,星辰の石靴");
//		meteoricIron.setSpecialName(EnumUnsagaTools.BOOTS, "Cosmic Leggins,コスモレガース");
//		meteoricIron.setSpecialName(EnumUnsagaTools.HELMET, "Cosmic Helmet,コスモヘルム");
//		silver.setSpecialName(EnumUnsagaTools.HELMET, "Silver Ring,シルバーリング");
//		silver.setSpecialName(EnumUnsagaTools.BOOTS, "Silver Boots,シルバーブーツ");
	}
	
	protected void setItemAssociated(){
		lazuli.associate(new ItemStack(Items.dye,1,4));
		quartz.associate(new ItemStack(Items.quartz,1));
		fur.associate(new ItemStack(Items.leather,1));
		bone.associate(new ItemStack(Items.bone,1));
		obsidian.associate(new ItemStack(Blocks.obsidian,1));
		silk.associate(new ItemStack(Items.string,1));
		ironOre.associate(new ItemStack(Blocks.iron_ore,1));

	}
	
	protected void setRenderColor(){
		damascus.setRenderColor(0x726250);
		crocodileLeather.setRenderColor(0x8f2e14);
		metals.setRenderColor(0x949495);
		categoryStone.setRenderColor(0xadadad);
		tonerico.setRenderColor(0xa59564);
		oak.setRenderColor(0xbfa46f);
		categorywood.setRenderColor(0xbfa46f);
		copper.setRenderColor(0xc37854);
		fairieSilver.setRenderColor(0xa6a5c4);
		meteoricIron.setRenderColor(0xc0c6c9);
		bestial.setRenderColor(0xadadad);
		carnelian.setRenderColor(0xc9171e);
		topaz.setRenderColor(0xdccb18);
		opal.setRenderColor(0xeaf4fc);
		lazuli.setRenderColor(0x192f60);
		ravenite.setRenderColor(0xdcdddd);
		corundum1.setRenderColor(0xba2636);
		corundum2.setRenderColor(0x19448e);
		obsidian.setRenderColor(0x0d0015);
		fur.setRenderColor(0x583822);
		lead.setRenderColor(0x7b7c7d);
		iron.setRenderColor(0xafafb0);
		diamond.setRenderColor(0x89c3eb);
		steels.setRenderColor(0x595857);
		dragonHeart.setRenderColor(0x16160e);
		sivaQueen.setRenderColor(0xc71585);
	}
	
	protected void setIcons(){
		crocodileLeather.setSpecialIcon(ToolCategory.HELMET,"band");
		cloth.setSpecialIcon(ToolCategory.HELMET,"bandana");
		cloth.setSpecialIcon(ToolCategory.BOOTS,"socksicon");
		liveSilk.setSpecialIcon(ToolCategory.HELMET,"bandana");
		liveSilk.setSpecialIcon(ToolCategory.BOOTS,"socksicon");
		bone.setSpecialIcon(ToolCategory.ACCESSORY,"crossbone" );
		damascus.setSpecialIcon(ToolCategory.ACCESSORY, "breslet_damascus");
		obsidian.setSpecialIcon(ToolCategory.ACCESSORY, "breslet_damascus");
		diamond.setSpecialIcon(ToolCategory.ACCESSORY, "ring_dia");
		corundum1.setSpecialIcon(ToolCategory.ACCESSORY, "ring_ruby");
		corundum2.setSpecialIcon(ToolCategory.ACCESSORY, "ring_sapphire");
		meteorite.setSpecialIcon(ToolCategory.ACCESSORY, "amulet_meteorite");
		meteoricIron.setSpecialIcon(ToolCategory.ACCESSORY, "amulet_meteorite");
		fairieSilver.setSpecialIcon(ToolCategory.HELMET, "ringhelmet");
		silver.setSpecialIcon(ToolCategory.HELMET, "ringhelmet");
	}
	
	public UnsagaMaterial getMaterial(String name){
		Optional<UnsagaMaterial> mat = Optional.of(allMaterials.get(name));
		return mat.get();
	}
	
	public ToolMaterial addNewToolMaterial(String name,int harvest,int maxuse,float efficiency,float damage,int enchant){
		toolMaterialList.put(name, EnumHelper.addToolMaterial(name, harvest, maxuse, efficiency, damage,enchant));
		return toolMaterialList.get(name);
	}
	
	public ArmorMaterial addNewArmorMaterial(String name,int armor,int[] reduces,int enchant){
		armorMaterialList.put(name, EnumHelper.addArmorMaterial(name, armor, reduces, enchant));
		return armorMaterialList.get(name);
	}
	
	public Collection<UnsagaMaterial> getAllMaterialValues(){
		return this.allMaterials.values();
	}
}
