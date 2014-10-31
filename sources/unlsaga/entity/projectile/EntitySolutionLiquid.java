package com.hinasch.unlsaga.entity.projectile;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.hinasch.lib.HSLibs;
import com.hinasch.lib.PairID;
import com.hinasch.lib.RangeDamage;
import com.hinasch.lib.RangeDamageHelper;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.network.packet.PacketClientThunder;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;
import com.hinasch.unlsagamagic.spell.Spells;
import com.hinasch.unlsagamagic.spell.effect.ScannerElectricShock;

public class EntitySolutionLiquid extends EntityThrowable implements IProjectile{

	protected float damageHP;
	protected float damageLP;
	protected boolean isThunderCrap = false;
	
	public EntitySolutionLiquid(World par1World) {
		super(par1World);
		this.damageHP = 1.0F;
		this.damageLP = 0.1F;
		// TODO 自動生成されたコンストラクター・スタブ
	}

    public EntitySolutionLiquid(World par1World, EntityLivingBase par2EntityLivingBase)
    {
    	super(par1World,par2EntityLivingBase);
		this.damageHP = 1.0F;
		this.damageLP = 0.1F;
    }
	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if(this.isThunderCrap){
			this.doThunderCrap(mop);
		}else{
			if(mop.typeOfHit==MovingObjectPosition.MovingObjectType.BLOCK){
				this.setDead();
			}
			if(mop.entityHit!=null){
				Entity hitEntity = mop.entityHit;
				if(hitEntity!=this.getThrower()){
					if(hitEntity instanceof EntityLivingBase){
						EntityLivingBase living = (EntityLivingBase)hitEntity;
						living.attackEntityFrom(new DamageSourceUnsaga(null,this.getThrower(),damageLP,DamageHelper.Type.MAGIC), damageHP);
					}
				}
			}
		}

		
	}

	private void doThunderCrap(MovingObjectPosition mop) {
		Spells spells = Unsaga.magic.spellManager;
		if(mop.typeOfHit==MovingObjectPosition.MovingObjectType.BLOCK || mop.typeOfHit==MovingObjectPosition.MovingObjectType.ENTITY){
			DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.getThrower(),spells.thunderCrap.getStrHurtLP(),DamageHelper.Type.MAGIC,this);
			ds.setSubDamageType(DamageHelper.SubType.ELECTRIC);
			WorldHelper helper = new WorldHelper(this.worldObj);
			AxisAlignedBB bb = HSLibs.getBounding(XYZPos.entityPosToXYZ(this), 2, 1);
			RangeDamage rd = new RangeDamage(worldObj);
			rd.causeDamage(ds, bb, spells.thunderCrap.getStrHurtLP());


			if(!this.worldObj.isRemote){
			

	            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
				PacketClientThunder pt = new PacketClientThunder(XYZPos.entityPosToXYZ(this));
				Unsaga.packetDispatcher.sendToAllAround(pt, PacketUtil.getTargetPointNear(this));
				//液体のキャッチが不安定なのでもうちょっと範囲広げる
				if(helper.findNearMaterial(this.worldObj,Material.water, XYZPos.entityPosToXYZ(this), 15)!=null){
					
					XYZPos pos = helper.findNearMaterial(worldObj,Material.water, XYZPos.entityPosToXYZ(this), 15);
					bb = HSLibs.getBounding(pos, 2, 1);
					RangeDamage rd2 = new RangeDamage(worldObj);
					rd2.causeDamage(ds, bb, spells.thunderCrap.getStrHurtHP());
					RangeDamageHelper.causeDamage(worldObj, null, bb, ds, spells.thunderCrap.getStrHurtHP());
//					Unsaga.debug("液体発見");
					PairID compare = new PairID(Blocks.water,0).setCheckMetadata(false);
					compare.sameBlocks.add(new PairID(Blocks.flowing_water,0).setCheckMetadata(false));
					
					
					ScannerElectricShock shocker = new ScannerElectricShock(worldObj,10,compare,pos,this.getThrower());
					Unsaga.scannerPool.addEvent(shocker);
				}
				
				
			}
				this.setDead();
		}
		
	}
	


	public void setDamage(float hp,float lp){
		this.damageHP = hp;
		this.damageLP = lp;
	}
	
	public void setThunderCrap(){
		this.isThunderCrap = true;
	}
}
