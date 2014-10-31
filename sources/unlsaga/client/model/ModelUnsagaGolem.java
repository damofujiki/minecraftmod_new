package com.hinasch.unlsaga.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelUnsagaGolem extends ModelBiped
{
//    /**
//     * The head model for the iron golem.
//     */
//    public ModelRenderer ironGolemHead;
//    /**
//     * The body model for the iron golem.
//     */
//    public ModelRenderer ironGolemBody;
//    /**
//     * The right arm model for the iron golem.
//     */
//    public ModelRenderer ironGolemRightArm;
//    /**
//     * The left arm model for the iron golem.
//     */
//    public ModelRenderer ironGolemLeftArm;
//    /**
//     * The left leg model for the Iron Golem.
//     */
//    public ModelRenderer ironGolemLeftLeg;
//    /**
//     * The right leg model for the Iron Golem.
//     */
//    public ModelRenderer ironGolemRightLeg;


    public ModelUnsagaGolem()
    {
        this(0.0F);
    }

    public ModelUnsagaGolem(float par1)
    {
        this(par1, -10.0F);
    }

    public ModelUnsagaGolem(float par1, float par2)
    {
        short short1 = 128;
        short short2 = 128;
        this.bipedHead = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, -2.0F);
        this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, par1);
        this.bipedHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, par1);
        this.bipedBody = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.bipedBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, par1);
        this.bipedBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, par1 + 0.5F);
        this.bipedRightArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.bipedRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, par1);
        this.bipedLeftArm = (new ModelRenderer(this)).setTextureSize(short1, short2);
        this.bipedLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, par1);
        this.bipedLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
        this.bipedLeftLeg.setRotationPoint(-4.0F, 18.0F + par2, 0.0F);
        this.bipedLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, par1);
        this.bipedRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(short1, short2);
        this.bipedRightLeg.mirror = true;
        this.bipedRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + par2, 0.0F);
        this.bipedRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, par1);
    }
    
   
    protected void func_82422_c()
    {
    	Unsaga.debug("test",this.getClass());
        GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
    }
    
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

        if (this.isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
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
            float f6 =2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F * f6, 1.5F * f6, 1.5F* f6);
            GL11.glTranslatef(0.0F, -0.7F, 0.0F);
            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
            GL11.glPopMatrix();
        }
    }
//
//    /**
//     * Sets the models various rotation angles then renders the model.
//     */
//    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
//    {
//        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
//        this.bipedHead.render(par7);
//        this.bipedBody.render(par7);
//        this.bipedLeftLeg.render(par7);
//        this.bipedRightLeg.render(par7);
//        this.bipedRightArm.render(par7);
//        this.bipedLeftArm.render(par7);
//    }
//
//    /**
//     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
//     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
//     * "far" arms and legs can swing at most.
//     */
//    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
//    {
//        this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
//        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
//        this.bipedLeftLeg.rotateAngleX = -1.5F * this.func_78172_a(par1, 13.0F) * par2;
//        this.bipedRightLeg.rotateAngleX = 1.5F * this.func_78172_a(par1, 13.0F) * par2;
//        this.bipedLeftLeg.rotateAngleY = 0.0F;
//        this.bipedRightLeg.rotateAngleY = 0.0F;
//    }
//
//    /**
//     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
//     * and third as in the setRotationAngles method.
//     */
//    public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
//    {
//        EntityGolemUnsaga entityirongolem = (EntityGolemUnsaga)par1EntityLivingBase;
////        int i = entityirongolem.getAttackTimer();
////
////        if (i > 0)
////        {
////            this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
////            this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
////        }
////        else
////        {
////            int j = entityirongolem.getHoldRoseTick();
////
////            if (j > 0)
////            {
////                this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * this.func_78172_a((float)j, 70.0F);
////                this.ironGolemLeftArm.rotateAngleX = 0.0F;
////            }
////            else
////            {
////                this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
////                this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
////            }
////        }
//    }
//
//    private float func_78172_a(float par1, float par2)
//    {
//        return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
//    }
}