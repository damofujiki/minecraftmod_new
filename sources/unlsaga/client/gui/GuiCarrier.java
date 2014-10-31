package com.hinasch.unlsaga.client.gui;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedMerchantData;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.inventory.container.ContainerCarrier;

public class GuiCarrier extends GuiContainerBase{

	protected World world;
	protected EntityVillager theCarrier;
	protected ExtendedMerchantData carrierData;
	protected ExtendedPlayerData playerData;
	public int carrierid;
	public static final int BUTTON_CONTRACT = 1;
	public static final int BUTTON_REQUEST = 2;
	public static final int BUTTON_DELIVER = 3;
	
	public GuiCarrier(EntityPlayer ep) {
		super(new ContainerCarrier(ep,null));
		this.world = ep.worldObj;
		ExtendedPlayerData pdata = ExtendedPlayerData.getData(ep);
		this.carrierid = -1;
	}

	public void syncCarrierID(int par1){
		this.carrierid = par1;
	}
	
	@Override
	public String getGuiTextureName(){
		return Unsaga.DOMAIN+":textures/gui/box.png";
	}
	
	@Override
	public String getGuiName(){
		return "Carrier";
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		

		
		
		//fontRendererObj.drawString("Carrier ID:"+this.carrierData.getCarrierID(),28,18,0x404040);

	}
	
	@Override
    public void initGui()
    {
    	super.initGui();
    	
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		
    	this.addButton(BUTTON_REQUEST, i+32, j+70, 30, 20, "Request");
    	this.addButton(BUTTON_CONTRACT, i+32, j+90, 30, 20, "Contract");
    	this.addButton(BUTTON_DELIVER, i+32, j+110, 30, 20, "Deliver");
    }
	
	@Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();

    }
}
