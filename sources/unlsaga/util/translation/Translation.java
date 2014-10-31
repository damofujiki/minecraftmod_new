package com.hinasch.unlsaga.util.translation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Translation {

	protected static ResourceLocation langfileUS = new ResourceLocation(Unsaga.DOMAIN,"unsaga_en_US.lang");
	protected static ResourceLocation langfileJP = new ResourceLocation(Unsaga.DOMAIN,"unsaga_ja_JP.lang");
	protected static Map<String,String> langMap;
	protected static Translation instance;
	protected Map<String,String> mapJa;
	protected Map<String,String> mapEn;
//	public String strEN;
//	public String strJP;
//	
//	public Translation(String en,String jp){
//		this.strEN = en;
//		this.strJP = jp;
//	}
	public static Translation getInstance(){
		if(instance==null){
			instance = new Translation();
		}
		return instance;
	}
	
	protected Translation(){
		langMap = new HashMap();
		this.mapEn = new HashMap();
		this.mapJa = new HashMap();
//		add("msg.gained.ability","gained ability %s.", "%s のアビリティを解放した！");
//		add("msg.gained.skill","gained skill %s.", "%s を閃いた！");
//		add("element.fire","fire", "火");
//		add("element.water","water", "水");
//		add("element.wood","wood", "木");
//		add("element.metal","metal", "金");
//		add("element.earth","earth", "土");
//		add("element.forbidden","forbidden", "禁");
//		add("word.spell","spell", "術");
//		add("word.chest","Chest", "宝箱");
//		add("msg.has.noability","you don't have the ability.","アビリティを持ってない。");
//		add("msg.chest.needle","Cactus Damage!","ダメージ！");
//		add("msg.chest.poison","Poison Trap!","毒のトラップ！");
//		add("msg.chest.burst","Chest Exploded!","宝箱が爆発した！");
//		add("msg.failed","Failed.","失敗した。");
//		add("msg.finished.deciphred","finished deciphring the magic tablet.", "魔道板の解読が完了した。");
//		add("msg.write.spell","wrote spell to book.", "術を本に書き込んだ。");
//		add("msg.spell.succeeded","Invoking Spell Succeeded.", "詠唱成功");
//		add("msg.spell.failed","Invoking Spell Failed.", "詠唱失敗");
//		add("msg.heal","%s's life was healed %d.", "%sの体力が%d回復した。");
//		add("tablet.notdeciphred","not deciphred", "未解読");
//		add("tablet.deciphred","deciphred", "解読済み");
//		add("msg.touch.chest","try to open the chest.", "宝箱を開けようとした。");
//		add("msg.chest.locked","chest has locked.", "宝箱には鍵がかかっている。");
//		add("msg.chest.magiclocked","chest has locked with magical lock.", "宝箱には魔法鍵がかかっている。");
//		add("msg.chest.unlocked","unlock succeeded.", "鍵を開けた。");
//		add("msg.chest.magiclock.unlocked","unlock magiclock,","魔法鍵を解除した。");
//		add("msg.chest.defused","defuse succeeded.", "ワナを外した。");
//		add("msg.spell.repair","repaired the item %d", "アイテムを%d修理した。");
//		add("msg.spell.cant.repair","can't repair the item","アイテムを修理できません。");
//		add("gui.blender.elements","Fi:%d Me:%d Wo:%d Wa:%d Ea:%d Fo:%d","火:%d 金:%d 木:%d 水:%d 土:%d 禁:%d");
//		add("gui.blender.percentage","Fi:%d Me:%d Wo:%d Wa:%d Ea:%d Fo:%d","火:%d 金:%d 木:%d 水:%d 土:%d 禁:%d");
//		add("gui.blender.button.blend","Blend","合成");
//		add("gui.blender.button.undo","Undo","解除");
//		add("gui.chest.button.open","Open","開ける");
//		add("gui.chest.button.unlock","Unlock","鍵開け");
//		add("gui.chest.button.defuse","Defuse","罠外し");
//		add("gui.chest.button.divination","Divination","占い");
//		add("gui.bartering.amount","Amount:","合計:");
//		add("msg.chest.divination.succeeded","Divination Suceeded.","占い成功");
//		add("msg.chest.divination.failed","Divination Failed.","占い失敗");
//		add("msg.chest.divination.levelis","this chest is Level %?","LV %d の宝箱？");
//		add("msg.chest.divination.catastrophe","Divination Awfully Failed!","占い大失敗..");
//		add("msg.spell.touchgold.succeeded","changed %s to gold nugget.","%sを金塊に変えた。");
//		add("msg.spell.touchgold.failed","touch gold failed.","タッチゴールドに失敗した。");
//		add("msg.spell.enchant","%s was enchanted.","%sはエンチャントされた。");
//		add("msg.spell.chest.notfound","can't find chests near.","周辺にチェストは見当たらなかった。");
//		add("msg.spell.metal.notfound","can't find metals near.","周辺の金属反応を探知できなかった。");
//		add("tips.validmaterial","Valid Material:","素材使用可:");
//		add("tips.canuseforbase","Can Use For:","ベース使用可:");
//		add("word.sword","Sword","剣");
//		add("word.axe","Axe","斧");
//		add("word.spear","Spear","槍");
//		add("word.bow","Bow","弓");
//		add("word.staff","Staff","杖");
		this.loadExternalFile();
	}
	

	
	
	@SideOnly(Side.CLIENT)
	public static boolean isJapanese(){
		String language = Minecraft.getMinecraft().gameSettings.language;
		return language.equals("ja_JP") ? true : false;
	}
	
	public static String getLang(){
		return Minecraft.getMinecraft().gameSettings.language;
	}
	
	public boolean hasKey(String key,String lang){
		if(lang.equals("ja_JP")){
			return this.mapJa.containsKey(key);
		}else{
			return this.mapEn.containsKey(key);
		}
	}
	
	public void add(String key,String en,String ja){
		this.mapEn.put(key, en);
		this.mapJa.put(key, ja);
	}
	
	public String getLocalized(String key){
		if(Translation.isJapanese()){
			if(mapJa.containsKey(key)){
				return this.mapJa.get(key);
			}
		}
		if(mapEn.containsKey(mapEn)){
			return this.mapEn.get(key);
		}
		return "";
	}
	
	public String getLocalized(String key,String lang){
		if(lang.equals("ja_JP")){
			if(mapJa.containsKey(key)){
				return this.mapJa.get(key);
			}
		}
		if(mapEn.containsKey(key)){
			return this.mapEn.get(key);
		}
		return "";
	}

	public static String localize(String key){
		return Unsaga.translation.getLocalized(key).equals("") ? key : Unsaga.translation.getLocalized(key);
	}
	
	public void loadExternalFile(){

		try {
			Map jpmap = this.loadProperties(langfileJP.getResourcePath());
			this.merge(jpmap, "ja_JP");
			Map enmap = this.loadProperties(langfileUS.getResourcePath());
			this.merge(enmap, "en_US");
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	//from http://park1.wakwak.com/~ima/java_tips0008.html
    private Map loadProperties(String path) throws IOException {
        InputStream in = this.getClass().getResourceAsStream(path);
        if (in == null) {
            throw new IllegalArgumentException(path + " not found.");
        }
        Properties props = new Properties();
        props.load(new InputStreamReader(in,"UTF-8"));
        return new HashMap(props);
    }
	
    private static String locationToName(ResourceLocation par0ResourceLocation)
    {
        return String.format("%s/%s/%s", new Object[] {"assets", par0ResourceLocation.getResourceDomain(), par0ResourceLocation.getResourcePath()});
    }
    
    //マージする際、本体のローカライズに使えるものがあったら追加しておく
    private void merge(Map map,String str){
    		for(Object ke:map.keySet()){
    			String key = (String)ke;
    			String value = (String) map.get(key);
    			if(str.equals("ja_JP")){
    				if(key.contains("item")){//(key).substring(0, 3).equals("item")){
    					Unsaga.debug("add localize:"+key+">"+value);
    					//LanguageRegistry.instance().addStringLocalization(key, "ja_JP", value);
    				}
    				this.mapJa.put(key, value);
    			}
    			if(str.equals("en_US")){
    				if((key).substring(0, 3).equals("item")){
    					Unsaga.debug("add localize:"+key+">"+value);
    					//LanguageRegistry.instance().addStringLocalization(key, "en_US", value);
    				}
    				this.mapEn.put(key, value);
    			}
    		}
    	
    }
}
