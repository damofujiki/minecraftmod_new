package com.hinasch.unlsaga.client.render.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.weapon.ItemBowUnsaga;

public class RenderItemWeapon implements IItemRenderer{

	protected DebugUnsaga debugdata ;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		// TODO 自動生成されたメソッド・スタブ
		return type == ItemRenderType.EQUIPPED;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


	public void renderItemDo(ItemRenderType type, ItemStack item, int pass,Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		IIcon icon = null;
		
		if(this.debugdata==null){
			this.debugdata = Unsaga.proxy.getDebugUnsaga().get();
		}
		
		if(item.getItem() instanceof ItemBowUnsaga && entity instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer)entity;
			icon = item.getItem().getIcon(item, pass, ep, item, ep.getItemInUseCount());
		}else{
			icon = item.getItem().getIconFromDamageForRenderPass(0, pass);
		}
		
		
        Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		
        int k = item.getItem().getColorFromItemStack(item, pass);
        float f31 = (float)(k >> 16 & 255) / 255.0F;
        float f32 = (float)(k >> 8 & 255) / 255.0F;
        float f33 = (float)(k & 255) / 255.0F;
        GL11.glColor4f(f31, f32, f33, 1.0F);
        //texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));

        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = 1.0F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
        float fx;
        float fy;
        float fz;
        float fr;
        float fr1,fr2;
        if(item.getItem() instanceof ItemBowUnsaga){
        	fx = -0.357F;
        	fy = 0.397F;
        	fz = -0.18F;
        	fr = 227.0F;
        	fr1 = -0.24F;
        	fr2 = 2.18F;
//        	if(entity instanceof EntitySkeleton){
//        		
//        		fx = -0.9375F;
//        		fy = -0.0625F;
//        		fz = -0.0F;
//        		fr = 335.0F;
//        	}
        }else{
        	fx = -0.587F;
        	fy = 0.56F;
        	fz = 0.58F;
        	fr = 335.0F;
        	fr1 = 0.0F;
        	fr2 = 0.0F;
        }
        GL11.glRotatef(fr,fr1,fr2,1.0F);
        GL11.glTranslatef(fx,fy,fz);
        ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        
        if (item.hasEffect(pass))
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

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// TODO 自動生成されたメソッド・スタブ
		if(!item.getItem().requiresMultipleRenderPasses()){
			this.renderItemDo(type, item, 0,(Entity) data[1]);
			return;
		}
        for (int j = 0; j < item.getItem().getRenderPasses(item.getItemDamage()); ++j)
        {

            this.renderItemDo(type, item, j, (Entity) data[1]);
        }
	}

}
