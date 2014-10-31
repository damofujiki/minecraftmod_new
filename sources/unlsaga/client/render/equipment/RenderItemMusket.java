package com.hinasch.unlsaga.client.render.equipment;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;

public class RenderItemMusket implements IItemRenderer{

	protected DebugUnsaga debugdata ;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {


		//System.out.println(type);
	
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
			return false;
		}
		if(type==ItemRenderType.EQUIPPED){
			//System.out.println("not first person");
			return true;
		}
		
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// TODO Auto-generated method stub

		Minecraft mc = Minecraft.getMinecraft();
		IIcon icon = item.getItem().getIconFromDamage(0);
		if(this.debugdata==null){
			this.debugdata = Unsaga.proxy.getDebugUnsaga().get();
		}

		//mc.renderEngine.bindTexture(UnsagaCore.fullpath+"/textures/items/spear_stone.png");
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(UnsagaCore.fullpath+"/textures/items/spear_stone.png"));



		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float f4 = 0.0F;
		float f5 = 0.3F;

		GL11.glTranslatef(-f4, -f5, 0.0F);
		float f6 = 1.5F;
		//DebugUnsaga db = UnsagaCore.proxy.getDebugUnsaga();
		//GL11.glScalef(f6,2.0F, f6);
		//GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);

		GL11.glRotatef(90.0F,0.04F,-0.08F,-0.08F);
		GL11.glTranslatef(-0.67F,0.17F,-0.3F);
	

		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);

        if (item.hasEffect(0))
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            //texturemanager.bindTexture(RES_ITEM_GLINT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
        
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

	}

}
