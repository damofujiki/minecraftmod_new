package com.hinasch.unlsaga.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.inventory.container.ContainerBartering;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.util.translation.Translation;

public class GuiBartering extends GuiContainerBase{

	//protected final ResourceLocation background = new ResourceLocation(Unsaga.DOMAIN + ":textures/gui/shop.png");
	protected ContainerBartering container;
	protected IMerchant merchant;
	protected EntityPlayer ep;
	public static final int BUTTON_GET_SECRET = 0;
	public static final int BUTTON_UP_PRICE = 1;
	public static final int BUTTON_DOWN_PRICE = 2;
	
	public int priceDown = 0;
	public int priceUp = 0;
	
	public GuiBartering(IMerchant merchant,World world,EntityPlayer ep) {
		super(new ContainerBartering(world,ep,merchant));
		// TODO 自動生成されたコンストラクター・スタブ
		this.merchant = merchant;
		this.container = (ContainerBartering)this.inventorySlots;
		this.ep = ep;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{


		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRendererObj.drawString(Translation.localize("gui.bartering.amount")+this.container.getSellPrice(),8,48,0x404040);
	}
	
	@Override
	public String getGuiTextureName(){
		return Unsaga.DOMAIN + ":textures/gui/shop.png";
	}
	
	@Override
	public String getGuiName(){
		return "bartering";
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		
		this.addButton(BUTTON_GET_SECRET, i+7+(0*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.secret"));
		this.addButton(BUTTON_UP_PRICE, i+7+(3*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.upprice"));
		this.addButton(BUTTON_DOWN_PRICE, i+7+(6*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.downprice"));
//
//		
//		// ボタンを追加
//		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
//		buttonList.add(new GuiButton(DOFORGE, i + (18*5)+2, j + 16 +(18*2), 30, 19 , "Forge!"));
//		buttonList.add(new GuiButton(CATEGORY, i + (18*3)+2, j + 16 +(18*2), 31, 19 , "Category"));
		//for (int k = 0; k < 9; ++k)
		//{
		//	this.addButton(k, i+7+(k*18), j+85-(18*3), 18, 18 , " ");
		//	//this.merchandiseSlot[i] = new SlotMerchandise(this.invMerchant, i+10, 8 + i * 18, 63-(18*3));
		//	//this.addSlotToContainer(new SlotMerchandise(this.invMerchant, i+10, 8 + i * 18, 63-(18*3)));
		//}

	}
	
	@Override
	public void prePacket(GuiButton par1GuiButton){
		switch(par1GuiButton.id){
		case BUTTON_UP_PRICE:
			this.priceUp = SkillPanels.getHighestLevelOfPanel(ep.worldObj,ep, Unsaga.skillPanels.gratuity) + 1;
			Unsaga.debug(this.priceUp,this.getClass());
			break;
		case BUTTON_DOWN_PRICE:
			this.priceDown = SkillPanels.getHighestLevelOfPanel(ep.worldObj,ep, Unsaga.skillPanels.discount) + 1;
			Unsaga.debug(this.priceDown,this.getClass());
			break;
		}
	}
	
	@Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();
    	this.merchant.setCustomer(null);
    }
}
