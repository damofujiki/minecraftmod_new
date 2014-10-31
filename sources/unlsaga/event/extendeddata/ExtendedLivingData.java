package com.hinasch.unlsaga.event.extendeddata;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.common.base.Optional;
import com.hinasch.lib.CSVText;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.Debuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtendedLivingData implements IExtendedEntityProperties{

	protected Set<LivingDebuff> debuffs;

	public static String KEY = "Unsaga.Debuffs";
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		if(!debuffs.isEmpty()){
			String str = CSVText.exchangeCollectionToCSV(debuffs);
			compound.setString(KEY, str);
			return;
		}
		compound.setString(KEY, "");
		
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		String str = compound.getString(KEY);
		if(str.equals("")){
			this.debuffs = new HashSet();
			return;
		}
		List<String> strList = CSVText.csvToStrList(str);
		this.debuffs = strListToDebuffSet(strList); 
		
	}
	

	@Override
	public void init(Entity entity, World world) {
		// TODO 自動生成されたメソッド・スタブ
		this.debuffs = new HashSet();
	
	}

	@SubscribeEvent
	public void attachDataEvent(EntityConstructing e){
		
		if(e.entity instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase)e.entity;
			living.registerExtendedProperties(KEY, new ExtendedLivingData());
		}
	}
	
	@SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent e)
    {
		EntityLivingBase living =(EntityLivingBase)e.entityLiving;
        if(living.getExtendedProperties(KEY)!=null){
        	
        	ExtendedLivingData ldata = (ExtendedLivingData) living.getExtendedProperties(KEY);
        	
        	if(living.ticksExisted % 20 * 12 == 0){
        		ldata.updateAllRemaining(living);
        	}
        	ldata.updateTickAllRemaining(living);
        	
        }
    }

	//デバフの効果を実行（毎ティック）
	private void updateTickAllRemaining(EntityLivingBase living) {
		Set<LivingDebuff> removes = new HashSet();
		if(this.debuffs.isEmpty() || this.debuffs==null)return;
		for(LivingDebuff debuff:this.debuffs){
			debuff.updateTick(living);
			if(debuff.isExpired()){
				debuff.onExpiredEvent(living);
				removes.add(debuff);
			}
		}
		if(!removes.isEmpty()){
			for(LivingDebuff debuff:removes){
				this.debuffs.remove(debuff);
			}
		}
		
	}

	public Set<LivingDebuff> getProgressDebuffs(){
		return this.debuffs;
	}
	public Set<LivingDebuff> strListToDebuffSet(List<String> list){
		Unsaga.debug("復元中");
		Set<LivingDebuff> output = new HashSet();
		for(String str:list){
			output.add(LivingDebuff.buildFromString(str));
		}
		return output;
	}
	
	//デバフの効果を実行（毎フレーム）
	public void updateAllRemaining(EntityLivingBase living){
		if(this.debuffs.isEmpty() || this.debuffs==null)return;
		for(LivingDebuff debuff:this.debuffs){
			debuff.updateRemain(living);
		}
	}
	
	public boolean hasDebuff(Debuff debuff){
		if(this.debuffs.isEmpty())return false;
		for(LivingDebuff living:this.debuffs){
			if(living.getDebuff().number==debuff.number)return true;
		}
		return false;
	}
		
	public static ExtendedLivingData getData(EntityLivingBase living){
		if(living.getExtendedProperties(KEY)!=null){
			return (ExtendedLivingData) living.getExtendedProperties(KEY);
		}
		return new ExtendedLivingData();
	}
	public static void addDebuff(EntityLivingBase living,Debuff debuff,int remain){
		if(living.getExtendedProperties(ExtendedLivingData.KEY)!=null){
			ExtendedLivingData dataLiving = (ExtendedLivingData) living.getExtendedProperties(ExtendedLivingData.KEY);
			if(!dataLiving.hasDebuff(debuff)){
				LivingDebuff livdebuff = new LivingDebuff(debuff,remain);
				dataLiving.debuffs.add(livdebuff);
				livdebuff.onInitEvent(living);
			}else{
				removeDebuff(living,debuff);
				LivingDebuff livdebuff = new LivingDebuff(debuff,remain);
				dataLiving.debuffs.add(livdebuff);
				livdebuff.onInitEvent(living);
			}
			
		}
	}
	
	public static void addLivingDebuff(EntityLivingBase living,LivingDebuff livdebuff){
		if(living.getExtendedProperties(ExtendedLivingData.KEY)!=null){
			ExtendedLivingData dataLiving = (ExtendedLivingData) living.getExtendedProperties(ExtendedLivingData.KEY);
			if(!dataLiving.hasDebuff(livdebuff.getDebuff())){
				dataLiving.debuffs.add(livdebuff);
				livdebuff.onInitEvent(living);
			}else{
				removeDebuff(living,livdebuff.getDebuff());
				dataLiving.debuffs.add(livdebuff);
				livdebuff.onInitEvent(living);
			}
			
		}
	}

	public static void removeDebuff(EntityLivingBase living, Debuff debuff) {
		if(living.getExtendedProperties(ExtendedLivingData.KEY)!=null){
			ExtendedLivingData ldata = (ExtendedLivingData) living.getExtendedProperties(ExtendedLivingData.KEY);

			if(ldata.debuffs.isEmpty())return;
			Set<LivingDebuff> removes = new HashSet();
			for(LivingDebuff ldebuff:ldata.debuffs){
				if(ldebuff.getDebuff().number==debuff.number){

					removes.add(ldebuff);
				}
			}
			
			//同期エラーを防ぐ
			if(!removes.isEmpty()){
				for(LivingDebuff remov:removes){
					
					ldata.debuffs.remove(remov);
				}
			}

			
		}
		
	}
	
	public static boolean hasDebuff(EntityLivingBase living,Debuff debuff){
		if(living.getExtendedProperties(ExtendedLivingData.KEY)!=null){
			ExtendedLivingData ldata = (ExtendedLivingData) living.getExtendedProperties(ExtendedLivingData.KEY);
			if(ldata.debuffs.isEmpty())return false;
			for(LivingDebuff ldebuff:ldata.debuffs){
				if(ldebuff.getDebuff().number==debuff.number){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Optional<LivingDebuff> getDebuff(EntityLivingBase living,Debuff debuff){
		if(living.getExtendedProperties(ExtendedLivingData.KEY)!=null){
			ExtendedLivingData ldata = (ExtendedLivingData) living.getExtendedProperties(ExtendedLivingData.KEY);
			if(ldata.debuffs.isEmpty())return Optional.absent();
			for(LivingDebuff ldebuff:ldata.debuffs){
				if(ldebuff.getDebuff().number==debuff.number){
					return Optional.of(ldebuff);
				}
			}
		}
		return Optional.absent();
	}
}
