package com.hinasch.unlsaga.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.hinasch.unlsaga.Unsaga;

public class ModelArmorColored extends ModelBiped{

	protected ItemStack itemStackArmor;
	
	
	public ModelArmorColored(float f) {
		super(f);
	}

	public void setItemStack(ItemStack is){
		this.itemStackArmor = is;
	}
	
	@Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

        if (this.isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            
            int k = this.itemStackArmor.getItem().getColorFromItemStack(itemStackArmor, 0);
            Unsaga.debug("armor:"+k);
            float f31 = (float)(k >> 16 & 255) / 255.0F;
            float f32 = (float)(k >> 8 & 255) / 255.0F;
            float f33 = (float)(k & 255) / 255.0F;
            GL11.glColor4f(f31, f32, f33, 1.0F);
            
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
            this.bipedHead.render(par7);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
            GL11.glPopMatrix();
        }
        else
        {
        	
            int k = this.itemStackArmor.getItem().getColorFromItemStack(itemStackArmor, 0);

            float f31 = (float)(k >> 16 & 255) / 255.0F;
            float f32 = (float)(k >> 8 & 255) / 255.0F;
            float f33 = (float)(k & 255) / 255.0F;
            GL11.glColor4f(f31, f32, f33, 1.0F);
            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
        }
    }
}
