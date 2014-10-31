package com.hinasch.unlsaga.client.gui;

import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.inventory.container.ContainerEquipment;

public class GuiEquipment extends GuiContainerBase{

	protected EntityPlayer player;

	public static final int BUTTON_OPEN_SKILLS = 1;
	public static final int BUTTON_OPEN_BLEND = 2;
	public GuiEquipment(EntityPlayer player) {
		super(new ContainerEquipment(player.inventory,player));
		this.player = player;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;

		
		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		//buttonList.add(new GuiButton(-2, i + (18*4)+2, j + 16 +(18*2), 30, 20 , "Forging"));
		this.addButton(BUTTON_OPEN_BLEND,  i + (18*4)+2, j + 16 +(18*0), 30, 20 ,"Blend");
		this.addButton(BUTTON_OPEN_SKILLS,  i + (18*4)+2, j + 16 +(18*2), 30, 20 ,"Skills");
	}
	
	public String getGuiTextureName(){
		return Unsaga.DOMAIN+":textures/gui/equipment.png";
	}
	public String getGuiName(){
		return "Accessory";
	}
	

}
