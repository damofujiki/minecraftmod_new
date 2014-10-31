package com.hinasch.unlsaga.client.render.projectile;


import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.hinasch.unlsaga.entity.projectile.EntityFlyingAxeNew;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlyingAxe extends Render
{
	private float field_77002_a;


	//アイテムレンダーについて、なぜかリソースをファイル直接していするとうまくいかない
	//アイテムでそういうアイコンをもったアイコンを作ってからそれのアイコンを取得し、TextureMapでアイテムのロケーションを
	//指定するとうまくいく謎
	public RenderFlyingAxe(float par1)
	{
		this.field_77002_a = par1;
	}


	public void doRenderFlyingAxe(EntityFlyingAxeNew axe, double par2, double par4, double par6, float par8, float par9)
	{

		GL11.glPushMatrix();
		IIcon icon = Items.iron_axe.getIconFromDamage(0);

		this.bindEntityTexture(axe);


		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f2 = this.field_77002_a;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);

		if(axe.getAxeItemStack()!=null){
			int k = axe.getAxeItemStack().getItem().getColorFromItemStack(axe.getAxeItemStack(), 0);

			float f31 = (float)(k >> 16 & 255) / 255.0F;
			float f32 = (float)(k >> 8 & 255) / 255.0F;
			float f33 = (float)(k & 255) / 255.0F;
			GL11.glColor4f(f31, f32, f33, 1.0F);
		}



		//System.out.println("render:"+axe.itemstackaxe);
		//this.loadTexture("/gui/items.png");
		Tessellator tessellator = Tessellator.instance;
		float f3 = icon.getMinU();
		float f4 = icon.getMaxU();
		float f5 = icon.getMinV();
		float f6 = icon.getMaxV();
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(axe.getRotation(), 0.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
		tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
		tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
		tessellator.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.doRenderFlyingAxe((EntityFlyingAxeNew)par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO 自動生成されたメソッド・スタブ
		return TextureMap.locationItemsTexture;
	}
}
