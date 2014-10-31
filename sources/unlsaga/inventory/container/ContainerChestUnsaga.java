package com.hinasch.unlsaga.inventory.container;

import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.AbilityRegistry;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.client.gui.GuiChest;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.HelperChestUnsaga;
import com.hinasch.unlsaga.util.translation.Translation;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerChestUnsaga extends ContainerBase{

	public TileEntityChestUnsagaNew chest;
	public HelperChestUnsaga helper;
	protected EntityPlayer openPlayer;
	protected boolean setClose;

	protected AbilityRegistry abilities;
	//protected int id;


	public ContainerChestUnsaga(TileEntityChestUnsagaNew chest, EntityPlayer ep) {
		super(ep, chest);
		this.abilities = Unsaga.abilityManager;
		this.openPlayer = ep;
		this.chest = chest;
		this.setClose = false;
		this.helper = new HelperChestUnsaga(chest);

		
	}
	
	@Override
	public boolean isShowPlayerInv(){
		return false;
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		if(this.chest==null){
			return false;
		}
		if(this.chest.hasChestOpened()){
			return false;
		}
		if(this.openPlayer.openContainer==this){
			if(this.setClose){
				return false;
			}
			return this.openPlayer==entityplayer;
		}

		return false;

	}


	@Override
	public void  onPacketData() {
		//HSLibs.closeScreen(openPlayer);
		boolean noAbility = true;
		openPlayer.closeScreen();
		switch(buttonID){
		case GuiChest.OPEN:
			this.chest.activateChest(this.openPlayer);
			break;
		case GuiChest.DEFUSE:
			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.defuse)){
				this.helper.tryDefuse(openPlayer);
				noAbility = false;
			}
			break;
		case GuiChest.DIVINATION:
			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.divination)){
				this.helper.divination(openPlayer);
				noAbility = false;
			}
			break;
		case GuiChest.UNLOCK:
			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.unlock)){
				noAbility = false;
			}
			break;
		case GuiChest.PENETRATION:
			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.penetration)){
				this.helper.tryPenetration(openPlayer);
				noAbility = false;
			}
		}

		if(noAbility){
			ChatMessageHandler.sendChatToPlayer(openPlayer, Translation.localize("msg.has.noability"));
		}
	}
	public boolean playerHasAbility(Ability ab) {
		// TODO 自動生成されたメソッド・スタブ
		return HelperAbility.getAmountAbility(openPlayer, ab)>0;
	}

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			Object... args) {
		// TODO 自動生成されたメソッド・スタブ
		return new PacketGuiButtonNew(guiID,buttonID);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return guiNumber.CHEST;
	}
}
