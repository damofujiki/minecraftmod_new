package com.hinasch.lib;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hinasch.lib.base.TileEntityMultiFacing;

public class RenderTileEntityBase extends TileEntitySpecialRenderer{


	protected ModelBase model;
	public RenderTileEntityBase(){
		this.model = this.getModel();
	}
	
	public ModelBase getModel(){
		return null;
	}
	
	public String getTextureName(){
		return "";
	}
	
	public String getDomain(){
		return "";
	}

	public ResourceLocation getTextureLocation(){
		return new ResourceLocation(this.getDomain(),this.getTextureName());
	}
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {

		GL11.glPushMatrix();
		GL11.glTranslatef((float)var2,(float)var4,(float)var6);
		//GL11.glScalef((float)var8, (float)var8, (float)var8);
		if(var1.getClass()==this.getTileEntityClass()){
			renderTileEntity(var1, var1.getWorldObj(), var1.xCoord, var1.yCoord, var1.zCoord, this.getBlock());

		}
		GL11.glPopMatrix();
	}

	public Class<? extends TileEntity> getTileEntityClass(){
		return null;
	}
	
	public Block getBlock(){
		return null;
	}
    public void renderTileEntity(TileEntity tl, World world, int i, int j, int k, Block block) {

    	GL11.glPushMatrix();
    	GL11.glDisable(GL11.GL_CULL_FACE);    	
    	this.bindTexture(this.getTextureLocation());
    	GL11.glTranslatef(0.5F, 1.5F, 0.5F);
        int dir = 0;
        if(tl instanceof TileEntityMultiFacing){

            switch(ForgeDirection.getOrientation(((TileEntityMultiFacing) tl).getOrientation())){
            case NORTH:
            	dir = 2;
            	break;
            case SOUTH:
            	dir = 4;
            	break;
            case WEST:
            	dir = 3;
            	break;
            case EAST:
            	dir = 1;
            	break;
            default:
            	dir = 0;
            	break;
            }

        }

    	GL11.glRotatef(-180, 1F, 0F, 0F); //反転
    	GL11.glRotatef(dir*(-90F), 0F, 1.0F, 0F);

    	this.preRender(tl, world, dir, j, k, block);
    	this.renderModel(tl, world, dir, j, k, block);

    	GL11.glPopMatrix();
    }
    
    public void renderModel(TileEntity tl, World world, int i, int j, int k, Block block){
    	model.render((Entity)null, i, j, k, 0.0F, 0.0F, 0.0625F);
    }
    public void preRender(TileEntity tl, World world, int i, int j, int k, Block block){
    	
    }
}
