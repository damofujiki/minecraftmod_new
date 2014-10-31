package com.hinasch.unlsaga.event.extendeddata;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import com.hinasch.lib.CSVText;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;
import com.hinasch.unlsaga.network.packet.PacketSyncTag;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtendedEntityTag implements IExtendedEntityProperties{

	protected List<String> taglist;
	protected String KEY = "unsaga.extendedtag";
	public static String tagKEY = "unsaga.extendedEntityTag";
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		if(!this.taglist.isEmpty() && this.taglist!=null){
			String str = CSVText.exchangeCollectionToCSV(this.taglist);
			compound.setString(KEY, str);
		}

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		if(compound.hasKey(KEY)){
			String str = compound.getString(KEY);
			if(!str.equals("")){
				this.taglist = CSVText.csvToStrList(str);
			}
		}


	}

	@SubscribeEvent
	public void attachDataEvent(EntityConstructing e){
	
		//Unsaga.debug("called",this.getClass());
		if(e.entity instanceof EntityArrow){
			Entity ent = (Entity)e.entity;
			ent.registerExtendedProperties(tagKEY, new ExtendedEntityTag());
		}

	}
	
	public static void initVillager(EntityVillager villager){

			if(villager.getProfession()==3){
				int var1 = Unsaga.debug? 2 : 10;
				int sw = villager.getRNG().nextInt(var1);
				if(!hasTag(villager,"initialized")){
					Unsaga.debug("タグをつける",villager,"initvilalger");
					PacketSyncTag pa = null;
					switch(sw){
					case 0:
						addTagToEntity(villager, ContainerSmithUnsaga.SMITH_TYPE_REPAIR_PRO);
						pa = new PacketSyncTag(villager.getEntityId(),0);
						Unsaga.packetPipeline.sendToServer(pa);
						addTagToEntity(villager, "initialized");
						break;
					case 1:
						addTagToEntity(villager, ContainerSmithUnsaga.SMITH_TYPE_ADD_ABILTY);
						pa = new PacketSyncTag(villager.getEntityId(),1);
						Unsaga.packetPipeline.sendToServer(pa);
						addTagToEntity(villager, "initialized");
						break;					
						
					}
				}

			}
		
	}
	@Override
	public void init(Entity entity, World world) {
		this.taglist = new ArrayList();
		
	}
	
	public void addTag(String par1){
		if(this.taglist==null){
			this.taglist = new ArrayList();
		}
		this.taglist.add(par1);
	}
	
	public boolean hasTag(String par1){
		for(String str:taglist){
			if(str.equals(par1)){
				return true;
			}
		}
		return false;
	}

	public static boolean hasTag(Entity entity,String par1){
		if(entity.getExtendedProperties(tagKEY)!=null){
			ExtendedEntityTag tag = (ExtendedEntityTag)entity.getExtendedProperties(tagKEY);
			return tag.hasTag(par1);
		}
		return false;
	}
	
	public static void addTagToEntity(Entity entity,String tag){
		
		ExtendedEntityTag tags = (ExtendedEntityTag) entity.getExtendedProperties(tagKEY);
		if(tags!=null){
			Unsaga.debug("タグとれました");
			tags.addTag(tag);
		}else{
			Unsaga.debug("タグとれませんでした、タグつけてみます");
			entity.registerExtendedProperties(tagKEY, new ExtendedEntityTag());
			tags = (ExtendedEntityTag) entity.getExtendedProperties(tagKEY);
			tags.addTag(tag);
		}
	}
	
	public static boolean entityHasTag(Entity entity,String tag){
		ExtendedEntityTag tags = (ExtendedEntityTag) entity.getExtendedProperties(tagKEY);
		if(tags!=null){
			return tags.hasTag(tag);
		}
		return false;
	}
	

}
