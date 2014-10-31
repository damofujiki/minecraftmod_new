package com.hinasch.unlsaga.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import com.hinasch.lib.ClientHelper;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.EventInteractEntity;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {

	private final KeyBinding openEquipmentGUI  = new KeyBinding("Open Gui[Equipment,Bartering,Smith]", Keyboard.KEY_COMMA, Unsaga.MODID);
	//private final KeyBinding debugKeyOnCreative = new KeyBinding("Attach Ability To Tool(on Creative)",Keyboard.KEY_M,Unsaga.MODID);
	
	public KeyHandler(){
		ClientHelper.registerKeyBinding(openEquipmentGUI);
	}
	
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent e){
		if(openEquipmentGUI.isPressed()){
			MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
			if(mop!=null){
				if(mop.entityHit!=null && mop.entityHit instanceof EntityVillager){
					EventInteractEntity.interactsVillager(null,Minecraft.getMinecraft().thePlayer, (EntityVillager) mop.entityHit,false);
					return;
				}
			}
			PacketGuiOpen pgo = new PacketGuiOpen(Unsaga.guiNumber.EQUIP);
			Unsaga.packetPipeline.sendToServer(pgo);
		}
//		if(this.debugKeyOnCreative.isPressed()){
//			PacketSkill ps = new PacketSkill(PacketSkill.PACKETID.DEBUG_ABILITY);
//			Unsaga.packetPipeline.sendToServer(ps);
//		}
	}
}
