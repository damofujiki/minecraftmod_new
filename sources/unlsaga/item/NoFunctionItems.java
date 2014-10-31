package com.hinasch.unlsaga.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.material.UnsagaMaterials;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NoFunctionItems {

	protected static Map<Integer,ItemData> itemMap;
	protected UnsagaMaterials materials = Unsaga.materialManager;
	
	public void load(){
		if(itemMap!=null)return;
		
		itemMap = new HashMap();
		addData(0,"steelIngot","鋼インゴット","ingotSteel","ingot",0xa3a3a2,materials.steel1,250);
		addData(1,"steelIngot*","鋼インゴット*","ingotSteel","ingot",0xa3a3a2,materials.steel2,250);
		addData(2,"damascusIngot","ダマスカス鋼インゴット","ingotDamascus","ingot",0x928178,materials.damascus,2000);
		addData(3,"copperIngot","銅インゴット","ingotCopper","ingot",0xbf794e,materials.copper,90);
		addData(4,"silverIngot","銀インゴット","ingotSilver","ingot",0,materials.silver,80);
		addData(5,"leadIngot","鉛インゴット","ingotLead","ingot",0x0f2350,materials.lead,100);
		addData(6,"corundum","鋼玉","gemRuby","ruby",0,materials.corundum1,1200);
		addData(7,"corundum*","鋼玉","gemSapphire","sapphire",0,materials.corundum2,1200);
		addData(8,"faerieSilver","精霊銀インゴット","ingotFaerieSilver","ingot",0xaaaaFF,materials.fairieSilver,1000);
		addData(9,"meteorite","隕石","stoneMeteorite","stone",0x2e2930,materials.meteorite,200);
		addData(10,"meteoricIron","隕鉄","ingotMeteoriticIron","ingot",0xafafb0,materials.meteoricIron,300);
		addData(11,"carnelian","朱雀石","gemCarnelian","carnelian",0,materials.carnelian,50);
		addData(12,"topaz","黄龍石","gemTopaz","topaz",0,materials.topaz,50);
		addData(13,"ravenite","玄武石","gemRavenite","ravenite",0,materials.ravenite,50);
		addData(14,"lapisLazuli","蒼龍石","gemLapis","lapis",0,materials.lazuli,50);
		addData(15,"opal","白虎石","gemOpal","opal",0,materials.opal,50);
		addData(16,"angelite","聖石","gemAngelite","angelite",0,materials.angelite,100);
		addData(17,"demonite","魔石","gemDemonite","demonite",0,materials.demonite,100);
		addData(18,"debris","廃石","stoneDebris","stone",0,materials.debris,2);
		addData(19,"debris*","廃石*","stoneDebris","stone",0,materials.debris2,2);
	}
	
	public void addData(int num,String name,String jp,String dict,String icon,int color,UnsagaMaterial material,int damage){
		//int num = itemMap.size();
		Unsaga.debug("put into itemMap:"+num+":"+name);
		if(itemMap.containsKey(num)){
			Unsaga.debug("key"+num+" is already registered.");
		}
		itemMap.put(num, new ItemData(num,name,dict,icon,color,material,num,damage));
	}
	
	public int length(){
		return itemMap.size();
	}
	
	public Map<Integer,ItemData> getList(){
		return itemMap;
	}
	
	public String getName(int par1){
		return getList().get(par1).name;
	}
	
	public ItemData getDataFromMeta(int meta){
		return itemMap.get(meta);
	}
	
	public ItemStack getItemStack(int stack,int meta){
		return new ItemStack(Unsaga.items.materials,stack,meta);
	}
	
	public void registerOreDict(){
		Unsaga.debug(itemMap);
		
		for(Iterator<Integer> ite=itemMap.keySet().iterator();ite.hasNext();){
			int key = ite.next();
			ItemData var1 = itemMap.get(key);
			//HSLibs.langSet(var1.name, var1.nameJP, getItemStack(1,key));
			if(!var1.dictID.equals("")){
				OreDictionary.registerOre(var1.dictID, getItemStack(1,key));
			}
			
			Unsaga.debug("put localize:"+key+"/"+var1.name+"+"+":"+var1.rendercolor);
		}
	}
	
	public class ItemData {
		
		public final String name;
		//public final String nameJP;
		public final String dictID;
		public final String iconname;
		public final int rendercolor;
		public final UnsagaMaterial associated;
		public final int number;
		public int repair;
		
		
		public ItemData(int number,String par1,String par3,String par4,int par5,UnsagaMaterial material){
			this.number = number;
			name = par1;
			//nameJP = par2;
			dictID = par3;
			iconname = par4;
			rendercolor = par5;
			this.associated = material;
		}
		
		public ItemData(int number,String par1,String par2,String par4,int par5,UnsagaMaterial material,int meta,int damage){
			this(number,par1,par2,par4,par5,material);
			this.repair = damage;
			material.associate(new ItemStack(Unsaga.items.materials,1,meta));
		}
		
		public UnsagaMaterial getAssociatedMaterial(){
			return this.associated;
		}

	}
}
