package com.hinasch.unlsaga.entity.projectile;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

import cpw.mods.fml.common.FMLLog;

public class EntityFlyingAxeNew extends EntityThrowable implements IProjectile{

	protected float damage = 2.0F;
	protected float drive = 1.0F;
	//protected boolean isSkyDrive = false;
	protected ItemStack itemStackAxe;
	protected int knockbackModifier = 0;
	protected float lpDamage = 0;
	protected float rotation = 0;
	protected Entity target;
	protected int tickAxe = 0;
	
	protected final int DRIVE = 20;
	protected final int TARGETID = 22;
	protected final int SKYDRIVE_FLAG = 23;
	
	protected final int STATUS_DEAD = 4;
	
	public EntityFlyingAxeNew(World par1World) {
		super(par1World);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public EntityFlyingAxeNew(World par1World, EntityLivingBase par2EntityLiving, float par3,ItemStack par4,boolean par5)
	{
		super(par1World,par2EntityLiving);
		this.setDrive(par3);
		this.setAxeItemStack(par4);
		this.setSkyDrive(par5);

	}
	


	
	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
		this.getDataWatcher().addObject(SKYDRIVE_FLAG, (byte)0);
		this.getDataWatcher().updateObject(SKYDRIVE_FLAG, (byte)0);
		this.getDataWatcher().addObjectByDataType(10, 5);
		this.getDataWatcher().addObjectByDataType(DRIVE, 3);
		this.getDataWatcher().addObject(TARGETID, (int)-1);
		this.getDataWatcher().updateObject(TARGETID, -1);
		this.getDataWatcher().updateObject(DRIVE, (Float)1.0F);
	}
	
	protected void setSkyDrive(boolean par1){
		this.getDataWatcher().updateObject(SKYDRIVE_FLAG,(byte)(par1 ? 1 : 0));
		this.getDataWatcher().setObjectWatched(SKYDRIVE_FLAG);
	}
	
	protected void setSkyDriveAttack(boolean par1){
		this.getDataWatcher().updateObject(SKYDRIVE_FLAG,(byte)(par1 ? 2 : 1));
		this.getDataWatcher().setObjectWatched(SKYDRIVE_FLAG);
	}
	
	protected void updateSkyDriveStatus(int par1){
		this.getDataWatcher().updateObject(SKYDRIVE_FLAG, (byte)par1);
		this.getDataWatcher().setObjectWatched(SKYDRIVE_FLAG);
	}
	
	protected int getSkyDriveStatus(){
		return (int)this.getDataWatcher().getWatchableObjectByte(SKYDRIVE_FLAG);
	}
	
	protected boolean isSkyDrive(){
		return this.getDataWatcher().getWatchableObjectByte(SKYDRIVE_FLAG) > 0 ? true : false;
	}
	
	protected boolean hasSkyDriveLockOn(){
		return this.getDataWatcher().getWatchableObjectByte(SKYDRIVE_FLAG) == 2 ? true : false;
	}
	
	protected void setDrive(float par1){
		this.getDataWatcher().updateObject(DRIVE, (Float)par1);
		this.getDataWatcher().setObjectWatched(DRIVE);
	}
	
	protected float getDrive(){
		float f = this.getDataWatcher().getWatchableObjectFloat(DRIVE);
		return f;
	}
    
	@Override
    protected float func_70182_d()
    {
        return this.getDrive() * 1.5F;
    }
	
	public ItemStack getAxeItemStack()
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

	public float getDamage(){
		return this.damage;
	}
	
	@Override
    protected float getGravityVelocity()
    {
		if(this.isSkyDrive()){
			return 0.0F;
		}
        return 0.03F;
    }
	
	public int getKnockbackModifier(){
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, this.getAxeItemStack());
		
	}
	
    public float getLPDamage(){
		return this.lpDamage;
	}
    
	public float getRotation() {
		// TODO 自動生成されたメソッド・スタブ
		return this.rotation;
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		
		this.updateSkyDriveStatus(STATUS_DEAD);
		
		if(mop!=null){
			Unsaga.debug("hit!side:"+this.worldObj.isRemote);
			this.playSound("mob.irongolem.hit", 1.5F,1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.worldObj.spawnParticle("largeexplode", this.motionX,this.motionY,this.motionZ, 1.0D, 0.0D, 0.0D);

			
			if(mop.typeOfHit==MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit!=null){
				DamageSource ds = new DamageSourceUnsaga(null, this.getThrower(), this.getLPDamage(),DamageHelper.Type.SWORDPUNCH,this);
				mop.entityHit.attackEntityFrom(ds, this.getDamage());
				if(mop.entityHit instanceof EntityLivingBase){
					if(this.getKnockbackModifier()>0){
						mop.entityHit.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)this.getKnockbackModifier() * 0.5F), 0.1D, 
								(double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)this.getKnockbackModifier() * 0.5F));
					}


				}
				if(this.hasFire()){
					mop.entityHit.setFire(5);
				}
			}
			
		}
		
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		this.rotation += 50.0F;
		this.rotation = MathHelper.wrapAngleTo180_float(this.rotation);
		
		this.tickAxe += 1;
		Unsaga.debug("flyingAxe:"+new XYZPos(motionX,motionY,motionZ)+" side;"+this.worldObj.isRemote+" tick:"+this.tickAxe+" status:"+this.getSkyDriveStatus());
		if(this.tickAxe > 100){
			this.transformToEntityItem();
		}
		if(this.isSkyDrive()){
			this.onUpdateSkyDrive();
		}
	}
	

	private void onUpdateSkyDrive() {
		if(this.getSkyDriveStatus()==STATUS_DEAD){
			this.transformToEntityItem();
		}
		if(this.getSkyDriveStatus()<2){
			if(this.tickAxe<30){
				this.motionX = 0;
				this.motionY = 0;
				this.motionZ = 0;
			}
			
			if(this.tickAxe==30){
				this.updateSkyDriveStatus(2);
			}
		}

		if(this.getSkyDriveStatus()==2){
			
			

			if(this.getTarget()==null){
				Entity target = (EntityLivingBase) this.worldObj.findNearestEntityWithinAABB(IMob.class, this.boundingBox.expand(50.0D, 50.0D, 50.0D), this);
				if(target!=null){
					this.setTarget(target);
				}
			}
			Unsaga.debug("side:"+this.worldObj.isRemote+" entity:"+this.getTarget());
			if(this.getTarget()!=null){
				this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
				Entity target = this.getTarget();
				Vec3 vecaxe = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
				Vec3 vecent = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
				Vec3 sub = vecaxe.subtract(vecent);
				sub.normalize();
				double d1 = 0.45D;
				this.setVelocity(sub.xCoord*d1, sub.yCoord*d1, sub.zCoord*d1);

				//this.setVelocity(motionX, motionY, motionZ);
				this.updateSkyDriveStatus(3);
			}else{
				this.transformToEntityItem();
			}

		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttag)
	{

		super.readEntityFromNBT(nbttag);
		this.setDamage(nbttag.getFloat("damage.HP"));
		this.setLPDamage(nbttag.getFloat("damage.LP"));
		this.tickAxe = nbttag.getInteger("tickAxe");
		this.setDrive(nbttag.getFloat("drive"));
		//this.knockbackModifier = par1NBTTagCompound.getInteger("knockbackModifier");
		this.updateSkyDriveStatus(nbttag.getInteger("statusSkyDrive"));

        NBTTagCompound nbttagcompound1 = nbttag.getCompoundTag("Item");
        this.setAxeItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));

        ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

        if (item == null || item.stackSize <= 0)
        {
            this.setDead();
        }
	}
	
	public boolean hasFire(){
		Map enchantmap = EnchantmentHelper.getEnchantments(getAxeItemStack());
		return enchantmap.containsKey(Enchantment.fireAspect.effectId);
	}
	
	
	public void setAxeItemStack(ItemStack par1ItemStack)
    {
        this.getDataWatcher().updateObject(10, par1ItemStack);
        this.getDataWatcher().setObjectWatched(10);
    }
	
	public void setDamage(float par1){
		this.damage = par1;
	}
	
//	public void setKnockbackModifier(int par1){
//		this.knockbackModifier = par1;
//	}
	
	public void setLPDamage(float par1){
		this.lpDamage = par1;
	}
	
	public void setTarget(Entity par1){
		//this.target = par1;
		this.getDataWatcher().updateObject(TARGETID, (int)par1.getEntityId());
		this.getDataWatcher().setObjectWatched(TARGETID);
	}

	public Entity getTarget(){
		int entityid = this.getDataWatcher().getWatchableObjectInt(TARGETID);
		Entity tar = this.worldObj.getEntityByID(entityid);
		return tar;
	}

	//アイテムエンティティと化す
	protected void transformToEntityItem()
	{
		Unsaga.debug("Dead side:"+this.worldObj.isRemote);
		EntityItem var15 = new EntityItem(this.worldObj, (double)this.posX, (double)this.posY, (double)this.posZ, this.getAxeItemStack());
		var15.delayBeforeCanPickup = 10;
		if(!this.worldObj.isRemote && var15!=null){
			this.worldObj.spawnEntityInWorld(var15);


		}
		this.setDead();

	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setFloat("damage.HP", this.getDamage());
		par1NBTTagCompound.setFloat("damage.LP", this.getLPDamage());
		par1NBTTagCompound.setInteger("tickAxe", this.tickAxe);
		par1NBTTagCompound.setFloat("drive",this.getDrive());
		//par1NBTTagCompound.setInteger("knockbackModifier", this.getKnockbackModifier());
		par1NBTTagCompound.setInteger("statusSkyDrive", this.getSkyDriveStatus());

        if (this.getAxeItemStack() != null)
        {
        	par1NBTTagCompound.setTag("Item", this.getAxeItemStack().writeToNBT(new NBTTagCompound()));
        }
	}
}
