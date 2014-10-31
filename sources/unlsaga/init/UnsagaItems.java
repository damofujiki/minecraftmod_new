package com.hinasch.unlsaga.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hinasch.lib.base.ItemCustomEntityEgg;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.entity.EntityGolemUnsaga;
import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.armor.ItemAccessory;
import com.hinasch.unlsaga.item.armor.ItemArmorUnsaga;
import com.hinasch.unlsaga.item.misc.ItemBullet;
import com.hinasch.unlsaga.item.misc.ItemIngotsUnsaga;
import com.hinasch.unlsaga.item.misc.ItemSkillBook;
import com.hinasch.unlsaga.item.panel.ItemSkillPanel;
import com.hinasch.unlsaga.item.tool.ItemPickaxeUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemAxeUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemBowUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemGunUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemSpearUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemStaffUnsaga;
import com.hinasch.unlsaga.item.weapon.ItemSwordUnsaga;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.material.UnsagaMaterials;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

import cpw.mods.fml.common.registry.GameRegistry;

public class UnsagaItems {

	public Item magicTablet;
	public Item spellBook;
	public Item itemToolsHS;
	public Item musket;
	public Item spellBlender;
	public Item itemKey;
	public Item ammo;
	public Item materials;
	public Item entityEgg;
	public Item skillPanels;
	public Item skillBook;
	
	protected UnsagaToolInitializer swordInitializer;
	protected UnsagaToolInitializer axeInitializer;
	protected UnsagaToolInitializer staffInitializer;
	protected UnsagaToolInitializer bowInitializer;
	protected UnsagaToolInitializer spearInitializer;
	protected UnsagaToolInitializer armorInitializer;
	protected UnsagaToolInitializer bootsInitializer;
	protected UnsagaToolInitializer legginsInitializer;
	protected UnsagaToolInitializer helmetInitializer;
	protected UnsagaToolInitializer acsInitializer;
	protected UnsagaToolInitializer pickaxeInitializer;
	

	protected Map<String,Item> itemsMap;
	protected Item[] swords = new Item[30];
	protected Item[] axes = new Item[30];
	protected Item[] staffs = new Item[30];
	protected Item[] spears = new Item[30];
	protected Item[] knives = new Item[30];
	protected Item[] accessories = new Item[50];
	protected Item[] armors = new Item[80];
	protected Item[] bows = new Item[30];
	protected Item[] pickaxes = new Item[30];

	protected static ArrayList<UnsagaItems.Key> kiesPIckedUp;

	protected UnsagaMaterials materialManager;

	public static enum EnumSelecterItem {BOWONLY,WEAPONONLY,MERCHANDISE,ALL};
	
	public UnsagaItems(){
	
		this.itemsMap = new HashMap();
		this.materialManager = Unsaga.materialManager;
	}
	
	@Deprecated
	public void loadConfig(Configuration config){
//		PropertyCustom prop = new PropertyCustom(new String[]{"itemIDs.Swords","itemIDs.Axes","itemIDs.Staffs"
//				,"itemIDs.Spears","itemIDs.Bows","itemIDs.Accessories","itemIDs.Armors","itemID.Ingots","itemID.Barrett"
//				,"itemID.Gun"});
//
//		prop.setValues(new Integer[]{1200,1230,1260,1290,1320,1370,1450,1600,1601,1602});
//
//		prop.setCategoriesAll(config.CATEGORY_GENERAL);
//
//		
//		prop.buildProps(config);

//		itemIDsSwords = prop.getProp(0).getInt();
//		itemIDsAxes = prop.getProp(1).getInt();
//		itemIDsStaffs = prop.getProp(2).getInt();
//		itemIDsSpears = prop.getProp(3).getInt();
//		itemIDsBows = prop.getProp(4).getInt();
//		itemIDsAccessories = prop.getProp(5).getInt();
//		itemIDsArmors = prop.getProp(6).getInt();
//		itemIDIngotsUnsaga = prop.getProp(7).getInt();
//		itemIDBarrett = prop.getProp(8).getInt();
//		itemIDGun = prop.getProp(9).getInt();

	}

	public void register(){
		Unsaga.materialManager.toolMaterialData.init();
		//FileObject file = new FileObject("i:\\test.txt");
		//file.openForOutput();
		swordInitializer = new UnsagaToolInitializer();
//		swordInitializer.setNoParticles(Sets.newHashSet(UnsagaMaterials.stone,UnsagaMaterials.wood));
//		swordInitializer.setNoUseParentNames(Sets.newHashSet(UnsagaMaterials.metals));
//		swordInitializer.setHooter("en_US", "Sword").setHooter("ja_JP", "剣");
		swordInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.SWORD));
		swordInitializer.register(swords, ItemSwordUnsaga.class, "sword");

		
		axeInitializer = new UnsagaToolInitializer();
//		axeInitializer.setHooter("en_US", "Axe").setHooter("ja_JP", "斧");
//		axeInitializer.setNoParticles(Sets.newHashSet(UnsagaMaterials.bone,UnsagaMaterials.stone));
//		axeInitializer.setNoUseParentNames(Sets.newHashSet(UnsagaMaterials.metals));
		axeInitializer.setAvailableMaterial(Unsaga.materialManager.toolMaterialData.getAvailableSet(ToolCategory.AXE));
		axeInitializer.register(axes, ItemAxeUnsaga.class, "axe");
		
		spearInitializer = new UnsagaToolInitializer();
		spearInitializer.setNoParticles(Sets.newHashSet(materialManager.categoryStone,materialManager.categorywood,materialManager.bone,materialManager.iron));
		spearInitializer.setNoUseParentNames(Sets.newHashSet(materialManager.metals));
		spearInitializer.setHooter("en_US", "Spear").setHooter("ja_JP", "槍");
		spearInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.SPEAR));
		spearInitializer.register(spears, ItemSpearUnsaga.class, "spear");

		staffInitializer = new UnsagaToolInitializer();
		staffInitializer.setHooter("en_US", "Staff").setHooter("ja_JP", "杖");
		staffInitializer.setNoParticles(Sets.newHashSet(materialManager.categoryStone));
		staffInitializer.setNoUseParentNames(Sets.newHashSet(materialManager.metals));
		staffInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.STAFF));
		staffInitializer.register(staffs, ItemStaffUnsaga.class, "staff");
		
		bowInitializer = new UnsagaToolInitializer();
		bowInitializer.setHooter("en_US", "Bow").setHooter("ja_JP", "弓");
		bowInitializer.setNoParticles(Sets.newHashSet(materialManager.metals));
		bowInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.BOW));
		bowInitializer.register(bows, ItemBowUnsaga.class, "bow");

		bootsInitializer = new UnsagaToolInitializer();
		bootsInitializer.setHooter("en_US", "Boots").setHooter("ja_JP", "靴");
		bootsInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.BOOTS));
		bootsInitializer.setArmorData(ToolCategory.BOOTS, 3);
		bootsInitializer.register(armors, ItemArmorUnsaga.class, "boots");
		
		legginsInitializer = new UnsagaToolInitializer();
		legginsInitializer.setHooter("en_US", "Leggins").setHooter("ja_JP", "レギンス");
		legginsInitializer.setUseParentLocalized(true);
		legginsInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.LEGGINS));
		legginsInitializer.setArmorData(ToolCategory.LEGGINS, 2);
		legginsInitializer.register(armors, ItemArmorUnsaga.class, "leggins");

		helmetInitializer = new UnsagaToolInitializer();
		helmetInitializer.setHooter("en_US", "Helmet").setHooter("ja_JP", "兜");
		helmetInitializer.setUseParentLocalized(true);
		helmetInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.HELMET));
		helmetInitializer.setArmorData(ToolCategory.HELMET, 0);
		helmetInitializer.register(armors, ItemArmorUnsaga.class, "helmet");

		
		armorInitializer = new UnsagaToolInitializer();
		armorInitializer.setHooter("en_US", "Armor").setHooter("ja_JP", "鎧");
		armorInitializer.setUseParentLocalized(true);
		armorInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.ARMOR));
		armorInitializer.setArmorData(ToolCategory.ARMOR, 1);
		armorInitializer.register(armors, ItemArmorUnsaga.class, "armor");

		
		acsInitializer = new UnsagaToolInitializer();
		acsInitializer.setHooter("en_US", "Ring").setHooter("ja_JP", "腕輪");
		acsInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.ACCESSORY));
		acsInitializer.register(accessories, ItemAccessory.class, "acs");
		
		//FileObject fo = new FileObject("i:\\test.txt"); //unlocalizedname書き出し用
		//fo.openForOutput();
		pickaxeInitializer = new UnsagaToolInitializer();
		pickaxeInitializer.setAvailableMaterial(materialManager.toolMaterialData.getAvailableSet(ToolCategory.STAFF));
		pickaxeInitializer.register(pickaxes, ItemPickaxeUnsaga.class, "pickaxe");
		//fo.close();
			
		materials = new ItemIngotsUnsaga().setUnlocalizedName("unsaga.ingots").setCreativeTab(Unsaga.tabMain);
		GameRegistry.registerItem(materials, "nofuncitem");
		ammo = new ItemBullet("gunpowder").setUnlocalizedName("unsaga.bullet").setCreativeTab(Unsaga.tabMain);
		GameRegistry.registerItem(ammo, "barrett");
		musket = new ItemGunUnsaga("musket").setUnlocalizedName("unsaga.musket").setCreativeTab(Unsaga.tabMain);
		GameRegistry.registerItem(musket, "musket");
		entityEgg = new ItemCustomEntityEgg().setUnlocalizedName("unsaga.entityEgg").setCreativeTab(Unsaga.tabMain);
		((ItemCustomEntityEgg)entityEgg).addMaping(0, EntityTreasureSlime.class, "treasureSlime", 0xff0000, 0x000000);
		((ItemCustomEntityEgg)entityEgg).addMaping(1, EntityGolemUnsaga.class, "golem", 0xffff00, 0x000000);		
		GameRegistry.registerItem(entityEgg, "unsaga.entityEgg");
		skillPanels = new ItemSkillPanel().setUnlocalizedName("unsaga.skillPanels").setCreativeTab(Unsaga.tabSkillPanels);
		GameRegistry.registerItem(skillPanels, "skillPanel");
		skillBook = new ItemSkillBook().setUnlocalizedName("unsaga.skillBook").setCreativeTab(Unsaga.tabMain);
		GameRegistry.registerItem(skillBook, "skillBook");
	}
	



	public void putItemToMap(Item item,UnsagaItems.Key key){
		Unsaga.debug(key+":"+Item.getIdFromItem(item));
		itemsMap.put(key.get(), item);
	}
	
	public Set<UnsagaItems.Key> getKeySetFromItemsMap(){
		Set<UnsagaItems.Key> newKies = new HashSet();
		for(String k:this.itemsMap.keySet()){
			newKies.add(new Key(k));
		}
		return newKies;
	}

	public Set<UnsagaMaterial> getUnsuitedMaterials(ToolCategory category){
		Set<UnsagaMaterial> availableSet = Sets.newHashSet(materialManager.toolMaterialData.getAvailableSet(category).values());
		Set<UnsagaMaterial> unsuitedSet = new HashSet();
		for(UnsagaMaterial uns:materialManager.allMaterials.values()){
			if(!uns.setContainsThis(availableSet) && uns.isChild){
				unsuitedSet.add(uns);
			}
		}
		Unsaga.debug(unsuitedSet);
		return unsuitedSet;
		
	}
	
	public UnsagaMaterial getRandomMaterial(Set<UnsagaMaterial> set,Random rand){
		List<UnsagaMaterial> list = Lists.newArrayList(set);
		int num = rand.nextInt(list.size());
		return list.get(num);
	}
	public ItemStack getItemStack(ToolCategory axe,UnsagaMaterial material,int stack,int damage){
		String key = buildKeyString(axe,material);
		Optional<Item> itemid = Optional.fromNullable(itemsMap.get(key));
		if(itemid.isPresent()){
			return new ItemStack(itemid.get(),stack,damage);
		}
		if(material.isChild()){
			key = buildKeyString(axe,material.getParentMaterial());
			itemid = Optional.fromNullable(itemsMap.get(key));
			if(itemid.isPresent()){
				return new ItemStack(itemid.get(),stack,damage);
			}
			
		}
		Unsaga.debug("Caution:getItem>null",this.getClass());
		return new ItemStack(Items.stone_sword,stack,damage);

	}
	
	public boolean isForgeMaterial(ToolCategory category,UnsagaMaterial material){
		Unsaga.debug(category,material);
		if(itemsMap.containsKey(buildKeyString(category,material))){
			return true;
		}
		if(material.isChild()){
			UnsagaMaterial parent = material.getParentMaterial();
			if(itemsMap.containsKey(buildKeyString(category,parent))){
				return true;
			}
		}
		return false;
	}
	
	public String buildKeyString(ToolCategory category,UnsagaMaterial material){
		return category.toString() + "." + material.getName();
	}
	//出来損ない武器・防具を取得
	public ItemStack getFailedWeapon(Random rand,ToolCategory category){
		ItemStack failed = getItemStack(category,materialManager.failed,1,0);
		UnsagaMaterial material = getRandomMaterial(getUnsuitedMaterials(category), rand);

		HelperUnsagaItem.initWeapon(failed, material.getName(), material.getWeight());
		return failed;
		
	}

	public ItemStack getRandomWeapon(Random rand, int rankMin,int rankMax,EnumSelecterItem selecter){
		boolean flag = false;
		kiesPIckedUp = new ArrayList();
		for(Iterator<UnsagaItems.Key> ite=this.getKeySetFromItemsMap().iterator();ite.hasNext();){
			UnsagaItems.Key key = ite.next();


			switch(selecter){
			
			case WEAPONONLY:
				if(ToolCategory.weaponSet.contains(key.getCategory())){
					flag = true;
				}
				break;
			case BOWONLY:
				if(key.getCategory().equals(ToolCategory.BOW.toString())){
					flag = true;
				}
				break;
			case MERCHANDISE:
				if(ToolCategory.merchandiseSet.contains(key.getCategory())){
					flag  = true;
				}
				break;
			default:
				flag  = false;
				break;
				
			}
//			if(selecter==EnumSelecterItem.WEAPONONLY){
//				if(ToolCategory.weaponList.contains(keys[0])){
//					flag = true;
//				}else{
//					flag = false;
//				}
//			}
//
//			if(selecter==EnumSelecterItem.BOWONLY){
//				if(!keys[0].equals(ToolCategory.BOW.toString())){
//					flag = false;
//				}
//			}
//			if(selecter==EnumSelecterItem.MERCHANDISE){
//				if(ToolCategory.merchandiseList.contains(keys[0])){
//					flag = true;
//				}else{
//					flag = false;
//				}
//			}
			if(materialManager.getMaterial(key.getMaterial()).rank>=rankMin && materialManager.getMaterial(key.getMaterial()).rank<=rankMax){
				flag = true;
			}else{
				flag = false;
			}
			if(flag){
				kiesPIckedUp.add(key);
			}

		}
		//}
		if(kiesPIckedUp.isEmpty()){
			return new ItemStack(Items.stone_sword,1,0);
		}

		int var1 = rand.nextInt(kiesPIckedUp.size());
		UnsagaItems.Key randomKey = kiesPIckedUp.get(var1);
		Optional<Item> itemid = Optional.fromNullable(itemsMap.get(randomKey.get()));
		if(itemid.isPresent()){
			ItemStack is = new ItemStack(itemid.get(),1,0);
			UnsagaMaterial mate = materialManager.getMaterial(randomKey.getMaterial());
			if(mate==materialManager.failed){
				Unsaga.debug("picked failed tool");
				IUnsagaMaterialTool iu = (IUnsagaMaterialTool)is.getItem();
				return getFailedWeapon(rand, iu.getCategory());
			}
			HelperUnsagaItem.initWeapon(is, mate.getName(), mate.getWeight());
			return is;
		}
		return new ItemStack(Items.stone_sword,1,0);
	}
	

	public static class Key{
		

		protected final String key;
		
		public Key(String key){
			this.key = key;
		}
		
		public static Key makeKey(ToolCategory cate,UnsagaMaterial mate){
			return new Key(cate.toString()+"."+mate.getName());
		}
		

		public String get(){
			return this.key;
		}
		
		public String getSplitted(int num){
			String kies[] = this.key.split("\\.");
			return (num<kies.length) ? kies[num] : "";
		}
		
		public String getCategory(){
			return this.getSplitted(0);
		}
		
		public String getMaterial(){
			return this.getSplitted(1);
		}
	}

}
