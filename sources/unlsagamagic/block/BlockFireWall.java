package com.hinasch.unlsagamagic.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsagamagic.element.IUnsagaElements;
import com.hinasch.unlsagamagic.spell.SpellMixTable;
import com.hinasch.unlsagamagic.tileentity.TileEntityFireWall;

public class BlockFireWall extends BlockContainer implements IUnsagaElements{

	public IIcon[] iconArray;
	private IIcon[] theIcon;

	public BlockFireWall() {
		super(Material.circuits);

	}

	@Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }



	@Override
	public IIcon getIcon(int par1, int par2)
	{
		return par1 != 0 && par1 != 1 ? this.theIcon[1] : this.theIcon[0];
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		if(par5Entity instanceof EntityLivingBase){

			EntityLivingBase living = (EntityLivingBase)par5Entity;
			if(!living.isPotionActive(Potion.fireResistance.id)){
				float damage = (float)par1World.getBlockMetadata(par2, par3, par4);
				damage = MathHelper.clamp_float(damage, 2.0F, 9.0F);
				living.attackEntityFrom(DamageSource.inFire, damage);
				living.setFire(5);
				float yaw = MathHelper.wrapAngleTo180_float(par5Entity.rotationYaw + 180.0F);
				float i = 1.0F;
				living.addVelocity((double)(-MathHelper.sin((yaw) * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(yaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));


			}
		}
		if(par5Entity instanceof EntityThrowable || par5Entity instanceof IProjectile || par5Entity instanceof EntityFireball){
			par5Entity.setDead();
		}

	}




	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.theIcon = new IIcon[] {par1IconRegister.registerIcon("lava_still"), par1IconRegister.registerIcon("lava_flow")};
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		Blocks.fire.randomDisplayTick(par1World, par2, par3, par4, par5Random);
		
		
//		if (par5Random.nextInt(24) == 0)
//		{
//			par1World.playSound((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F, false);
//		}
//
//		int l;
//		float f;
//		float f1;
//		float f2;
//
//		if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !Blocks.fire.canBlockCatchFire(par1World, par2, par3 - 1, par4, UP))
//		{
//			if (Block.fire.canBlockCatchFire(par1World, par2 - 1, par3, par4, EAST))
//			{
//				for (l = 0; l < 2; ++l)
//				{
//					f = (float)par2 + par5Random.nextFloat() * 0.1F;
//					f1 = (float)par3 + par5Random.nextFloat();
//					f2 = (float)par4 + par5Random.nextFloat();
//					par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//				}
//			}
//
//			if (Block.fire.canBlockCatchFire(par1World, par2 + 1, par3, par4, WEST))
//			{
//				for (l = 0; l < 2; ++l)
//				{
//					f = (float)(par2 + 1) - par5Random.nextFloat() * 0.1F;
//					f1 = (float)par3 + par5Random.nextFloat();
//					f2 = (float)par4 + par5Random.nextFloat();
//					par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//				}
//			}
//
//			if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 - 1, SOUTH))
//			{
//				for (l = 0; l < 2; ++l)
//				{
//					f = (float)par2 + par5Random.nextFloat();
//					f1 = (float)par3 + par5Random.nextFloat();
//					f2 = (float)par4 + par5Random.nextFloat() * 0.1F;
//					par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//				}
//			}
//
//			if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 + 1, NORTH))
//			{
//				for (l = 0; l < 2; ++l)
//				{
//					f = (float)par2 + par5Random.nextFloat();
//					f1 = (float)par3 + par5Random.nextFloat();
//					f2 = (float)(par4 + 1) - par5Random.nextFloat() * 0.1F;
//					par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//				}
//			}
//
//			if (Block.fire.canBlockCatchFire(par1World, par2, par3 + 1, par4, DOWN))
//			{
//				for (l = 0; l < 2; ++l)
//				{
//					f = (float)par2 + par5Random.nextFloat();
//					f1 = (float)(par3 + 1) - par5Random.nextFloat() * 0.1F;
//					f2 = (float)par4 + par5Random.nextFloat();
//					par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//				}
//			}
//		}
//		else
//		{
//			for (l = 0; l < 3; ++l)
//			{
//				f = (float)par2 + par5Random.nextFloat();
//				f1 = (float)par3 + par5Random.nextFloat() * 0.5F + 0.5F;
//				f2 = (float)par4 + par5Random.nextFloat();
//				par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
//			}
//		}
	}

//	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5, ItemStack par6ItemStack)
//	{
//		return true;
//	}

	//	@Override
	//    public void registerIcons(IconRegister par1IconRegister)
	//    {
	//        this.iconArray = new Icon[] {par1IconRegister.registerIcon(this.getTextureName() + "_layer_0"), par1IconRegister.registerIcon(this.getTextureName() + "_layer_1")};
	//    }

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
	{
		XYZPos pos = new XYZPos(par2,par3,par4);
		if(HSLibs.checkAroundMaterial(par1World, pos, Material.water)){
			this.triggerLavaMixEffects(par1World, par2, par3, par4);
			par1World.setBlockToAir(par2, par3, par4);
		}

	}

	@Override
	public boolean isCollidable()
	{
		return true;
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		if (par1World.provider.dimensionId > 0 || par1World.getBlock(par2, par3 - 1, par4) != Blocks.obsidian || !Blocks.portal.func_150000_e(par1World, par2, par3, par4))
		{

			par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate(par1World) + par1World.rand.nextInt(10));

		}
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}



	
	protected void triggerLavaMixEffects(World par1World, int par2, int par3, int par4)
	{
		par1World.playSoundEffect((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

		for (int l = 0; l < 8; ++l)
		{
			par1World.spawnParticle("largesmoke", (double)par2 + Math.random(), (double)par3 + 1.2D, (double)par4 + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public SpellMixTable getElements() {
		// TODO 自動生成されたメソッド・スタブ
		return new SpellMixTable(1.0F,0,0,-0.5F,0,0);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		
		return new TileEntityFireWall();
	}
	

}
