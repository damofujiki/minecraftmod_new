package com.hinasch.lib.base;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiContainerBase extends GuiContainer{


	protected Container container;
	protected final ResourceLocation background = new ResourceLocation(this.getGuiTextureName());
	
	public GuiContainerBase(Container par1Container) {
		super(par1Container);
		this.container = par1Container;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(background);
		int xStart = width - xSize >> 1;
			int yStart = height - ySize >> 1;
			drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		fontRendererObj.drawString(getGuiName(),8,6,0x404040);
		
	}
	
	//initGuiでやる
	public GuiButton addButton(int id,int startX,int startY,int width,int height,String buttonString){
		GuiButton guiButton = new GuiButton(id,startX,startY,width,height,buttonString);
		buttonList.add(guiButton);
		return guiButton;
	}

	public String getGuiTextureName(){
		return "Domain:textures/gui/";
	}
	public String getGuiName(){
		return "Unknown";
	}
	
	public Object[] getSendingArgs(){
		return null;
	}
	
	public void prePacket(GuiButton par1GuiButton){
		
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{	
		if(par1GuiButton!=null){

			if (!par1GuiButton.enabled)
			{
				return;
			}
			

			this.prePacket(par1GuiButton);

			if(container instanceof ContainerBase){
				((ContainerBase) container).onButtonPushed(par1GuiButton.id,this.getSendingArgs());
			}
			
			

			
		}
	}
}
