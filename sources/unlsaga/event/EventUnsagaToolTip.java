package com.hinasch.unlsaga.event;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.gui.GuiBartering;
import com.hinasch.unlsaga.client.gui.GuiSmithUnsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsaga.villager.bartering.MerchandiseInfo;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//買い物の時に価格を表示したり、インベントリで素材を表示したり
public class EventUnsagaToolTip {

	protected MaterialInfo info;

	protected boolean isClientInGuiBartering(){
		return Minecraft.getMinecraft().currentScreen instanceof GuiBartering;
	}

	protected boolean isClientInGuiSmithing(){
		return Minecraft.getMinecraft().currentScreen instanceof GuiSmithUnsaga;
	}

	@SubscribeEvent
	public void toolTipEvent(ItemTooltipEvent event){
		if(Unsaga.debug){
			event.toolTip.add(event.itemStack.getUnlocalizedName());
		}
		if(isClientInGuiBartering()){
			this.addBarteringTips(event);
			return;
		}
		
		if(!isClientInGuiSmithing())return;
		GuiSmithUnsaga guismith = (GuiSmithUnsaga)Minecraft.getMinecraft().currentScreen;
		int currentcategory = guismith.getCurrentCategory();
		ItemStack is = event.itemStack;
		info = new MaterialInfo(event.itemStack);
		if(info.isValidMaterial()){

			Unsaga.debug("ok",this.getClass());
			//if(info.getMaterial().isPresent()){
			UnsagaMaterial material = info.getMaterial().get();
			event.toolTip.add(Translation.localize("tips.validmaterial")+material.getLocalized());

			if(Unsaga.items.isForgeMaterial(ToolCategory.toolArray.get(currentcategory), material)){
				event.toolTip.add(Translation.localize("tips.canuseforbase")+ToolCategory.toolArray.get(currentcategory).getLocalized());

			}
			//}
			//MaterialLibrary info = MaterialLibrary.findInfo(event.itemStack).get();

		}

	}

	protected void addBarteringTips(ItemTooltipEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		if(MerchandiseInfo.hasBuyPriceTag(event.itemStack)){
			MerchandiseInfo merchandiseInfo = new MerchandiseInfo(event.itemStack);
			if(Minecraft.getMinecraft().currentScreen instanceof GuiBartering){
				GuiBartering gui = (GuiBartering) Minecraft.getMinecraft().currentScreen;
				if(gui.priceDown>0 || gui.priceUp>0){
					event.toolTip.add("Cost:"+merchandiseInfo.getBuyPriceTagWithDiscount(gui.priceDown,gui.priceUp));
					return;
				}
			}
			event.toolTip.add("Cost:"+merchandiseInfo.getBuyPriceTag());
		}else{
			if(MerchandiseInfo.isPossibleToSell(event.itemStack)){
				MerchandiseInfo merchandiseInfo = new MerchandiseInfo(event.itemStack);
				event.toolTip.add("Sell Price:"+merchandiseInfo.getPrice());
			}

		}


	}
}
