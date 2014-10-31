package com.hinasch.unlsaga.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.hinasch.lib.base.GuiContainerBase;
import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedEntityTag;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.util.translation.Translation;

public class GuiSmithUnsaga extends GuiContainerBase{

	protected ContainerSmithUnsaga container;
	protected EntityVillager villager;
	protected FontRenderer font = Minecraft.getMinecraft().fontRenderer;
	protected DebugUnsaga debug;
	protected byte currentCategory;

	public final static int CATEGORY = -2;
	public final static int DOFORGE = -1;

	public GuiSmithUnsaga(IMerchant merchant,World world,EntityPlayer ep) {
		super(new ContainerSmithUnsaga(merchant,world,ep));

		this.currentCategory = 0;

		this.container = (ContainerSmithUnsaga) this.inventorySlots;
		// TODO 自動生成されたコンストラクター・スタブ
		ExtendedPlayerData data = ExtendedPlayerData.getData(ep);
		if(data.getMerchant().isPresent()){
			
			this.villager = data.getMerchant().get();
			Unsaga.debug(this.villager,this.getClass());
		}
	}

	public int getCurrentCategory(){
		return (int)this.currentCategory;
	}

	@Override
	public Object[] getSendingArgs(){
		Byte[] args = new Byte[1];
		args[0] =(byte) this.getCurrentCategory();
		Unsaga.debug(args[0],this.getClass());
		return args;
	}

	public String getGuiTextureName(){
		return Unsaga.DOMAIN + ":textures/gui/smith.png";
	}
	public String getGuiName(){
		String str = "";
		if(this.villager!=null){
			if(ExtendedEntityTag.hasTag(villager, ContainerSmithUnsaga.SMITH_TYPE_REPAIR_PRO)){
				str = "gui.smith.repairPro";
			}
			if(ExtendedEntityTag.hasTag(villager, ContainerSmithUnsaga.SMITH_TYPE_ADD_ABILTY)){
				str = "gui.smith.abilityPro";
			}
		}
		return "Forging<"+Translation.localize(str)+">";
	}


	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;


		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		this.addButton(DOFORGE, i + (18*5)+2, j + 16 +(18*2), 30, 19 , "Forge!");
		this.addButton(CATEGORY, i + (18*3)+2, j + 16 +(18*2), 31, 19 , "Category");
	}

	@Override
	public void prePacket(GuiButton par1){
		if(par1.id==CATEGORY){
			this.currentCategory += 1;
			if(this.currentCategory>=ToolCategory.toolArray.size()){
				this.currentCategory = 0;
			}
		}
	}


	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{

		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRendererObj.drawString(ToolCategory.toolArray.get(this.currentCategory).toString(), 8, 42, 0x404040);
		//fontRenderer.drawString("Result:"+getSpellStr(), 8, (ySize - 96) + 2, 0x404040);
	}

}
