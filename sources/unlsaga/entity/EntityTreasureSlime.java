package com.hinasch.unlsaga.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import org.apache.commons.lang3.tuple.Pair;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.entity.ai.AISelector;
import com.hinasch.unlsaga.entity.ai.EntityAISlime;
import com.hinasch.unlsaga.entity.ai.EntityAISlime.ISlimeAI;
import com.hinasch.unlsaga.entity.ai.EntityAISpell;
import com.hinasch.unlsaga.entity.ai.EntityAISpell.ISpellAI;
import com.hinasch.unlsaga.entity.ai.EntityAISpell.SpellAIData;
import com.hinasch.unlsaga.entity.projectile.EntityFireArrowNew;
import com.hinasch.unlsaga.entity.projectile.EntitySolutionLiquid;

public class EntityTreasureSlime extends EntityMob implements IRangedAttackMob,ISlimeAI,ISpellAI
{
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;

    public AISelector aiSelector;

    public final List<Pair<String,Double>> projectileList;
    public List<Pair<String,Double>> rangedList;
    
    public final List<SpellAIData> spellList;
    public final String FIREBALL = "fireball";
    public final String LIQUID = "liquid";
    
    //public final EntityAIArrowTracking aiarrow;
    
    public int attackcounter;
    
    public int slimeLevel = -1;
    /** the time between each jump of the slime */
    private int slimeJumpDelay;
    private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
    public EntityTreasureSlime(World par1World)
    {
        super(par1World);
        
       // this.aiarrow = new EntityAIArrowTracking(this,1.0D,60,10.0F);
        this.rangedList = new ArrayList();
        this.spellList = new ArrayList();

        int i = 1 << this.rand.nextInt(3);
        this.yOffset = 0.0F;
        this.setAIMoveSpeed(0.1F);
        this.slimeJumpDelay = this.rand.nextInt(20) + 10;
        this.setSlimeSize(i);
        this.attackcounter = 0;
        this.projectileList = new ArrayList();
        this.tasks.addTask(1, new EntityAISlime(this));
//        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
       // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        //movespeed,時間,範囲
        this.targetTasks.addTask(1, new EntityAIArrowAttack(this,1.0D,60,15.0F));
        this.targetTasks.addTask(2, new EntityAISpell(this,this.spellList,1.0D,200,15.0F));

        if(this.slimeLevel==-1){
        	this.slimeLevel = this.rand.nextInt(99)+1;
        }

    	this.spellList.add(new SpellAIData(Unsaga.magic.spellManager.purify,120.0D,0.0D));	
        if(this.slimeLevel>25){
        	this.projectileList.add(Pair.of(FIREBALL, 28.0D));
        	this.spellList.add(new SpellAIData(Unsaga.magic.spellManager.fireVeil,120.0D,0.0D));
        	this.spellList.add(new SpellAIData(Unsaga.magic.spellManager.lifeBoost,120.0D,0.0D));

        }
        this.projectileList.add(Pair.of(LIQUID, 10.0D));

        if(this.slimeLevel>45){
            this.spellList.add(new SpellAIData(Unsaga.magic.spellManager.callThunder,120.0D,10.0D));
        	this.spellList.add(new SpellAIData(Unsaga.magic.spellManager.abyss,120.0D,10.0D));

        }
       
        this.experienceValue = 8;


        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Slime Level Scaled HP",this.slimeLevel/3,0));
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifier("Slime Level Scaled Attack Damage",this.slimeLevel/5,0));
        Unsaga.debug(this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue(),this.getClass());
        Unsaga.debug(this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue(),this.getClass());
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
    }
    
    public EntityTreasureSlime(World par1World,int chestlevel)
    {
    	this(par1World);
    	this.slimeLevel = chestlevel;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)1));
    }


    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(7.0F);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
       }
    
    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }
    
    protected void setSlimeSize(int par1)
    {
        this.dataWatcher.updateObject(16, new Byte((byte)par1));
        this.setSize(0.6F * (float)par1, 0.6F * (float)par1);
        this.setPosition(this.posX, this.posY, this.posZ);
        //this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute((double)(par1 * par1));
        this.setHealth(this.getTreasureSlimeMaxHealth());
        this.experienceValue = par1;
    }

    private float getTreasureSlimeMaxHealth() {
		float maxhealth = 7.0F + (this.slimeLevel / 3) + 1.0F;
		return maxhealth;
	}

	/**
     * Returns the size of the slime.
     */
    public int getSlimeSize()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    

    @Override
    public boolean canInvokeSpell(){
    	if(LivingDebuff.hasDebuff(this, Unsaga.debuffManager.lockSlime)){
    		return false;
    	}
    	return true;
    }
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Size", this.getSlimeSize() - 1);
        par1NBTTagCompound.setInteger("SlimeLevel", this.slimeLevel);
        par1NBTTagCompound.setInteger("attackCounter", this.attackcounter);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setSlimeSize(par1NBTTagCompound.getInteger("Size") + 1);
        this.slimeLevel = par1NBTTagCompound.getInteger("SlimeLevel");
        this.attackcounter = par1NBTTagCompound.getInteger("attackCounter");
    }

    /**
     * Returns the name of a particle effect that may be randomly created by EntitySlime.onUpdate()
     */
    protected String getSlimeParticle()
    {
        return "slime";
    }

    public List<Pair<String,Double>> getRangedList(double range){
    	List<Pair<String,Double>> ranged = new ArrayList();
    	for(Pair<String,Double> p:this.projectileList){
    		if(p.getRight()>=range){
    			ranged.add(p);
    		}
    	}
    	//Unsaga.debug("Range",range,"list",ranged);
    	return ranged;
    }
    /**
     * Returns the name of the sound played when the slime jumps.
     */
    public String getJumpSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	if(!LivingDebuff.hasDebuff(this, Unsaga.debuffManager.lockSlime)){
    		//this.aiSelector.updateAITick();
    	}
    //	Unsaga.logger.log("jump",this.isJumping,"Y",this.motionY,this.getClass().getName());
//        this.attackcounter += 1;
//        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
//        //Unsaga.debug(this.attackTime);
//        if(this.attackcounter>100 && !LivingDebuff.hasDebuff(this, DebuffRegistry.lockSlime)){
//        	if(entityplayer!=null){
//        		this.attackEntityWithRangedAttack(entityplayer, 0.5F);
//        	}
//        	
//        	this.attackcounter= 0;
//        }
        

        
        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.EASY && this.getSlimeSize() > 0)
        {
            this.isDead = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        boolean flag = this.onGround;
        super.onUpdate();
        int i;


        if (this.onGround && !flag)
        {
            i = this.getSlimeSize();

            for (int j = 0; j < i * 8; ++j)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                this.worldObj.spawnParticle(this.getSlimeParticle(), this.posX + (double)f2, this.boundingBox.minY, this.posZ + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            if (this.makesSoundOnLand())
            {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.squishAmount = -0.5F;
        }
        else if (!this.onGround && flag)
        {
            this.squishAmount = 1.0F;
        }

        this.alterSquishAmount();

        if (this.worldObj.isRemote)
        {
            i = this.getSlimeSize();
            this.setSize(0.6F * (float)i, 0.6F * (float)i);
        }
    }

    public int getSlimeJumpDelay(){
    	return this.slimeJumpDelay;
    }
    
    public void setSlimeJumpDelay(int par1){
    	this.slimeJumpDelay = par1;
    }
    public void updateEntityActionState()
    {
        this.despawnEntity();
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);


        if (entityplayer != null)
        {
            this.faceEntity(entityplayer, 10.0F, 20.0F);
        }

        if (this.onGround && this.slimeJumpDelay-- <= 0)
        {
            this.slimeJumpDelay = this.getJumpDelay();

            if (entityplayer != null)
            {
                this.slimeJumpDelay /= 3;
            }

            this.isJumping = true;

            if (this.makesSoundOnJump())
            {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
            this.moveForward = (float)(1 * this.getSlimeSize());
        }
        else
        {
            this.isJumping = false;

            if (this.onGround)
            {
                this.moveStrafing = this.moveForward = 0.0F;
            }
        }
    }

    protected void alterSquishAmount()
    {
        this.squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    public int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

//    protected EntityTreasureSlime createInstance()
//    {
//        return new EntityTreasureSlime(this.worldObj,this.slimeLevel);
//    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
//        int i = this.getSlimeSize();
//
//        if (!this.worldObj.isRemote && i > 1 && this.getHealth() <= 0.0F)
//        {
//            int j = 2 + this.rand.nextInt(3);
//
//            for (int k = 0; k < j; ++k)
//            {
//                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
//                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
//                EntityTreasureSlime entityslime = this.createInstance();
//                entityslime.setSlimeSize(i / 2);
//                entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
//                this.worldObj.spawnEntityInWorld(entityslime);
//            }
//        }

        super.setDead();
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (this.canDamagePlayer())
        {
            int i = this.getSlimeSize();

            if (this.canEntityBeSeen(par1EntityPlayer) && this.getDistanceSqToEntity(par1EntityPlayer) < 0.6D * (double)i * 0.6D * (double)i && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getTreasureSlimeStrength()))
            {
                this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    private float getTreasureSlimeStrength() {
		float modifier = 1 + (this.slimeLevel /5);
		return modifier;
	}

	/**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return this.getSlimeSize() > 1;
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength()
    {
        return this.getSlimeSize();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }


    @Override
    protected Item getDropItem()
    {
        return this.getSlimeSize() == 1 ? Items.slime_ball: null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere()
    {
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));

        if (this.worldObj.getWorldInfo().getTerrainType().handleSlimeSpawnReduction(rand, worldObj))
        {
            return false;
        }
        else
        {
            if (this.getSlimeSize() == 1 || this.worldObj.difficultySetting == EnumDifficulty.EASY)
            {
                BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));

                if (biomegenbase == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getBlockLightValue(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) <= this.rand.nextInt(8))
                {
                    return super.getCanSpawnHere();
                }

                if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
                {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    public float getSoundVolume()
    {
        return 0.4F * (float)this.getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    public boolean makesSoundOnJump()
    {
        return this.getSlimeSize() > 0;
    }

    /**
     * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
     */
    protected boolean makesSoundOnLand()
    {
        return this.getSlimeSize() > 2;
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target,
			float f) {
		if(LivingDebuff.hasDebuff(this, Unsaga.debuffManager.lockSlime)){
			return;
		}
		Entity throwable = null;
		float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		String sl = "";
		this.rangedList = this.getRangedList(this.getDistanceToEntity(target));
		if(this.rangedList.isEmpty()){
			return;
		}
		
		if(this.rangedList.size()<=1){
			sl = this.LIQUID;
		}else{
			sl = this.rangedList.get(this.rand.nextInt(this.rangedList.size())).getLeft();
		}
		
		if(sl.equals(FIREBALL)){
	        EntityFireArrowNew entityFireArrow = new EntityFireArrowNew(this.worldObj, this, target, 1.5F,1.0F);
	        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
	        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
	        entityFireArrow.setDamage((float) ((f * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F)));

	        if (i > 0)
	        {
	        	entityFireArrow.setDamage((float) (entityFireArrow.getDamage() + (double)i * 0.5D + 0.5D));
	        }

	        if (j > 0)
	        {
	        	entityFireArrow.setKnockBackModifier((int) f);
	        }
	        throwable = entityFireArrow;
	        this.playSound("mob.ghast.fireball", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		}
		if(sl.equals(LIQUID)){
			EntitySolutionLiquid liquid = new EntitySolutionLiquid(this.worldObj,this);
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.3F);
			liquid.setDamage(damage*0.6F, 0);
			throwable = liquid;
		}


		if(throwable!=null){

	        if(!this.worldObj.isRemote){
	        	this.worldObj.spawnEntityInWorld(throwable);
	        }
		}

        
		
	}


	@Override
	public void despawn() {
		this.despawnEntity();
	}

//	@Override
//	public Entity getNewThrowableInstance(AIThrowProjectile parent,EntityLivingBase target,float f) {
//
//		Entity throwable = null;
//		float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
//		if(parent.getName().equals("firearrow")){
//	        EntityFireArrowNew entityFireArrow = new EntityFireArrowNew(this.worldObj, this, target, 1.5F,1.0F);
//	        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
//	        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
//	        entityFireArrow.setDamage((float) ((f * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F)));
//
//	        if (i > 0)
//	        {
//	        	entityFireArrow.setDamage((float) (entityFireArrow.getDamage() + (double)i * 0.5D + 0.5D));
//	        }
//
//	        if (j > 0)
//	        {
//	        	entityFireArrow.setKnockBackModifier((int) f);
//	        }
//	        throwable = entityFireArrow;
//	        this.playSound("mob.ghast.fireball", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
//		}
//		if(parent.getName().equals("liquid")){
//			EntitySolutionLiquid liquid = new EntitySolutionLiquid(this.worldObj,this);
//			liquid.setDamage(damage*0.6F, 0);
//			throwable = liquid;
//		}
//
//		return throwable;
//	}
    


}
