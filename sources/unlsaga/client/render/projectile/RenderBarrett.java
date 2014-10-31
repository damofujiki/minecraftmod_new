package com.hinasch.unlsaga.client.render.projectile;


import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.entity.projectile.EntityBullet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBarrett extends Render
{
	
	//MMM氏のものをコピペ
    private float field_77002_a;
    private IIcon loadicon;
    
    private static final ResourceLocation arrowTextures = new ResourceLocation(Unsaga.DOMAIN,"textures/items/barrett.png");


    public RenderBarrett(float par1)
    {
        this.field_77002_a = par1;
        //this.loadicon = par2;
    }

    public void doRenderBarrett(EntityBullet par1EntityBarrett, double d, double d1, double d2, float f, float f1)
    {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef((par1EntityBarrett.prevRotationYaw + (par1EntityBarrett.rotationYaw - par1EntityBarrett.prevRotationYaw) * f1) - 90F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(par1EntityBarrett.prevRotationPitch + (par1EntityBarrett.rotationPitch - par1EntityBarrett.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.instance;
		int i = 0;
		float f10 = 0.05625F;
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glScalef(f10, f10, f10);
		
		GL11.glPushMatrix();
		GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-4.7F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(0.5F, 0.25F, 0.0F, 1.0F);
		tessellator.addVertex(4.5D, -0.5D, 0.0D);
		tessellator.addVertex(4.5D, 0.0D, -0.5D);
		tessellator.addVertex(4.5D, 0.5D, 0.0D);
		tessellator.addVertex(4.5D, 0.0D, 0.5D);
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(0.4F, 0.25F, 0.0F, 1.0F);
		tessellator.addVertex(4.5D, 0.0D, 0.5D);
		tessellator.addVertex(4.5D, 0.5D, 0.0D);
		tessellator.addVertex(4.5D, 0.0D, -0.5D);
		tessellator.addVertex(4.5D, -0.5D, 0.0D);
		tessellator.draw();
		for (int j = 0; j < 4; j++) {
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0.5F, 0.25F, 0.0F, 1.0F);
			tessellator.addVertex(4.5D, -0.5D, 0.0D);
			tessellator.addVertex(6.5D, -0.5D, 0.0D);
			tessellator.addVertex(6.5D, 0.5D, 0.0D);
			tessellator.addVertex(4.5D, 0.5D, 0.0D);
			tessellator.draw();
		}
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderBarrett((EntityBullet)par1Entity, par2, par4, par6, par8, par9);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
