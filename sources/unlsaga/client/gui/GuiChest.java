package com.hinasch.unlsaga.client.gui;

import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.inventory.container.ContainerChestUnsaga;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsaga.util.translation.Translation;

public class GuiChest extends GuiContainerBase{


	//protected ResourceLocation guiPanel = new ResourceLocation(Unsaga.DOMAIN+":textures/gui/box.png");

	//アクセ情報、宝箱情報同期させる必要性
	public static final int OPEN = 1;
	public static final int UNLOCK = 2;
	public static final int DEFUSE = 3;
	public static final int DIVINATION = 4;
	public static final int PENETRATION = 5;

	public TileEntityChestUnsagaNew theChest;
	public EntityPlayer openPlayer;

	protected ContainerChestUnsaga container;

	public GuiChest(TileEntityChestUnsagaNew chest,EntityPlayer ep) {
		super(new ContainerChestUnsaga(chest,ep));
		this.container = (ContainerChestUnsaga) this.inventorySlots;
		this.theChest = chest;
		this.openPlayer = ep;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		//
		//		
		//		// ボタンを追加
		//		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		this.addButton(OPEN, i + 32, j + 16 +(18*1), 30, 19 , Translation.localize("gui.chest.button.open"));
		this.addButton(UNLOCK, i + 32, j + 16 +(18*2), 31, 19 ,  Translation.localize("gui.chest.button.unlock"));
		this.addButton(DEFUSE, i + 32, j + 16 +(18*3), 30, 19 , Translation.localize("gui.chest.button.defuse"));
		this.addButton(DIVINATION, i + 32, j + 16 +(18*4), 30, 19 , Translation.localize("gui.chest.button.divination"));
		this.addButton(PENETRATION, i + 32, j + 16 +(18*5), 30, 19 , Translation.localize("gui.chest.button.penetration"));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		fontRendererObj.drawString(Translation.localize("word.chest")+" LV:"+this.theChest.getChestLevel(), 8, 6, 0x404040);
		
		//fontRenderer.drawString("Result:"+getSpellStr(), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	public String getGuiTextureName(){
		return Unsaga.DOMAIN+":textures/gui/box.png";
	}
	
	@Override
	public String getGuiName(){
		return "";
	}
}
