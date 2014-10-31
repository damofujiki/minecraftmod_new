package com.hinasch.unlsaga.event.extendeddata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import com.hinasch.lib.UtilNBT;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.panel.ItemSkillPanel;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.item.panel.SkillPanels.PanelData;

public class WorldSaveDataUnsaga extends WorldSavedData{

	public static final String KEY = Unsaga.MODID+".skillData";
	
	public Map<String,PanelList> panelDataPerUser;
	

	public Map<Integer,XYZPos> carrierMap;
	
	
	public WorldSaveDataUnsaga(String par1Str) {
		super(par1Str);
		this.panelDataPerUser = new HashMap();
		this.carrierMap = new HashMap();
	}

	public WorldSaveDataUnsaga() {
		super(KEY);
	}

	public XYZPos getCarrierAddress(int id){
		return this.carrierMap.get(id);
	}
	public CarrierData getRandomCarrier(Random rand,double dis,XYZPos pos,int current){
		Map<Integer,XYZPos> newlist = new HashMap();
		for(Integer id:this.carrierMap.keySet()){
			if(this.carrierMap.get(id).getDistanceTo(pos)>dis){
				Unsaga.debug(this.carrierMap.get(id).getDistanceTo(pos));
				if(id!=current){
					newlist.put(id, this.carrierMap.get(id));
				}

			}
		}
		Unsaga.debug(newlist);
		if(newlist.isEmpty()){
			return null;
		}
		int rnd = rand.nextInt(newlist.size());
		if(newlist.get(rnd)==null){
			return null;
		}
		return new CarrierData(rnd,newlist.get(rnd));
	}
	public void addCarrierData(int id,EntityVillager villager){
		if(this.carrierMap==null){
			this.carrierMap = new HashMap();
		}
	
		this.carrierMap.put(id, XYZPos.entityPosToXYZ(villager));
		Unsaga.debug("Add Carrier:",id,this.carrierMap.get(id),this.getClass().getName());
	}
	public int getNextCarrierID(){
		int index = 0;
		if(this.carrierMap==null ||this.carrierMap.isEmpty()){
			return 0;
		}
		for(int i=0;i<65535;i++){
			if(!this.carrierMap.containsKey(i)){
				return i;
			}
		}
		return 0;
	}
	@Override
	public void readFromNBT(NBTTagCompound var1) {

		Unsaga.debug("読み込まれます",this.getClass());
		NBTTagList tagUsers = UtilNBT.getTagList(var1, "PanelPerUser");
		for(int i=0; i< tagUsers.tagCount() ; i++){
			
			NBTTagCompound nbtPerUser = tagUsers.getCompoundTagAt(i);
			Unsaga.debug(nbtPerUser,this.getClass());

			NBTTagList tagPanels = UtilNBT.getTagList(nbtPerUser,"Panels");
			PanelList newPanels = new PanelList();
			newPanels.setItemStacks(UtilNBT.getItemStacksFromNBT(tagPanels, 7));
			Unsaga.debug(newPanels.panels,this.getClass());
			String username = nbtPerUser.getString("username"); 
			Unsaga.debug(username+"のデータをmapにput"+tagPanels,this.getClass());
			this.panelDataPerUser.put(username, newPanels);
			
		}
		
		NBTTagList tagCarriers = UtilNBT.getTagList(var1, "Carriers");
		if(this.carrierMap==null){
			this.carrierMap = new HashMap();
		}
		for(int i=0; i< tagCarriers.tagCount() ; i++){
			NBTTagCompound perCarrier = tagCarriers.getCompoundTagAt(i);
			int id = perCarrier.getInteger("id");
			XYZPos pos = XYZPos.strapOff(perCarrier.getString("pos"));
			Unsaga.debug("Carrier",id,pos.toString(),this.getClass().getName());
			if(pos!=null){
				this.carrierMap.put(id, pos);
			}

		}
		
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		Unsaga.debug("書き込まれます",this.getClass());
		
		if(this.panelDataPerUser!=null && !this.panelDataPerUser.isEmpty()){
			NBTTagList tagsPerUser = new NBTTagList();
			for(String username:this.panelDataPerUser.keySet()){
				Unsaga.debug(username+"のを書き込みます",this.getClass());
				PanelList panelListPerUser = this.panelDataPerUser.get(username);
				NBTTagList tagPanels = UtilNBT.getTagList();
				UtilNBT.writeItemStacksToNBTTag(tagPanels, panelListPerUser.panels);
				NBTTagCompound compoundOneUser = new NBTTagCompound();
				compoundOneUser.setString("username",username);
				compoundOneUser.setTag("Panels", tagPanels);

				tagsPerUser.appendTag(compoundOneUser);
			}

			Unsaga.debug(tagsPerUser);
			var1.setTag("PanelPerUser", tagsPerUser);
		}
		
		if(this.carrierMap!=null && !this.carrierMap.isEmpty()){
			NBTTagList tagsCarrier = new NBTTagList();
			for(Integer id:this.carrierMap.keySet()){
				NBTTagCompound compoundCarrier = new NBTTagCompound();
				compoundCarrier.setInteger("id", id);
				compoundCarrier.setString("pos", this.carrierMap.get(id).toString());
				tagsCarrier.appendTag(compoundCarrier);
				Unsaga.debug("Carrier-Save",id,this.carrierMap.get(id).toString(),this.getClass().getName());
			}
			
			var1.setTag("Carriers", tagsCarrier);
		}
	}

	public PanelList getPanelList(String user){
		if(this.panelDataPerUser==null){
			this.panelDataPerUser = new HashMap();
		}
		String username = Unsaga.debug ? "debug"  : user;
		if(!this.panelDataPerUser.isEmpty() && this.panelDataPerUser.containsKey(username)){
			return this.panelDataPerUser.get(username);
		}
		return null;
	}
	
	//debug
	public void clearData(){
		this.panelDataPerUser = new HashMap();
	}
	public void setPanels(String user,ItemStack[] panels){
		String username = Unsaga.debug ? "debug"  : user;
		this.panelDataPerUser.put(username, new PanelList(panels));
	}
	
	public static int getHighestLevelOfPanel(World world,EntityPlayer ep,PanelData panel){
		PanelList list = getPanels(world,ep);
		int level = -1;
		if(list!=null){
			level = list.getHighest(panel);
		}

		return level;
		
	}
	public static WorldSaveDataUnsaga getData(World world){
		WorldSaveDataUnsaga data = (WorldSaveDataUnsaga)world.loadItemData(WorldSaveDataUnsaga.class, WorldSaveDataUnsaga.KEY);
		if(data ==null){
				data = new WorldSaveDataUnsaga();
				world.setItemData(WorldSaveDataUnsaga.KEY, data);
		}
		
		return data;
	}
	
	public static PanelList getPanels(World world,EntityPlayer ep){
		WorldSaveDataUnsaga data = getData(world);
		PanelList panelsEP = data.getPanelList(ep.getCommandSenderName());
		return panelsEP;
	}
	public static void clearData(World world){
		WorldSaveDataUnsaga data = getData(world);
		if(data !=null){
			data.clearData();
		}
		data.markDirty();
		
	}
	public static class PanelList{
		
		public ItemStack[] panels;
		
		public PanelList(){
			this.panels = new ItemStack[7];
		}
		
		public PanelList(ItemStack[] iss){
			this.panels = new ItemStack[7];
			for(int i=0;i<iss.length;i++){
				this.panels[i] = iss[i] != null ? iss[i].copy() : null;
			}
		}

		public void setItemStacks(ItemStack[] iss){
			for(int i=0;i<iss.length;i++){
				this.panels[i] = iss[i] != null ? iss[i].copy() : null;
			}
		}
		
		public void debug(){
			for(ItemStack is:panels){
				Unsaga.debug(is,this.getClass());
			}
		}
		

		public int getHighest(PanelData panel){
			int ret = -1;
			for(ItemStack is:this.panels){
				if(is!=null && is.getItem() instanceof ItemSkillPanel){
					PanelData paneldata = Unsaga.skillPanels.getData(is.getItemDamage());
					int level = ItemSkillPanel.getLevel(is);
					PanelLevelPair pair = new PanelLevelPair(paneldata,level);
					if(pair.panel.id==panel.id){
						if(ret<pair.level){
							ret = pair.level;
						}
					}
				}


			}
			return ret;
		}
		public List<PanelLevelPair> getPanelLevelPairList(){
			List<PanelLevelPair> list = new ArrayList();
			for(ItemStack is:panels){
				if(is!=null && is.getItem() instanceof ItemSkillPanel){
					PanelData panel = Unsaga.skillPanels.getData(is.getItemDamage());
					int level = ItemSkillPanel.getLevel(is);
					Unsaga.debug(panel,level);
					list.add(new PanelLevelPair(panel,level));
					
				}
				
			}
			
			return list;
		}
	}
	
	public static class PanelLevelPair{
		public final int level;
		public final SkillPanels.PanelData panel;
		
		public PanelLevelPair(SkillPanels.PanelData panel,int level){
			this.level = level;
			this.panel = panel;
		}
		
		public String toString(){
			return "Level:"+this.level+":"+this.panel.toString();
		}
	}
	
	public static class CarrierData {
		
		public final int id;
		public final XYZPos pos;
		public CarrierData(int id,XYZPos pos){
			this.id = id;
			this.pos = pos;
		}
	}
}
