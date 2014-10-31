package com.hinasch.unlsaga.client;


import net.minecraft.client.model.ModelSlime;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.google.common.base.Optional;
import com.hinasch.lib.HSLibs;
import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.event.EventRenderLPSystem;
import com.hinasch.unlsaga.client.render.RenderGolemUnsaga;
import com.hinasch.unlsaga.client.render.RenderTreasureSlime;
import com.hinasch.unlsaga.client.render.equipment.RenderItemArmor;
import com.hinasch.unlsaga.client.render.equipment.RenderItemMusket;
import com.hinasch.unlsaga.client.render.equipment.RenderItemSpear;
import com.hinasch.unlsaga.client.render.equipment.RenderItemWeapon;
import com.hinasch.unlsaga.client.render.projectile.RenderArrowUnsaga;
import com.hinasch.unlsaga.client.render.projectile.RenderBarrett;
import com.hinasch.unlsaga.client.render.projectile.RenderFlyingAxe;
import com.hinasch.unlsaga.client.render.projectile.RenderThrowableItem;
import com.hinasch.unlsaga.entity.EntityGolemUnsaga;
import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsaga.entity.projectile.EntityArrowUnsaga;
import com.hinasch.unlsaga.entity.projectile.EntityBoulderNew;
import com.hinasch.unlsaga.entity.projectile.EntityBullet;
import com.hinasch.unlsaga.entity.projectile.EntityFireArrowNew;
import com.hinasch.unlsaga.entity.projectile.EntityFlyingAxeNew;
import com.hinasch.unlsaga.entity.projectile.EntitySolutionLiquid;
import com.hinasch.unlsaga.network.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy{

	public DebugUnsaga debugdata;
	//public UnsagaMagicHandlerClient proxyUnsagaSpell;
	public KeyHandler keyHandler;
	
	@Override
	public void registerKeyHandler(){
		this.keyHandler = new KeyHandler();
		FMLCommonHandler.instance().bus().register(keyHandler);
	}
	
	@Override
	public void registerSpearRenderer(Item par1){
		MinecraftForgeClient.registerItemRenderer(par1, new RenderItemSpear());
	}
	
	@Override
	public void registerSpecialRenderer(Item par1){
		MinecraftForgeClient.registerItemRenderer(par1, new RenderItemWeapon());
		
	}
	
	@Override
	public void registerMusketRenderer(Item par1){
		MinecraftForgeClient.registerItemRenderer(par1, new RenderItemMusket());
	}
	
	@Override
	public void registerArmorRenderer(Item par1){
		MinecraftForgeClient.registerItemRenderer(par1, new RenderItemArmor());
	}
	
	@Override
	public void registerRenderers(){
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowUnsaga.class, new RenderArrowUnsaga());
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBarrett(1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingAxeNew.class, new RenderFlyingAxe(1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityTreasureSlime.class, new RenderTreasureSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGolemUnsaga.class, new RenderGolemUnsaga());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireArrowNew.class, new RenderThrowableItem(1.0F,Items.fire_charge,0));
		RenderingRegistry.registerEntityRenderingHandler(EntityBoulderNew.class, new RenderThrowableItem(1.0F,Unsaga.items.materials,18));
		RenderingRegistry.registerEntityRenderingHandler(EntitySolutionLiquid.class, new RenderThrowableItem(1.0F,Items.slime_ball,0));

		HSLibs.registerEvent(new ParticlesUnsaga());
		HSLibs.registerEvent(EventRenderLPSystem.RendererLP.getEvent());
		HSLibs.registerEvent(EventRenderLPSystem.RenderGameOverlayLP.getEvent());
		

	}
	
	@Override
	public void setDebugUnsaga(){
		Unsaga.debug("[UnsagaMod]デバッグユーティリティの準備");
		
		if(Unsaga.debug){
			if(debugdata==null){
				debugdata = new DebugUnsaga();
				//		GL11.glRotatef(90.0F,0.04F,-0.08F,-0.08F);
				//GL11.glTranslatef(-0.67F,0.17F,-0.3F);
				debugdata.registFloat(28.0F*4.0F, 0);
				debugdata.registFloat(53.0F,1);
			}
		}
		//KeyBindingRegistry.registerKeyBinding(new KeyHandlerTest());
	}
	
	@Override
	public Optional<DebugUnsaga> getDebugUnsaga(){
		return Optional.of(this.debugdata);
	}
	
//	public sendGuiPacket(EntityClientPlayerMP clientPlayer){
//		PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(this,clientPlayer));
//	}
//	@Override
//	public void registerGunRenderer(int itemID) {
//		MinecraftForgeClient.registerItemRenderer(itemID, new RenderItemMusket());
//		
//	}
}
