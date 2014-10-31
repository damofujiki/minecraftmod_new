package com.hinasch.unlsaga.client.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hinasch.unlsaga.client.model.ModelUnsagaGolem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGolemUnsaga extends RenderBiped
{
    private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");
    /**
     * Iron Golem's Model.
     */
    private final ModelUnsagaGolem ironGolemModel;

    public RenderGolemUnsaga()
    {
        super(new ModelUnsagaGolem(1.0F), 1.0F);
        this.ironGolemModel = (ModelUnsagaGolem)this.mainModel;
    }

    protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving)
    {
        return ironGolemTextures;
    }
    
    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        GL11.glTranslatef(-1.825F, 0.625F, 0.0F);
        GL11.glScalef(3.0F,3.0F,3.0F);
    	super.renderEquippedItems(par1EntityLiving, par2);
    }
//    /**
//     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
//     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
//     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
//     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
//     */
//    public void doRender(EntityGolemUnsaga par1EntityIronGolem, double par2, double par4, double par6, float par8, float par9)
//    {
//        super.doRender((EntityLiving)par1EntityIronGolem, par2, par4, par6, par8, par9);
//    }
//
//    /**
//     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
//     */
//    protected ResourceLocation getEntityTexture(EntityGolemUnsaga par1EntityIronGolem)
//    {
//        return ironGolemTextures;
//    }
//
//    protected void rotateCorpse(EntityGolemUnsaga par1EntityIronGolem, float par2, float par3, float par4)
//    {
//        super.rotateCorpse(par1EntityIronGolem, par2, par3, par4);
//
//        if ((double)par1EntityIronGolem.limbSwingAmount >= 0.01D)
//        {
//            float f3 = 13.0F;
//            float f4 = par1EntityIronGolem.limbSwing - par1EntityIronGolem.limbSwingAmount * (1.0F - par4) + 6.0F;
//            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
//            GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
//        }
//    }
//
//    protected void renderEquippedItems(EntityGolemUnsaga par1EntityIronGolem, float par2)
//    {
//        super.renderEquippedItems(par1EntityIronGolem, par2);
//        GL11.glColor3f(1.0F, 1.0F, 1.0F);
//        ItemStack itemstack = par1EntityIronGolem.getHeldItem();
//        ItemStack itemstack1 = par1EntityIronGolem.func_130225_q(3);
//        Item item;
//        float f1;
//
//        if (itemstack1 != null)
//        {
//            GL11.glPushMatrix();
//            this.ironGolemModel.ironGolemHead.postRender(0.0625F);
//            item = itemstack1.getItem();
//
//            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, EQUIPPED);
//            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack1, BLOCK_3D));
//
//            if (item instanceof ItemBlock)
//            {
//                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))
//                {
//                    f1 = 0.625F;
//                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
//                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
//                    GL11.glScalef(f1, -f1, -f1);
//                }
//
//                this.renderManager.itemRenderer.renderItem(par1EntityIronGolem, itemstack1, 0);
//            }
//            else if (item == Items.skull)
//            {
//                f1 = 1.0625F;
//                GL11.glScalef(f1, -f1, -f1);
//                String s = "";
//
//                if (itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("SkullOwner", 8))
//                {
//                    s = itemstack1.getTagCompound().getString("SkullOwner");
//                }
//
//                TileEntitySkullRenderer.field_147536_b.func_147530_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), s);
//            }
//
//            GL11.glPopMatrix();
//        }
//
//        if (itemstack != null && itemstack.getItem() != null)
//        {
//            item = itemstack.getItem();
//            GL11.glPushMatrix();
//
//            if (this.mainModel.isChild)
//            {
//                f1 = 0.5F;
//                GL11.glTranslatef(0.0F, 0.625F, 0.0F);
//                GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
//                GL11.glScalef(f1, f1, f1);
//            }
//
//            this.ironGolemModel.ironGolemLeftArm.postRender(0.0625F);
//            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
//
//            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
//            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D));
//
//            if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())))
//            {
//                f1 = 0.5F;
//                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
//                f1 *= 0.75F;
//                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
//                GL11.glScalef(-f1, -f1, f1);
//            }
//            else if (item == Items.bow)
//            {
//                f1 = 0.625F;
//                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
//                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
//                GL11.glScalef(f1, -f1, f1);
//                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
//            }
//            else if (item.isFull3D())
//            {
//                f1 = 0.625F;
//
//                if (item.shouldRotateAroundWhenRendering())
//                {
//                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
//                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
//                }
//
//                this.func_82422_c();
//                GL11.glScalef(f1, -f1, f1);
//                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
//            }
//            else
//            {
//                f1 = 0.375F;
//                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
//                GL11.glScalef(f1, f1, f1);
//                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
//                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
//            }
//
//            float f2;
//            float f3;
//            int j;
//
//            if (itemstack.getItem().requiresMultipleRenderPasses())
//            {
//                for (j = 0; j <= 1; ++j)
//                {
//                    int i = itemstack.getItem().getColorFromItemStack(itemstack, j);
//                    f2 = (float)(i >> 16 & 255) / 255.0F;
//                    f3 = (float)(i >> 8 & 255) / 255.0F;
//                    float f4 = (float)(i & 255) / 255.0F;
//                    GL11.glColor4f(f2, f3, f4, 1.0F);
//                    for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++)
//                    {
//                        this.renderManager.itemRenderer.renderItem(par1EntityIronGolem, itemstack, x);
//                    }
//                }
//            }
//            else
//            {
//                j = itemstack.getItem().getColorFromItemStack(itemstack, 0);
//                float f5 = (float)(j >> 16 & 255) / 255.0F;
//                f2 = (float)(j >> 8 & 255) / 255.0F;
//                f3 = (float)(j & 255) / 255.0F;
//                GL11.glColor4f(f5, f2, f3, 1.0F);
//                this.renderManager.itemRenderer.renderItem(par1EntityIronGolem, itemstack, 0);
//            }
//
//            GL11.glPopMatrix();
//        }
//    }
//
//    /**
//     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
//     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
//     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
//     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
//     */
//    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
//    {
//        this.doRender((EntityGolemUnsaga)par1EntityLiving, par2, par4, par6, par8, par9);
//    }
//
//    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
//    {
//        this.renderEquippedItems((EntityGolemUnsaga)par1EntityLivingBase, par2);
//    }
//
//    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
//    {
//        this.rotateCorpse((EntityGolemUnsaga)par1EntityLivingBase, par2, par3, par4);
//    }
//
//    /**
//     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
//     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
//     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
//     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
//     */
//    public void doRender(EntityLivingBase par1Entity, double par2, double par4, double par6, float par8, float par9)
//    {
//        this.doRender((EntityGolemUnsaga)par1Entity, par2, par4, par6, par8, par9);
//    }
//
//    /**
//     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
//     */
//    protected ResourceLocation getEntityTexture(Entity par1Entity)
//    {
//        return this.getEntityTexture((EntityGolemUnsaga)par1Entity);
//    }
//
//    /**
//     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
//     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
//     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
//     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
//     */
//    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
//    {
//        this.doRender((EntityGolemUnsaga)par1Entity, par2, par4, par6, par8, par9);
//    }
//    
//
//    
//    protected void func_82422_c()
//    {
//        GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
//    }
}