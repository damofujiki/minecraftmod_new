package com.hinasch.unlsaga.ability.skill.effect;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.hinasch.lib.ChatHandler;
import com.hinasch.lib.HelperInventory;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.RangeDamageHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.Skill;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsaga.event.EventLivingHurt;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.item.misc.ItemSkillBook;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;

public class InvokeSkill extends AbstractInvoker{






	protected boolean failed = false;
	protected boolean prepared = false;
	protected boolean hasThrown = false;
	protected SkillBase skillEffect;
	protected SkillEffect skillEffecter;
	protected int coolingTime;
	protected Object parent;
	protected final Skill skill;

	public final ItemStack weapon;
	public final ToolCategory category;
	public EntityLivingBase target;
	public XYZPos usepoint;
	public float charge;

	public InvokeSkill(World world,EntityLivingBase skillOwner,Skill skill,ItemStack gainedWeapon){
		super(world,skillOwner);
		this.skill = skill;
		this.weapon = gainedWeapon;
		this.coolingTime = 6;

		Unsaga.debug(skill);


		if(gainedWeapon!=null && gainedWeapon.getItem() instanceof IUnsagaMaterialTool){
			IUnsagaMaterialTool iu = (IUnsagaMaterialTool)gainedWeapon.getItem();
			this.category = iu.getCategory();
		}else{
			this.category = null;
		}

		this.skillEffect = this.getSkill().getSkillEffect();
		this.skillEffect.setWorldHelper(world);


	}

	public void setPrepared(boolean par1){
		this.prepared = par1;
	}
	
	public boolean hasPrepared(){
		return this.prepared;
	}
	public void setCharge(float par1){
		this.charge = par1;
	}
	public void setParent(Object par1){
		this.parent = par1;
	}

	public void setTarget(EntityLivingBase target){
		this.target = target;
	}

	public void setUsePoint(XYZPos xyz){
		this.usepoint = xyz;
	}

	public SkillBase getEffect(){
		return this.skillEffect;
	}
	public boolean doSkill(){
		if(LivingDebuff.isCooling(owner) && this.requireCooling())return false;


		if(this.skillEffect!=null){
			
			
			if(this.skillEffect instanceof SkillMelee){
				SkillMelee skillMelee = (SkillMelee)this.skillEffect;
				if(skillMelee.isRequirePrepare()){
					if(!skillMelee.hasFinishedPrepare(this)){
						skillMelee.prepareSkill(this);
						return false;

					}

				}
			}
			


			Unsaga.debug("skilleffectet!=の那珂"+this.skillEffect);
			//this.skillEffecter.selector(this);
			this.skillEffect.invoke(this);
			if(this.owner instanceof EntityPlayer && !this.world.isRemote){

				if(SkillPanels.hasPanel(this.world, getOwnerEP(), Unsaga.skillPanels.weaponMaster)){
					int prob = 8;
					prob += (8*SkillPanels.getHighestLevelOfPanel(world, getOwnerEP(), Unsaga.skillPanels.weaponMaster)+1);
					if(this.getOwnerEP().capabilities.isCreativeMode){
						prob = 80;
					}
					if(this.getOwnerEP().getRNG().nextInt(100)<prob){
						this.masterSkill();
					}
				}

			}

		}
		if(!this.world.isRemote){
			if(!this.failed){
				this.damageWeapon();
			}
			
			if(this.hasThrown){
				this.weapon.stackSize --;
			}
		}
		
		if(this.requireCooling()){
			LivingDebuff.addDebuff(owner, Unsaga.debuffManager.cooling, this.coolingTime);
		}
		return true;
	}

	public void masterSkill(){
		HelperInventory hi = new HelperInventory(this.getOwnerEP().inventory);
		Map<Integer,ItemStack> map = hi.getInvItems(new ItemStack(Unsaga.items.skillBook), true);
		if(!map.isEmpty()){
			for(Integer num:map.keySet()){
				if(!ItemSkillBook.readSkill(hi.getInv().getStackInSlot(num)).isPresent()){
					ItemStack newStack = new ItemStack(Unsaga.items.skillBook);
					ChatHandler.sendChatToPlayer(getOwnerEP(), "message.skill.mastered");
					ItemSkillBook.writeSkill(newStack, this.skill);
					if(hi.isInvFull()){
						ItemUtil.dropItem(newStack, getOwnerEP());
					}else{
						hi.addItemStack(newStack);
					}
					hi.decrItemStack(num, 1);
					return;
				}
			}
		}

		
//		for(int i=0;i<this.getOwnerEP().inventory.getSizeInventory();i++){
//			ItemStack stack = this.getOwnerEP().inventory.getStackInSlot(i);
//			if(stack!=null && stack.getItem() instanceof ItemSkillBook){
//				if(!ItemSkillBook.readSkill(stack).isPresent()){
//					ChatHandler.sendChatToPlayer(getOwnerEP(), "message.skill.mastered");
//					ItemSkillBook.writeSkill(stack, this.skill);
//					return;
//				}
//			}
//		}
	}
	public EntityPlayer getOwnerEP(){
		if(this.owner instanceof EntityPlayer){
			return (EntityPlayer) this.owner;
		}
		return null;
	}
	
	public void setFailed(boolean par1){
		this.failed = par1;
	}
	public void setCoolingTime(int par1){
		this.coolingTime = par1;
	}
	public boolean requireCooling(){
		return Unsaga.abilityManager.requireCoolingSet.contains(this.skill);
	}

	public void damageWeapon(){
		if(this.weapon!=null){
			this.weapon.damageItem(getDamageItem(), owner);
		}

	}

	public int getDamageItem(){
		return this.skill.getDamageForWeapon();
	}

	public float getAttackDamage(){
		float modifier = 0;
		if(this.category == ToolCategory.BOW){
			if(this.weapon!=null){
				UnsagaMaterial material = HelperUnsagaItem.getMaterial(this.weapon);
				modifier += material.getBowModifier();

			}else{
				if(this.owner instanceof EntityPlayer){
					modifier += EventLivingHurt.getModifiedPunchDamage(getOwnerEP());
				}else{
					modifier += this.owner.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
				}
				
			}
			modifier += this.skill.getStrHurtHP();
		}
		modifier += LivingDebuff.getModifierAttackBuff(owner);
		float base = 0;

		return modifier + base + this.skill.getStrHurtHP();
	}



	public float getAttackDamageLP(){
		return this.skill.getStrHurtLP();
	}



	public DamageHelper.Type getDamageType(){
		return this.skill.damageType;
	}



	public Skill getSkill(){
		return this.skill;
	}
	public void causeRangeDamage(RangeDamageHelper parent,AxisAlignedBB bb,float damage,DamageSource ds,boolean isEnemyOnly){
		RangeDamageHelper.causeDamage(world,parent, bb,ds, damage);
	}

	public DamageSourceUnsaga getDamageSource(){
		DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.owner,this.getAttackDamageLP(),this.getDamageType());
		return ds;

	}

	public float getModifiedAttackDamage(){
		return this.getModifiedAttackDamage(false, 0);
	}
	public float getModifiedAttackDamage(boolean ischarge,float charge){
		float f = 0;
		if(ischarge){
			charge = MathHelper.clamp_float(charge, 0, 15);
			f = charge/15.0F;

		}
		float baseStr = (float)this.owner.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		float apliedStr;
		if(ischarge){
			apliedStr = baseStr  + (baseStr * ((this.getAttackDamage()*f)/30.0F));
		}else{
			apliedStr = baseStr  + (baseStr * (this.getAttackDamage()/30.0F));
		}

		return apliedStr;
	}
	public void attack(Entity living,Entity projectile){

		if(living instanceof EntityLivingBase){
			living.attackEntityFrom(this.getDamageSource(), this.getModifiedAttackDamage(false,0));

		}
	}

	public void attack(Entity living,Entity projectile,float charge){

		if(living instanceof EntityLivingBase){
			living.attackEntityFrom(this.getDamageSource(), this.getModifiedAttackDamage(true,charge));

		}

		//Unsaga.debug("modifier STR:"+this.getModifiedAttackDamage());
		//		float at = attack(this.skill.damageType,this.getAttackDamage(),living);
		//		Unsaga.debug("Owner:"+owner.getCommandSenderName()+" Target:"+living.getCommandSenderName()+" Damage:"+at);
		//		doAttack(owner, living, projectile, at, charge);
	}

	public static float attack(DamageHelper.Type type,float attackdamage,Entity living){
		float at = attackdamage;
		if(type==DamageHelper.Type.PUNCH){
			if(living instanceof EntitySkeleton) at += 3;
			if(living instanceof EntitySlime) at = 0;
			if(living instanceof EntityTreasureSlime) at = 0;
		}
		if(type==DamageHelper.Type.SPEAR){
			if(living instanceof EntitySkeleton) at -= 3;
		}
		if(type==DamageHelper.Type.SWORDPUNCH){
			if(living instanceof EntitySkeleton) at += 1;
			if(living instanceof EntitySlime) at -= 3;
			if(living instanceof EntityTreasureSlime) at -= 3;
		}



		at = MathHelper.clamp_float(at, 0, 100);
		return at;
	}

//	public static void doAttack(EntityLivingBase attacker,Entity living,Entity projectile,float at,float attackLP){
//		if(projectile!=null){
//			living.attackEntityFrom(DamageSource.causeThrownDamage(attacker, projectile), at);
//			if(living instanceof EntityLivingBase){
//				Unsaga.lpHandler.tryHurtLP((EntityLivingBase) living, (int)attackLP);
//			}
//
//			
//			return;
//		}
//
//		if(attacker instanceof EntityPlayer){
//			living.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), at);
//		}else{
//			living.attackEntityFrom(DamageSource.causeMobDamage(attacker), at);
//		}
//
//		if(living instanceof EntityLivingBase){
//			Unsaga.lpHandler.tryHurtLP((EntityLivingBase) living, (int) attackLP);
//		}
//		return;
//	}

	public void prepareSkill(){
		if(this.skillEffect instanceof SkillMelee){
			((SkillMelee) this.skillEffect).prepareSkill(this);
		}
	}
	
	public void setWeaponThrown(){
		this.hasThrown = true;
	}
}
