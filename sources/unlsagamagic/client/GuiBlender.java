package com.hinasch.unlsagamagic.client;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsagamagic.inventory.container.ContainerBlender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBlender extends GuiContainerBase
{


	public static final int BUTTON_UNDO = -3;
	public static final int BUTTON_BLEND = -2;
	
	private EntityPlayer ep;
	private ResourceLocation resourceGui = new ResourceLocation(Unsaga.DOMAIN+":textures/gui/blender.png");
	private ContainerBlender containerblender;


	public GuiBlender(EntityPlayer player,World world)	
	{
		super(new ContainerBlender(player, world));


		//this.blenderInventory = te;

		this.ep = player;
		this.containerblender = (ContainerBlender)inventorySlots;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;


		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		this.addButton(BUTTON_BLEND, i + 2, j + 16, 48, 20 , Translation.localize("gui.blender.button.blend"));
		this.addButton(BUTTON_UNDO, i + 2, j + 38, 48, 20 , Translation.localize("gui.blender.button.undo"));

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		//fontRenderer.drawString("Magic Blender", 58, 6, 0x404040);
		fontRendererObj.drawString(this.containerblender.getCurrentElement().toString(), 8, 6, 0x404040);

		fontRendererObj.drawString("Result:"+getSpellStr(), 8, (ySize - 96) + 2, 0x404040);
	}

	protected String getSpellStr(){
		if(this.containerblender.getSpellTransformed().isPresent()){
			return this.containerblender.getSpellTransformed().get().getName(Translation.getLang());
		}
		return "none";

	}
//	@Override
//	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
//	{
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		mc.renderEngine.bindTexture(resourceGui);
//		int xStart = width - xSize >> 1;
//		int yStart = height - ySize >> 1;
//		drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
//	}


	@Override
	public void updateScreen()
	{
		super.updateScreen();

	}

	public String getGuiTextureName(){
		return Unsaga.DOMAIN+":textures/gui/blender.png";
	}
	public String getGuiName(){
		return "Blender";
	}

}