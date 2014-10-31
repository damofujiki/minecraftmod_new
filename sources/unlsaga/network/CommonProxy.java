package com.hinasch.unlsaga.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import com.hinasch.lib.HSLibs;
import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.client.gui.GuiBartering;
import com.hinasch.unlsaga.client.gui.GuiCarrier;
import com.hinasch.unlsaga.client.gui.GuiChest;
import com.hinasch.unlsaga.client.gui.GuiEquipment;
import com.hinasch.unlsaga.client.gui.GuiSkillPanel;
import com.hinasch.unlsaga.client.gui.GuiSmithUnsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.inventory.container.ContainerBartering;
import com.hinasch.unlsaga.inventory.container.ContainerCarrier;
import com.hinasch.unlsaga.inventory.container.ContainerChestUnsaga;
import com.hinasch.unlsaga.inventory.container.ContainerEquipment;
import com.hinasch.unlsaga.inventory.container.ContainerSkillPanel;
import com.hinasch.unlsaga.inventory.container.ContainerSmithUnsaga;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsagamagic.client.GuiBlender;
import com.hinasch.unlsagamagic.inventory.container.ContainerBlender;

import cpw.mods.fml.common.network.IGuiHandler;





public class CommonProxy implements IGuiHandler{

	public DebugUnsaga debugdata;
	//public UnsagaMagicContainerHandler clientHandlerMagic;

	public CommonProxy(){
		//this.clientHandlerMagic = new UnsagaMagicContainerHandler();
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(Minecraft.getMinecraft().currentScreen!=null)return null;
		switch(ID){
		case guiNumber.EQUIP:
			return new GuiEquipment(player);
		case guiNumber.SMITH:
			return new GuiSmithUnsaga(new NpcMerchant(player), world, player);
		case guiNumber.BARTERING:
			return new GuiBartering(new NpcMerchant(player), world, player);
		case guiNumber.BLENDER:
			return new GuiBlender(player,world);
		case guiNumber.CHEST:
			TileEntityChestUnsagaNew chest = (TileEntityChestUnsagaNew)world.getTileEntity(x, y, z);
			if(chest!=null){
				return new GuiChest(chest,player);
			}
		case guiNumber.SKILLPANEL:
			Unsaga.debug("ok",this.getClass());
			return new GuiSkillPanel(player);
		case guiNumber.CARRIER:
			return new GuiCarrier(player);
			
		}

		return null;
	}

	public Optional<DebugUnsaga> getDebugUnsaga(){
		return Optional.of(this.debugdata);
	}




	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(player.openContainer != player.inventoryContainer)return null;
		switch(ID){
		case guiNumber.EQUIP:
			return new ContainerEquipment(player.inventory,player);
		case guiNumber.SMITH:
			if(HSLibs.getExtendedData(ExtendedPlayerData.KEY, player).isPresent()){
				ExtendedPlayerData data = (ExtendedPlayerData)HSLibs.getExtendedData(ExtendedPlayerData.KEY, player).get();
				if(data.getMerchant().isPresent()){
					Unsaga.debug(data.getMerchant().get(),this.getClass());
					return new ContainerSmithUnsaga(data.getMerchant().get(), world, player);
				}

			}
		case guiNumber.BARTERING:
			if(HSLibs.getExtendedData(ExtendedPlayerData.KEY, player).isPresent()){
				ExtendedPlayerData data = (ExtendedPlayerData)HSLibs.getExtendedData(ExtendedPlayerData.KEY, player).get();
				if(data.getMerchant().isPresent()){
					Unsaga.debug(data.getMerchant().get(),this.getClass());
					return new ContainerBartering(world,player,data.getMerchant().get());
				}

			}
		case guiNumber.BLENDER:
			return new ContainerBlender(player,world);
		case guiNumber.CHEST:
			TileEntityChestUnsagaNew chest = (TileEntityChestUnsagaNew)world.getTileEntity(x, y, z);
			if(chest!=null){
				return new ContainerChestUnsaga(chest,player);
			}
		case guiNumber.SKILLPANEL:
			Unsaga.debug("ok",this.getClass());
			return new ContainerSkillPanel(player);
		case guiNumber.CARRIER:
			return new ContainerCarrier(player,null);
		}

		return null;
	}

	public void registerArmorRenderer(Item par1){

	}


	public void registerKeyHandler(){
	}


	public void registerMusketRenderer(Item par1){

	}

	public void registerRenderers(){

	}

	public void registerSpearRenderer(Item par1){

	}




	public void registerSpecialRenderer(Item par1){

	}




	public void setDebugUnsaga(){

	}


}
