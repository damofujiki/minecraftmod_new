package com.hinasch.unlsaga.util.damage;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import com.google.common.collect.Lists;
import com.hinasch.unlsaga.util.FiveElements.Enums;

public class DamageSourceUnsaga extends EntityDamageSource{

	protected float strLPHurt;
	public EnumSet<DamageHelper.SubType> subTypeSet;
	public DamageHelper.Type damageType;
	public Enums element;

	public String str;
	protected Entity sourceOfDamage;
	
	public DamageSourceUnsaga(String damageType, Entity par2Entity) {
		super(DamageHelper.getMobOrPlayerString(damageType,par2Entity), par2Entity);

	}
	
	public DamageSourceUnsaga(String damageType, Entity par2Entity,float lpHurt,DamageHelper.Type type) {
		this(damageType, par2Entity);

		this.strLPHurt = lpHurt;
		this.damageType = type;
	}
	
	public DamageSourceUnsaga(String damageType, Entity par2Entity,float lpHurt,DamageHelper.Type type,Entity projectile) {
		this(damageType, par2Entity,lpHurt,type);

		this.sourceOfDamage = projectile;
		this.setProjectile();
	}
	
	public DamageSourceUnsaga(DamageSource ds){
		super(ds.damageType,ds.getEntity());

		this.expandToUnsagaDamage(ds);

	}

	public void expandToUnsagaDamage(DamageSource ds){
		if(ds.isFireDamage()){
			this.setSubDamageType(DamageHelper.SubType.FIRE);
		}
		if(ds.isMagicDamage()){
			this.setMagicDamage();
		}
		if(ds.getSourceOfDamage()!=null){
			this.sourceOfDamage = ds.getSourceOfDamage();
		}
		if(ds.isProjectile()){
			this.setProjectile();
		}
		if(ds.getEntity() instanceof EntityBlaze|| ds.getEntity() instanceof EntityMagmaCube){
			this.setSubDamageType(DamageHelper.SubType.FIRE);
		}
		if(ds==DamageSource.cactus){
			this.setUnsagaDamageType(DamageHelper.Type.SPEAR);;
		}
		if(ds.getSourceOfDamage() instanceof EntityArrow){
			this.setUnsagaDamageType(DamageHelper.Type.SPEAR);
		}
		if(ds.isExplosion()){
			this.setExplosion();
			this.setUnsagaDamageType(DamageHelper.Type.MAGIC);
			
		}


		//Unsaga.debug(ds.getEntity(),this.getClass());
		if(ds.getEntity() instanceof EntityLivingBase){
			this.figureEntityWeapon(ds);
		}
	}
	

	public void figureEntityWeapon(DamageSource ds){
		EntityLivingBase living = (EntityLivingBase)ds.getEntity();
		if(living.getHeldItem()==null){
			//素手
			this.setUnsagaDamageType(DamageHelper.Type.PUNCH);
			return;
		}
		if(living.getHeldItem()!=null){
			Item held = living.getHeldItem().getItem();
			if(held instanceof ItemSword){
				this.setUnsagaDamageType(DamageHelper.Type.SWORD);
				return;
			}
			if(held instanceof ItemAxe){
				this.setUnsagaDamageType(DamageHelper.Type.SWORDPUNCH);
				return;
			}
			//不明な時はとりあえず剣ダメージ
			this.setUnsagaDamageType(DamageHelper.Type.SWORD);
		}
	}
	@Override
	public Entity getSourceOfDamage(){
		return this.sourceOfDamage;
	}
	
	public float getStrengthLPHurt(){
		return this.strLPHurt;
		
	}
	
	public void setStrengthLPHurt(float par1){
		this.strLPHurt = par1;
	}
	
	public void setElement(Enums par1){
		this.element = par1;
	}
	
	public void setUnsagaDamageType(DamageHelper.Type type){
		this.damageType = type;
	}
	
	public DamageHelper.Type getUnsagaDamageType(){
		if(this.damageType!=null){
			return this.damageType;
		}
		//とりあえず
		return DamageHelper.Type.MAGIC;
	}
	

	public DamageSourceUnsaga setSubDamageType(DamageHelper.SubType... subtypes){
		EnumSet<DamageHelper.SubType> rt = EnumSet.copyOf(Lists.newArrayList(subtypes));
		this.subTypeSet = rt;
		return this;
	}
	public EnumSet<DamageHelper.SubType> getSubDamageType(){
		if(this.subTypeSet==null || this.subTypeSet.isEmpty()){
			return EnumSet.of(DamageHelper.SubType.NONE);
		}
		return this.subTypeSet;
	}
	public Enums getElement(){
		return this.element;
		
	}
	

}
