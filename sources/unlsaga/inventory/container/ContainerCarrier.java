package com.hinasch.unlsaga.inventory.container;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hinasch.lib.ChatHandler;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.client.gui.GuiCarrier;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga.CarrierData;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsaga.network.packet.PacketSyncGui;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerCarrier extends ContainerBase{

	protected World world;
	protected EntityVillager villager;
	protected int carrierID;
	protected boolean isRequested;
	protected WorldSaveDataUnsaga worldData;
	protected CarrierData request;
	
	public ContainerCarrier(EntityPlayer ep, IInventory inv) {
		super(ep, inv);
		this.spreadSlotItems = false;
		this.world = ep.worldObj;
		ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
		if(!world.isRemote){
			this.villager = pdata.getMerchant().get();
			this.worldData = WorldSaveDataUnsaga.getData(this.world);
			this.carrierID = ExtendedMerchantData.getData(villager).getCarrierID();
		}

		this.isRequested = false;
	}

	public void syncCarrierID(){
		ExtendedMerchantData data = ExtendedMerchantData.getData(villager);
		PacketSyncGui psg = new PacketSyncGui(PacketSyncGui.SYNC_CARRIER,data.getCarrierID());
		Unsaga.packetPipeline.sendTo(psg, (EntityPlayerMP) ep);
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
		return guiNumber.CARRIER;
	}

	@Override
	public void onPacketData() {
		Unsaga.debug("きてます");
		//CarrierData requestCarrier = this.worldData.getRandomCarrier(this.villager.getRNG(), 1.0D, XYZPos.entityPosToXYZ(villager));

		ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
		if(world.isRemote)return;
		switch(this.buttonID){
		case GuiCarrier.BUTTON_REQUEST:
			if(this.isRequested)return;
			this.request = this.worldData.getRandomCarrier(this.villager.getRNG(), 100.0D, XYZPos.entityPosToXYZ(villager),this.carrierID);

			if(pdata.getContractedCarryingID()>=0){
				XYZPos pos = this.worldData.getCarrierAddress(pdata.getContractedCarryingID());
				ChatHandler.sendChatToPlayer(ep, "You're task:carry to Carrier ID "+pdata.getContractedCarryingID());
			}
			if(request!=null){
				ChatHandler.sendChatToPlayer(ep, "Carrier ID "+this.carrierID+"'s Request:carry to Carrier ID "+request.id+".address is ["+request.pos.toString()+"].");
				this.isRequested = true;
			}else{
				ChatHandler.sendChatToPlayer(ep, "No Request.");
			}
			break;
		case GuiCarrier.BUTTON_CONTRACT:
			if(this.isRequested){
				pdata.setContractedCarrying(this.request.id);
				ChatHandler.sendChatToPlayer(ep, "You Contracted.");
			}
			break;
		case GuiCarrier.BUTTON_DELIVER:
			if(pdata.getContractedCarryingID()>=0){
				if(pdata.getContractedCarryingID()==this.carrierID){
					ChatHandler.sendChatToPlayer(ep, "You Finished Carrying.");
					this.ep.closeScreen();
					ItemUtil.dropItem(ep.worldObj, new ItemStack(Items.gold_nugget,1), XYZPos.entityPosToXYZ(villager));
					pdata.setContractedCarrying(-1);
				}else{
					ChatHandler.sendChatToPlayer(ep, "Wrong Carrier.You have to another Carrier.");
				}
			}
			break;
		}
	}

	public boolean isShowPlayerInv(){
		return false;
	}
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO 自動生成されたメソッド・スタブ
		return this.villager.getCustomer()==var1;
	}
	
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
    	super.onContainerClosed(par1EntityPlayer);
    	if(!this.world.isRemote){
        	this.villager.setCustomer(null);
        	ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
        	pdata.setMerchant(null);
    	}


    }
}
