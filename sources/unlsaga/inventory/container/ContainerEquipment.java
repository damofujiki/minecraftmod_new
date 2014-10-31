package com.hinasch.unlsaga.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import com.hinasch.lib.XYZPos;
import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.client.gui.GuiEquipment;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.inventory.InventoryEquipment;
import com.hinasch.unlsaga.inventory.slot.SlotAccessory;
import com.hinasch.unlsaga.inventory.slot.SlotTablet;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerEquipment extends ContainerBase{

	protected EntityPlayer player;
	protected InventoryEquipment invEquipment;

	public static final String KEY = "unsaga.equipment";
	
	public ContainerEquipment(InventoryPlayer par1InventoryPlayer, EntityPlayer ep)
	{
		super(ep,null);
		this.player = ep;
		this.invEquipment = new InventoryEquipment(ep);
		this.inv = this.invEquipment;
		ep.addStat(Unsaga.achievements.openInv, 1);

		this.loadFromData(ExtendedPlayerData.getData(ep));
		
		this.addSlotToContainer(new SlotAccessory(this.invEquipment, 0, 28, 53-(18*2))); 
		this.addSlotToContainer(new SlotAccessory(this.invEquipment, 1, 28+(18*2)-8, 53-(18*2))); 
		this.addSlotToContainer(new SlotTablet(this.invEquipment,2, 28, 53)); 
		//this.addSlotToContainer(new SlotMerchantResult(par1InventoryPlayer.player, par2IMerchant, this.merchantInventory, 2, 120, 53));
		//int i;

		
	}
	
	protected void loadFromData(ExtendedPlayerData data){
		this.invEquipment.setInventorySlotContents(0, data.getAccessory(0));
		this.invEquipment.setInventorySlotContents(1, data.getAccessory(1));
		this.invEquipment.setInventorySlotContents(2, data.getAccessory(2));
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.player.openContainer != player.inventoryContainer;
	}
	
	@Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
		this.invEquipment.closeInventory();
		return;
    }

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			Object... args) {
		// TODO 自動生成されたメソッド・スタブ
		return new PacketGuiButtonNew(guiID,buttonID);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.guiNumber.EQUIP;
	}

	@Override
	public void onPacketData() {

		XYZPos p = null;
		switch(this.buttonID){
		case GuiEquipment.BUTTON_OPEN_SKILLS:
			//Unsaga.debug("kiteru",this.getClass());
			p = XYZPos.entityPosToXYZ(player);
			this.player.closeScreen();
			this.player.openGui(Unsaga.instance, guiNumber.SKILLPANEL, this.player.worldObj, p.x, p.y, p.z);

			break;
		case GuiEquipment.BUTTON_OPEN_BLEND:
			if(SkillPanels.hasPanel(this.ep.worldObj, ep, Unsaga.skillPanels.spellBlend)){
				p = XYZPos.entityPosToXYZ(player);
				this.player.closeScreen();
				this.player.openGui(Unsaga.instance, guiNumber.BLENDER, this.player.worldObj, p.x, p.y, p.z);
			}
			//Unsaga.debug("kiteru",this.getClass());
			break;
		}

	}
	

	
	

}
