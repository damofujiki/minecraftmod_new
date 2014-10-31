package com.hinasch.unlsaga.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.inventory.container.ContainerSkillPanel;

public class GuiSkillPanel extends GuiContainerBase{

	public static final int BUTTON_DRAW_PANELS = 1;
	public static final int BUTTON_CHANGE_EXP = 2;
	public static final int BUTTON_UNDO = 3;
	protected GuiButton changeButton;
	protected int expToConsume;
	public GuiSkillPanel(EntityPlayer ep) {
		super(new ContainerSkillPanel(ep));
		this.expToConsume = 5;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void initGui(){
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		changeButton = this.addButton(BUTTON_CHANGE_EXP,  i + (18*7)+2, j + 30 -(18*1), 30, 20 ,String.valueOf(getExpToConsume()));
		this.addButton(BUTTON_DRAW_PANELS,  i + (18*7)+2, j + 30 +(18*0), 30, 20 ,"draw");
		this.addButton(BUTTON_UNDO,  i + (18*1)+2, j + 30 +(18*0), 30, 20 ,"undo");
	}
	@Override
	public String getGuiTextureName(){
		return Unsaga.DOMAIN+":textures/gui/skillpanel.png";
	}
	
	public int getExpToConsume(){
		return this.expToConsume;
	}
	@Override
	public String getGuiName(){
		return "Skill Panel";
	}
	
	@Override
	public void prePacket(GuiButton par1GuiButton){
		if(par1GuiButton.id==BUTTON_CHANGE_EXP){
			this.expToConsume += 5;
			Unsaga.debug(this.expToConsume,this.getClass());

			
			if(this.expToConsume>30){
				this.expToConsume = 5;
			}
			this.changeButton.displayString = String.valueOf(getExpToConsume());
		}
	}
	@Override
	public Object[] getSendingArgs(){
		Byte[] args = new Byte[1];
		args[0] = (byte)this.getExpToConsume();
		return args;
	}
}
