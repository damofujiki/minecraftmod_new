package com.hinasch.unlsaga.inventory.container;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.hinasch.lib.XYZPos;
import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.gui.GuiBartering;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.inventory.InventoryMerchantUnsaga;
import com.hinasch.unlsaga.inventory.slot.SlotMerchandise;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsaga.network.packet.PacketSyncGui;
import com.hinasch.unlsaga.villager.bartering.MerchandiseInfo;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerBartering extends ContainerBase{

	protected World worldobj;
	protected EntityPlayer theCustomer;
	protected IMerchant theMerchant;
	protected SlotMerchandise[] merchandiseSlot;
	

	protected int isPriceDown;
	protected int isPriceUp;

	protected InventoryMerchantUnsaga invMerchant;

	protected byte selected;

	public ContainerBartering(World world,EntityPlayer ep,IMerchant merchant){

		super(ep, new InventoryMerchantUnsaga(ep,merchant));
		this.worldobj = world;
		this.theCustomer = ep;
		this.theMerchant = merchant;
		this.theMerchant.setCustomer(theCustomer);
		this.invMerchant = (InventoryMerchantUnsaga) this.inv;
		this.merchandiseSlot = new SlotMerchandise[9];

		this.spreadSlotItems = false;

		this.isPriceDown = 0;
		this.isPriceUp = 0;
		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new SlotMerchandise(this.invMerchant, i+10, 8 + i * 18, 63-(18*3)));
		}

		for (int i = 0; i < this.invMerchant.getSizeBarteringInv(); ++i)
		{
			this.addSlotToContainer(new Slot(this.invMerchant, i, 8 + i * 18, 62));
		}
		this.addSlotToContainer(new Slot(this.invMerchant, this.invMerchant.RESULT, 8 + 8 * 18, 62));

		//this.addSlotToContainer(new Slot(this.invMerchant,30,152,42));
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.theCustomer == entityplayer;
	}

	public int getSellPrice(){

		return this.invMerchant.getCurrentPriceToSell();
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		this.invMerchant.closeInventory();
		this.theMerchant.setCustomer((EntityPlayer)null);
		if(!par1EntityPlayer.worldObj.isRemote){
			for(int i=0;i<this.invMerchant.getSizeBarteringInv();i++){
				ItemStack is = this.invMerchant.getStackInSlotOnClosing(i);
				if(is!=null){
					par1EntityPlayer.entityDropItem(is,0.1F);
				}
			}
			ItemStack is = this.invMerchant.getStackInSlotOnClosing(this.invMerchant.RESULT);
			if(is!=null){
				par1EntityPlayer.entityDropItem(is,0.1F);
			}
		}
	}


//	public void onButtonPushed(int buttonid) {
//		
//		PacketGuiButton pgo = new PacketGuiButton(PacketGuiButton.GUI_BARTERING,(byte)buttonid);
//		Unsaga.packetPipeline.sendToServer(pgo);
//
//	}
//
//	public static void writePacketData(ByteBuf dos, byte id) {
//		dos.writeByte((byte)id);
//
//	}
//
//	public void readPacketData(Object[] args) {
//
//			this.id = (Byte)args[0];
//			Unsaga.debug(this.id);
//	}

	public boolean canBuy(ItemStack is){
		if(!MerchandiseInfo.hasBuyPriceTag(is)){
			return false;
		}
		MerchandiseInfo info = new MerchandiseInfo(is);
		int priceBuy = info.getBuyPriceTagWithDiscount(this.isPriceDown,this.isPriceUp);
		int priceSell = this.getSellPrice();
		Unsaga.debug(priceSell,priceBuy,this.getClass().getName());
		if(priceSell>=priceBuy){
			return true;
		}
		return false;
	}
	
	@Override
	public void onPacketData() {

		this.selected = (byte)this.buttonID;
		Unsaga.debug(this.selected,this.getClass().getName());
//		if(this.invMerchant.getMerchandise(this.selected)!=null){
//
//			this.progressBuying(this.selected);
//		}
		PacketSyncGui psg = null;
		switch(this.selected){
		case GuiBartering.BUTTON_GET_SECRET:
			if(SkillPanels.getHighestLevelOfPanel(this.worldobj, this.ep, Unsaga.skillPanels.luckyFind)>=0){
				this.getAdditionalMerchandise();
			}

			break;
		case GuiBartering.BUTTON_UP_PRICE:
			this.isPriceUp = SkillPanels.getHighestLevelOfPanel(ep.worldObj,ep, Unsaga.skillPanels.gratuity) + 1;
			Unsaga.debug(this.isPriceUp,this.getClass());
			this.isPriceDown = 0;
			psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERINGGUI,this.isPriceDown,this.isPriceUp);
			Unsaga.packetPipeline.sendTo(psg, (EntityPlayerMP) this.ep);
			break;
		case GuiBartering.BUTTON_DOWN_PRICE:
			this.isPriceDown = SkillPanels.getHighestLevelOfPanel(ep.worldObj,ep, Unsaga.skillPanels.discount) + 1;
			Unsaga.debug(this.isPriceDown,this.getClass());
			this.isPriceUp = 0;
			psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERINGGUI,this.isPriceDown,this.isPriceUp);
			Unsaga.packetPipeline.sendTo(psg, (EntityPlayerMP) this.ep);
			break;
		}
		

	}
	
	public void getAdditionalMerchandise(){
		ExtendedMerchantData data = ExtendedMerchantData.getData((EntityVillager) this.theMerchant);
		if(this.invMerchant.getMerchandise(8)!=null || data.hasFoundSecret())return;
		Random rand = new Random();
		if(this.invMerchant.getHelper()!=null){
			int repu = ExtendedMerchantData.getData((EntityVillager) this.theMerchant).getDistributionLevel() + 5;
			ItemStack is = MerchandiseInfo.getRandomMerchandise(rand,repu);
			MerchandiseInfo.setBuyPriceTag(is,MerchandiseInfo.getPrice(is)*3);
			this.invMerchant.setMerchandise(8, is);
			data.setFoundSecret(true);
		}

	}
	public void progressBuying(int invNum){
		if(canBuy(this.invMerchant.getMerchandise(invNum))){
			ItemStack bought = this.invMerchant.getMerchandise(invNum).copy();
			MerchandiseInfo.removePriceTag(bought);
			//if(!this.theCustomer.worldObj.isRemote){
			this.invMerchant.setResult(bought);
			//this.theCustomer.entityDropItem(bought,0.1F);
			//}
			this.invMerchant.decrStackSize(invNum+10, this.invMerchant.getMerchandise(invNum).stackSize);
			if(!this.worldobj.isRemote){
				XYZPos xyz = XYZPos.entityPosToXYZ((Entity) this.theMerchant);
				//Village village = this.worldobj.villageCollectionObj.findNearestVillage(xyz.x, xyz.y, xyz.z, 32);

					int repu_up = +1;
					if(this.isPriceUp>0){
						repu_up = +2;
					}
					
					//village.setReputationForPlayer(this.theCustomer.getCommandSenderName(), repu_up);
				int repubase = ExtendedMerchantData.getData((EntityVillager) this.theMerchant).getDistributionLevel();
				ExtendedMerchantData.getData((EntityVillager) this.theMerchant).setDistributionLevel(repubase + repu_up);
				
			}
			Unsaga.debug(this.worldobj.getTotalWorldTime());
			this.cleanBarteringInv();
		}
	}
	
	public void cleanBarteringInv(){
		for(int i=0;i<this.invMerchant.getSizeBarteringInv();i++){
			if(this.invMerchant.getBartering(i)!=null){
				if(MerchandiseInfo.isPossibleToSell(this.invMerchant.getBartering(i))){
					this.invMerchant.setBartering(i, null);
				}
			}
		}
	}

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			Object... args) {
		// TODO 自動生成されたメソッド・スタブ
		return new PacketGuiButtonNew(guiID,buttonID,(byte)selected);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.guiNumber.BARTERING;
	}
	
	@Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
		if(par1>=36 && par1<=44){
			Unsaga.debug(par1,par2,par3,this.getClass().getName());
			if(this.getSlot(par1).getStack()!=null){
				if(par4EntityPlayer.worldObj.isRemote){
					Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
				}
				this.progressBuying(par1-36);
			}
			
		}

		return super.slotClick(par1, par2, par3, par4EntityPlayer);
		
    	
    }
}
