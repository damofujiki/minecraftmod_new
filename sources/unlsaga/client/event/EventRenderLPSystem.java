package com.hinasch.unlsaga.client.event;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.RenderHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.event.extendeddata.ExtendedDataLP;
import com.hinasch.unlsaga.event.extendeddata.ExtendedLivingData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventRenderLPSystem {

	public static class RendererLP{
		
		public RenderManager renderManager = RenderManager.instance;
		public RendererLP(){
			
		}
		
		public static RendererLP getEvent(){
			return new RendererLP();
		}
		@SubscribeEvent
		public void onRenderLivingPost(RenderLivingEvent.Post e){
	
			if(!Unsaga.lpManager.isLPEnabled())return;
			
			FontRenderer fontRenderer = e.renderer.getFontRendererFromRenderManager();
			ExtendedDataLP data = Unsaga.lpManager.getData(e.entity);
			if(HSLibs.isSameTeam(Minecraft.getMinecraft().thePlayer, e.entity)){
				String str = data.getLP()+"/"+Unsaga.lpManager.getMaxLP(e.entity);
				RenderHelper.renderStringAt(e.entity, str, new XYZPos(e.x,e.y,e.z), 100, 0xffffff, fontRenderer, renderManager);
			}
			if(data.isDirty()){
				if(data.renderTextPos==null){
					data.renderTextPos = new XYZPos(e.x,e.y,e.z);
				}
				XYZPos p = data.renderTextPos;
				data.renderTick -= 1;
				String str = String.valueOf(data.damage);
				RenderHelper.renderStringAt(e.entity, str, new XYZPos(e.x,e.y,e.z), 100, 0xff0000, fontRenderer, renderManager);
				//this.renderStringTag(e.entity, String.valueOf(data.damage), p.dx, p.dy,p.dz, 100, e);
				if(data.renderTick<0){
					data.renderTick = 0;
					data.markDirty(false);
					data.renderTextPos = null;
				}
			}
			
		}
	
	}

	public static class RenderGameOverlayLP{
		protected FontRenderer fontRenderer;
		public RenderGameOverlayLP(){
			
		}
		public static RenderGameOverlayLP getEvent(){
			return new RenderGameOverlayLP();
		}
		
		@SubscribeEvent
		public void onRenderStatus(RenderGameOverlayEvent e){

			
			
			if(this.fontRenderer==null){
				this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
			}
			EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
			if(e.type==RenderGameOverlayEvent.ElementType.TEXT){
				if(Unsaga.lpManager.isLPEnabled()){
					String str = "LP:"+String.valueOf(Unsaga.lpManager.getData(clientPlayer).getLP());
					fontRenderer.drawString(str, 10, 10, 0xffffff);
				}

				
				
				Set<LivingDebuff> debuffs = ExtendedLivingData.getData(clientPlayer).getProgressDebuffs();
				String  debuffstr = "";
				//Unsaga.debug(debuffs);
				for(LivingDebuff debuff:debuffs){
					
					if(debuff.getDebuff().isDisplayDebuff()){
						debuffstr += debuff.getDebuff().name + ",";
					}
					
				}
				fontRenderer.drawString(debuffstr, 10, 20, 0xffffff);
			}
	
		}
	}

}
