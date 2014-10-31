package com.hinasch.unlsaga.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.RangeDamageHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityFlyingAxe extends EntityThrowable implements IProjectile
{


	private int amp = 2;

	public Item itemaxe;
	private EntityItem entityitemaxe;
	//public ItemStack itemstackaxe;
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private int inData = 0;
	private EntityLivingBase target;
	private boolean isSkydrive = false;
	private boolean search = false;
	private boolean inGround = false;
	protected XYZPos startPos;
	public float rotation = 0;
	private int tick = 0;

	/** 1 if the player can pick up the arrow */
	public int canBePickedUp = 0;

	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake = 0;

	/** The owner of this arrow. */
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;
	private double damage = 2.0D;

	/** The amount of knockback an arrow applies when it hits a mob. */
	private int knockbackStrength;

	public EntityFlyingAxe(World par1World)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);

	}

	public EntityFlyingAxe(World par1World, double par2, double par4, double par6)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(par2, par4, par6);
		this.yOffset = 0.0F;
	}

	public EntityFlyingAxe(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving, float par4, float par5)
	{
		super(par1World);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = par2EntityLiving;

		if (par2EntityLiving instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.posY = par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight() - 0.10000000149011612D;
		double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
		double var8 = par3EntityLiving.posY + (double)par3EntityLiving.getEyeHeight() - 0.699999988079071D - this.posY;
		double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
		double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);

		if (var12 >= 1.0E-7D)
		{
			float var14 = (float)(Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
			float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / Math.PI));
			double var16 = var6 / var12;
			double var18 = var10 / var12;
			this.setLocationAndAngles(par2EntityLiving.posX + var16, this.posY, par2EntityLiving.posZ + var18, var14, var15);
			this.yOffset = 0.0F;
			float var20 = (float)var12 * 0.2F;
			this.setThrowableHeading(var6, var8 + (double)var20, var10, par4, par5);
		}
	}

	public EntityFlyingAxe(World par1World, EntityLivingBase par2EntityLiving, float par3,ItemStack par4,boolean par5)
	{
		super(par1World);
		this.renderDistanceWeight = 12.0D;
		this.shootingEntity = par2EntityLiving;
		this.setEntityItemStack(par4);
		this.itemaxe = this.getEntityItem().getItem();
		this.isSkydrive = par5;
		//System.out.println(this.itemstackaxe);
		//
		//        if (par2EntityLiving instanceof EntityPlayer)
		//        {
		//            this.canBePickedUp = 1;
		//        }

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		if(this.isSkydrive){
			par3 = 0;
			//this.boundingBox = this.boundingBox.expand(0.1D, 0.1D, 0.1D);
		}
		this.startPos = XYZPos.entityPosToXYZ(this);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem()
	{
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	private double cut(double par1,double par2){
		double ret = par1;
		if(par1>par2){
			ret = par2;
		}
		if(par1<-par2){
			ret = -par2;
		}
		return ret;
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
		this.getDataWatcher().addObjectByDataType(10, 5);
	}

	public double getDamage()
	{
		return this.damage;
	}


	public ItemStack getEntityItem()
    {
        ItemStack itemstack = this.getDataWatcher().getWatchableObjectItemStack(10);

        if (itemstack == null)
        {
            if (this.worldObj != null)
            {
                FMLLog.log(Level.WARN, "Item entity %d has no item?!", this.getEntityId());
            }

            return new ItemStack(Blocks.stone);
        }
        else
        {
            return itemstack;
        }
    }


	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public boolean getIsCritical()
	{
		byte var1 = this.dataWatcher.getWatchableObjectByte(16);
		return (var1 & 1) != 0;
	}
	//    
	//    public void drawParticles(World par1World, int par2, int par3, int par4, Random par5Random)
	//    {
	//    	if(par5Random.nextInt(7)==0){
	//    		return;
	//    	}
	//        int var6=0;
	//        double var7 = (double)((float)par2 + 0.5F);
	//        double var9 = (double)((float)par3 + 0.7F);
	//        double var11 = (double)((float)par4 + 0.5F);
	//        double var13 = 0.2199999988079071D;
	//        double var15 = 0.27000001072883606D;
	//
	//        if (var6 == 1)
	//        {
	//            par1World.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
	//            par1World.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
	//        }
	//        else if (var6 == 2)
	//        {
	//            par1World.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
	//            par1World.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
	//        }
	//        else if (var6 == 3)
	//        {
	//            par1World.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
	//            par1World.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
	//        }
	//        else if (var6 == 4)
	//        {
	//            par1World.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
	//            par1World.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
	//        }
	//        else
	//        {
	//            par1World.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
	//            par1World.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
	//        }
	//    }

	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}

    /**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
//		if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
//		{
//			boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;
//
//			if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(this.itemstackaxe))
//			{
//				var2 = false;
//			}
//
//			if (var2)
//			{
//				this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
//				par1EntityPlayer.onItemPickup(this, 1);
//
//				this.setDead();
//			}
//		}
	}
    
	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		float f2;
		float f3;
		AxisAlignedBB bb = HSLibs.getBounding(this.posX, this.posY, this.posZ, 3.0D, 2.0D);
		DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.shootingEntity,Unsaga.abilityManager.skyDrive.getStrHurtLP(),DamageHelper.Type.SWORDPUNCH,this);

		RangeDamageHelper.causeDamage(this.worldObj,null ,bb, DamageSource.causeThrownDamage(this, shootingEntity), (float)this.getDamage());


		if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

			if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
			{
				movingobjectposition = null;
			}
		}

		if(movingobjectposition!=null){
			if(movingobjectposition.entityHit!=null){
				if(movingobjectposition.entityHit==this.shootingEntity){
					movingobjectposition = null;
				}
			}

			//if(movingobjectposition.entityHit!=null){
//				if(movingobjectposition.typeOfHit!=null){
//				if(tick<100 && this.search && movingobjectposition.typeOfHit==EnumMovingObjectType.TILE){
//					movingobjectposition = null;
//				}
//				}
			//}
		}


		if(movingobjectposition!=null){
			this.playSound("mob.irongolem.hit", 1.5F,1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.worldObj.spawnParticle("largeexplode", this.motionX,this.motionY,this.motionZ, 1.0D, 0.0D, 0.0D);
			this.setItem();
			this.setDead();
			//				if(movingobjectposition.typeOfHit==EnumMovingObjectType.TILE){
			//	                this.playSound("mob.irongolem.hit", 1.5F,1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			//	                this.worldObj.spawnParticle("largeexplode", this.motionX,this.motionY,this.motionZ, 1.0D, 0.0D, 0.0D);
			//
			//					int bx = movingobjectposition.blockX;
			//					int by = movingobjectposition.blockY;
			//					int bz = movingobjectposition.blockZ;
			//					this.setItem();
			//					this.setDead();
			//
			//
			//				}

			if(movingobjectposition.entityHit!=null){
				//System.out.println(movingobjectposition.entityHit);

				f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

				int i1 = MathHelper.ceiling_double_int((double)f2 * this.damage);

				if (this.getIsCritical())
				{
					i1 += this.rand.nextInt(i1 / 2 + 2);
				}


				DamageSource damagesource = null;

				if (this.shootingEntity == null)
				{
					damagesource = DamageSource.causeThrownDamage(this, this);
				}
				else
				{
					damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
				}

				if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
				{
					movingobjectposition.entityHit.setFire(5);
				}

				if (movingobjectposition.entityHit.attackEntityFrom(damagesource, i1))
				{
					if (movingobjectposition.entityHit instanceof EntityLiving)
					{
						EntityLiving entityliving = (EntityLiving)movingobjectposition.entityHit;


						if (this.knockbackStrength > 0)
						{
							f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

							if (f3 > 0.0F)
							{
								movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f3, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f3);
							}
						}

						if (this.shootingEntity != null)
						{
                            EnchantmentHelper.func_151384_a(entityliving, this.shootingEntity);
                            EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, entityliving);
						}

						if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
						{
                            ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
					}


				}
				else
				{
					this.motionX *= -0.10000000149011612D;
					this.motionY *= -0.10000000149011612D;
					this.motionZ *= -0.10000000149011612D;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}


			}

		}


	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{

		super.onUpdate();
		

		this.rotation += 50.0F;


		this.tick += 1;

		Unsaga.debug("tick:"+tick+" X:"+this.motionX+" Y:"+this.motionY+" Z:"+this.motionZ);
		if(this.tick>5000){
			this.tick = 0;
		}
		this.rotation = MathHelper.wrapAngleTo180_float(this.rotation);

		if(isSkydrive){
			if(tick<10){
				//this.setPosition(startPos.dx, startPos.dy, startPos.dz);
				//this.setVelocity(0, 0, 0);

				
			}
			
			if(tick==10){
				//ターゲットがいなかったらガストを探す
				if(this.target==null){
					this.target = (EntityLivingBase) this.worldObj.findNearestEntityWithinAABB(EntityGhast.class, this.boundingBox.expand(20.0D, 20.0D, 20.0D), this);

				}
				//ガストがいないなら近くのモブ
				if(this.target==null){
					this.target = (EntityLivingBase) this.worldObj.findNearestEntityWithinAABB(EntityMob.class, this.boundingBox.expand(20.0D, 20.0D, 20.0D), this);
				}
				
				if(this.target!=null){
					//this.search = true;
					Vec3 vecaxe = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
					Vec3 vecent =Vec3.createVectorHelper(this.target.posX, this.target.posY+0.2D, this.target.posZ);
					Vec3 sub = vecaxe.subtract(vecent);

					double var1 = 0;
					double var2 = 0.06D;
//					if(sub.xCoord!=0){
//					
//						if(sub.xCoord<0){
//							var1 = -var2 / sub.xCoord;
//						}else{
//							var1 = var2 / sub.xCoord;
//						}
//						
//					}else{
//						var1 = 0.00000001D;
//					}
					sub.normalize();
					this.motionX += sub.xCoord * var1;
					this.motionZ += sub.zCoord * var1;
					this.motionY += sub.yCoord * var1;

					//this.motionY += this.getGravityVelocity();
//					this.motionX = this.cut(this.motionX,0.1D);
//					this.motionZ = this.cut(this.motionZ,0.1D);
//					this.motionY = this.cut(this.motionY,0.1D);

					this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.0F, 1.0F);

				}
			}
			
		}
//		if(isSkydrive && tick>3){
//			Entity nearent = this.worldObj.findNearestEntityWithinAABB(EntityGhast.class, this.boundingBox.expand(10.0D, 10.0D, 10.0D), this);
//
//			if(nearent==null){
//				nearent = this.worldObj.findNearestEntityWithinAABB(EntityMob.class, this.boundingBox.expand(10.0D, 10.0D, 10.0D), this);
//			}
//			if(nearent!=null){
//				this.search = true;
//				Vec3 vecaxe = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
//				Vec3 vecent = this.worldObj.getWorldVec3Pool().getVecFromPool(nearent.posX, nearent.posY, nearent.posZ);
//				Vec3 sub = vecaxe.subtract(vecent);
//
//				this.motionX += Math.floor(sub.xCoord) *0.2+0.02;
//				this.motionZ += Math.floor(sub.zCoord) *0.2+0.02;
//				this.motionY += Math.floor(sub.yCoord) *0.2+0.03;
//
//
//				this.motionX = this.cut(this.motionX,1.0D);
//				this.motionZ = this.cut(this.motionZ,1.0D);
//				this.motionY = this.cut(this.motionY,0.5D);
//
//
//
//			}else{
//				this.search = false;
//			}
//
//		}
	}
    
	
    protected float getGravityVelocity()
    {
    	if(this.isSkydrive){
    		return 0.0F;
    	}
        return 0.03F;
    }
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.xTile = par1NBTTagCompound.getShort("xTile");
		this.yTile = par1NBTTagCompound.getShort("yTile");
		this.zTile = par1NBTTagCompound.getShort("zTile");
		this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
		this.inData = par1NBTTagCompound.getByte("inData") & 255;
		this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
		this.tick = par1NBTTagCompound.getByte("tick") & 255;
		this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
		this.isSkydrive = par1NBTTagCompound.getBoolean("isSkyDrive");
		if (par1NBTTagCompound.hasKey("target"))
		{
			this.target = (EntityLivingBase) this.worldObj.getEntityByID(par1NBTTagCompound.getInteger("target"));
		}
		if (par1NBTTagCompound.hasKey("damage"))
		{
			this.damage = par1NBTTagCompound.getDouble("damage");
		}

		if (par1NBTTagCompound.hasKey("pickup"))
		{
			this.canBePickedUp = par1NBTTagCompound.getByte("pickup");
		}
		else if (par1NBTTagCompound.hasKey("player"))
		{
			this.canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
		}

        NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));

        ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

        if (item == null || item.stackSize <= 0)
        {
            this.setDead();
        }
	}



	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
	 */


	public void setAmp(int par1){
		this.amp = par1;
	}

	public void setDamage(double par1)
	{
		this.damage = par1;
	}

	//from EntityItem
    public void setEntityItemStack(ItemStack par1ItemStack)
    {
        this.getDataWatcher().updateObject(10, par1ItemStack);
        this.getDataWatcher().setObjectWatched(10);
    }

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public void setIsCritical(boolean par1)
	{
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
		}
	}

	public void setItem()
	{
//		System.out.println(this.getEntityItem());
//		EntityAxe axe = null;
//		if(this.getEntityItem()==null){
//			UnsagaCore.log(0, "EntityFlyingAxe has No ItemStack?");
//			axe = new EntityAxe(this.worldObj,this.posX,this.posY,this.posZ,new ItemStack(InitUnsagaTools.itemAxeSteel));
//		}else{
//			axe = new EntityAxe(this.worldObj,this.posX,this.posY,this.posZ,this.getEntityItem());
//		}
//
//		if(!this.worldObj.isRemote){
//		
//			this.worldObj.spawnEntityInWorld(axe);
//		}
		
		EntityItem var15 = new EntityItem(this.worldObj, (double)this.posX, (double)this.posY, (double)this.posZ, this.getEntityItem());
		this.entityitemaxe = var15;
		var15.delayBeforeCanPickup = 10;
		if(!this.worldObj.isRemote && var15!=null){
			this.worldObj.spawnEntityInWorld(var15);


		}


	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	public void setKnockbackStrength(int par1)
	{
		this.knockbackStrength = par1;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
	 * posY, posZ, yaw, pitch
	 */
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
	}

	public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
	{
		float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= (double)var9;
		par3 /= (double)var9;
		par5 /= (double)var9;
		par1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
		par1 *= (double)par7;
		par3 *= (double)par7;
		par5 *= (double)par7;
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
		float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var10) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}


	@SideOnly(Side.CLIENT)

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double par1, double par3, double par5)
	{
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var7) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short)this.xTile);
		par1NBTTagCompound.setShort("yTile", (short)this.yTile);
		par1NBTTagCompound.setShort("zTile", (short)this.zTile);
		par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
		par1NBTTagCompound.setByte("inData", (byte)this.inData);
		par1NBTTagCompound.setByte("shake", (byte)this.arrowShake);
		par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		par1NBTTagCompound.setByte("pickup", (byte)this.canBePickedUp);
		par1NBTTagCompound.setShort("tick", (byte)this.tick);
		par1NBTTagCompound.setDouble("damage", this.damage);
		par1NBTTagCompound.setBoolean("isSkyDrive", this.isSkydrive);
		if(this.target!=null){
			par1NBTTagCompound.setInteger("target", this.target.getEntityId());
		}

        if (this.getEntityItem() != null)
        {
        	par1NBTTagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
	}


	public boolean isSkyDrive(){
		return this.isSkydrive;
	}

	public void setTarget(EntityLivingBase target) {
		this.target = target;
		
	}
	

}
