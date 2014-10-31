package com.hinasch.unlsaga.inventory.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.gui.GuiSkillPanel;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.inventory.InventorySkillPanel;
import com.hinasch.unlsaga.inventory.slot.SlotSkillPanel;
import com.hinasch.unlsaga.item.panel.ItemSkillPanel;
import com.hinasch.unlsaga.item.panel.SkillPanels.PanelData;
import com.hinasch.unlsaga.item.panel.SkillPanels.WeightedRandomPanel;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerSkillPanel extends ContainerBase{

	protected InventorySkillPanel invSkillPanel;
	protected World world;
	protected ItemStack undoPanel;
	protected int slotUndo;
	protected ItemStack undoPanelPlayer;
	protected boolean drawed;
	protected boolean hasSet;
	protected int expToConsume;
	
	protected Map<Integer,Integer[]> modifier_level;
	
	public ContainerSkillPanel(EntityPlayer ep) {
		super(ep, new InventorySkillPanel());
		this.world = ep.worldObj;
		this.invSkillPanel = (InventorySkillPanel) this.inv;
		this.spreadSlotItems = false;
		this.expToConsume = 5;
		this.hasSet = false;
		this.drawed = false;

		this.modifier_level = new HashMap();
		this.modifier_level.put(0, new Integer[]{40,15,1,0,0});
		this.modifier_level.put(1, new Integer[]{10,40,10,0,0});
		this.modifier_level.put(2, new Integer[]{5,10,40,10,0});
		this.modifier_level.put(3, new Integer[]{2,5,40,30,1});
		this.modifier_level.put(4, new Integer[]{0,0,8,40,5});
		this.modifier_level.put(5, new Integer[]{0,0,2,10,30});

		

		WorldSaveDataUnsaga data = WorldSaveDataUnsaga.getData(world);

		WorldSaveDataUnsaga.PanelList panelList = data.getPanelList(ep.getCommandSenderName());
		if(panelList!=null){
			for(int i=0;i<7;i++){
				this.inv.setInventorySlotContents(i, panelList.panels[i]);
			}
		}
		int index = 0;
		for(int j=0;j<2 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j , 62 + 9 + j * 18, 17 + index * 18).setPlayerPanel(true));
		}
		index +=1;
		for(int j=0;j<3 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j + 2, 62 + j * 18, 17 + index * 18).setPlayerPanel(true));
		}
		index +=1;
		for(int j=0;j<2 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j + 5, 62 + 9 +  j * 18, 17 + index * 18).setPlayerPanel(true));
		}

		for(int j=0;j<2 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,7+ j, 53 + 18*4 +  j * 18, 17 + index * 18));
		}
		

	}

	@Override
	public boolean isShowPlayerInv(){
		return this.ep.capabilities.isCreativeMode;
	}
	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			Object... args) {
		// TODO 自動生成されたメソッド・スタブ
		return new PacketGuiButtonNew(guiID,buttonID,(Byte)args[0]);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.guiNumber.SKILLPANEL;
	}

	@Override
	public void onPacketData() {
		Unsaga.debug("kitemasu",this.getClass());
		switch(this.buttonID){
		case GuiSkillPanel.BUTTON_CHANGE_EXP:
			this.expToConsume = (Byte)this.argsSent[0];
			break;
		case GuiSkillPanel.BUTTON_DRAW_PANELS:
			if(!this.drawed){
				if(this.ep.experienceLevel>=this.expToConsume || this.ep.capabilities.isCreativeMode){
					this.drawSkills();
					this.drawed = true;
				}
			}

			break;
		case GuiSkillPanel.BUTTON_UNDO:
			if(this.hasSet){
				this.undo();
				this.hasSet = false;
			}
			break;
		}

	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		for(int i=0;i<7;i++){
			ItemStack panel = this.invSkillPanel.getStackInSlot(i);
			if(panel!=null){
				ItemSkillPanel.setLock(panel, true);
			}

		}
		par1EntityPlayer.inventory.setItemStack(null);
		this.savePanelData(par1EntityPlayer);
	}

	public void savePanelData(EntityPlayer ep){
		WorldSaveDataUnsaga data = WorldSaveDataUnsaga.getData(world);
		data.setPanels(ep.getCommandSenderName(), this.invSkillPanel.getPlayerPanels());
		this.world.setItemData(WorldSaveDataUnsaga.KEY, data);
		data.markDirty();
	}


	public void setUndoInfo(int undoSlot,ItemStack undoPlayer,ItemStack undoNew){
		this.slotUndo = undoSlot;
		this.undoPanelPlayer = undoPlayer!=null ?undoPlayer.copy() : null;
		this.undoPanel = undoNew!=null ?undoNew.copy() : null;
		this.hasSet = true;
	}
	public void undo(){



		this.getSlot(slotUndo).putStack(undoPanelPlayer!=null? undoPanelPlayer.copy() : null);
		this.inv.setInventorySlotContents(this.getEmptySlot(), undoPanel!=null? undoPanel.copy() : null);
	}

	public void drawSkills(){
		//Collection<SkillPanels.WeightedRandomPanel> weightedPanels = Unsaga.skillPanels.getWeightedRandomPanels();
		List<WeightedRandomPanel> weightedAllPanels = Unsaga.skillPanels.getWeightedRandomPanels();
		int num = this.expToConsume/5;
		Integer[] modifier = this.modifier_level.get(num-1);
		List<WeightedRandomPanel> availablePanels = new ArrayList();
		for(WeightedRandomPanel wp:weightedAllPanels){
			Unsaga.debug(wp.level);
			if(!this.hasPanel(wp.panel, wp.level)){
				if(modifier[wp.level]>0){
					int modifieredWeight = wp.itemWeight + modifier[wp.level];
					availablePanels.add(new WeightedRandomPanel(modifieredWeight,wp.panel,wp.level));
				}
			}
		}
		Unsaga.debug(availablePanels,this.getClass());
		WeightedRandomPanel drawedPanel = (WeightedRandomPanel) WeightedRandom.getRandomItem(ep.getRNG(), availablePanels);
		WeightedRandomPanel drawedPanel2 = (WeightedRandomPanel) WeightedRandom.getRandomItem(ep.getRNG(), availablePanels);

		
		
		
//		List<WeightedRandomLevel> weightedLevels = new ArrayList();
//		for(int i=0;i<5;i++){
//			if(!this.hasPanel(drawedPanel.panel, i)){
//				int num = this.expToConsume/5;
//				Integer[] modifier = this.modifier_level.get(num-1);
//				if(modifier[i]>0){
//					weightedLevels.add(new WeightedRandomLevel(modifier[i],i));
//				}
//				
//			}
//		}
//		WeightedRandomLevel drawedLevel = null;
//		if(!weightedLevels.isEmpty()){
//			drawedLevel =  (WeightedRandomLevel) WeightedRandom.getRandomItem(ep.getRNG(), weightedLevels);
//
//		}
				
		for(int i=0;i<2;i++){
			int drawedLevel = drawedPanel.level;
			ItemStack isPanel = i==0? drawedPanel.panel.getItemStack() : drawedPanel2.panel.getItemStack();
			if(isPanel==null)Unsaga.skillPanels.getData(0).getItemStack();
			ItemSkillPanel.setLevel(isPanel, drawedLevel);
			this.inv.setInventorySlotContents(7+i, isPanel.copy());
		}

		if(!this.ep.capabilities.isCreativeMode){
			if(this.ep.experienceLevel>=this.expToConsume){
				this.ep.addExperienceLevel(-this.expToConsume);
			}

		}
	}
	
	public boolean hasPanel(PanelData data,int level){
		for(int i=0;i<7;i++){
			if(this.inv.getStackInSlot(i)!=null){
				int damage = this.inv.getStackInSlot(i).getItemDamage();
				int lv = ItemSkillPanel.getLevel(this.inv.getStackInSlot(i));
				if(data.id==damage && lv==level){
					return true;
				}
			}
			
			
		}
		return false;
	}
	
	public int getEmptySlot(){
		for(int i=0;i<2;i++){
			if(this.inv.getStackInSlot(7+i)==null){
				return 7+i;
			}
		}
		return -1;
	}
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if(par1>0){
			Slot slot = this.getSlot(par1);

			if(slot instanceof SlotSkillPanel){
				if(!((SlotSkillPanel) slot).isPlayerPanel() && this.getEmptySlot()>0){
					return null;
				}

			}
			ItemStack is = slot.getStack();
			ItemStack catching = par4EntityPlayer.inventory.getItemStack();
			if(catching!=null){
				this.setUndoInfo(par1, is, catching);
				slot.putStack(catching);
				par4EntityPlayer.inventory.setItemStack(null);
				return null;
			}
			if(slot instanceof SlotSkillPanel){
				if(((SlotSkillPanel) slot).isPlayerPanel()){
					return null;
				}

			}



		}

		return super.slotClick(par1, par2, par3, par4EntityPlayer);

	}




}
