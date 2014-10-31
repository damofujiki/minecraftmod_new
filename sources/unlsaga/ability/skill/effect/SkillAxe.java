package com.hinasch.unlsaga.ability.skill.effect;

import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Sets;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.PairID;
import com.hinasch.lib.ScanHelper;
import com.hinasch.lib.SoundAndSFX;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingState;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateFlyingAxe;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateTarget;
import com.hinasch.unlsaga.entity.projectile.EntityFlyingAxeNew;
import com.hinasch.unlsaga.item.weapon.ItemAxeUnsaga;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.network.packet.PacketSkill;
import com.hinasch.unlsaga.util.LockOnHelper;
import com.hinasch.unlsaga.util.rangedattack.RangeDamageUnsaga;

public class SkillAxe extends SkillEffect{

	public WorldHelper worldHelper;
	public static SkillAxe INSTANCE;

	public static SkillAxe getInstance(){
		if(INSTANCE==null){
			INSTANCE = new SkillAxe();
		}
		return INSTANCE;
	}


	public final SkillMelee fujiView = new SkillFujiView(SkillMelee.Type.ENTITY_LEFTCLICK);
	public final SkillMelee skyDrive = new SkillSkyDrive(SkillMelee.Type.RIGHTCLICK).setRequirePrepare(true).setRequireSneak(false);
	public final SkillMelee woodBreaker = new SkillWoodBreaker(SkillMelee.Type.USE).setRequirePrepare(true);
	public final SkillMelee woodChopper = new SkillWoodChopper(SkillMelee.Type.USE);


	public class SkillWoodChopper extends SkillMelee{

		public SkillWoodChopper(Type type) {
			super(type);

		}



		@Override
		public void invokeSkill(InvokeSkill parent) {
			parent.owner.swingItem();
			EntityPlayer ep = parent.getOwnerEP();
			XYZPos po = parent.usepoint;
			int amount = 0;
			int fortune = EnchantmentHelper.getFortuneModifier(ep);
			this.playShootSound(ep);
			PairID blockdata = worldHelper.getBlockDatas(po);

			if(this.worldHelper.getMaterial(po)==Material.wood){
				this.breakWood(parent, blockdata, po);
			}

			return;

		}

		private void breakWood(InvokeSkill parent,PairID blockwooddata,XYZPos pos){
			Block block = blockwooddata.getBlockObject();
			SoundAndSFX.playBlockBreakSFX(parent.world, pos, blockwooddata);
			XYZPos upPos = pos.add(worldHelper.UP);
			PairID thisblock = new PairID(worldHelper.getBlock(upPos),worldHelper.getBlockMetadata(upPos));
			if(blockwooddata.equalsPair(thisblock)){
				this.breakWood(parent,blockwooddata,upPos);
				parent.weapon.damageItem(1, parent.owner);
			}
			return;

		}

	}


	public class SkillFujiView extends SkillMelee{

		Set<Block> blockSet = Sets.newHashSet(Blocks.sandstone,Blocks.gravel,Blocks.grass,Blocks.dirt,
				Blocks.cobblestone,Blocks.stone,Blocks.netherrack,Blocks.sand,Unsaga.blocks.fallStone,Blocks.anvil);
		public SkillFujiView(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSkill(InvokeSkill parent) {
			World world = parent.world;
			EntityLivingBase entity = parent.target;
			EntityPlayer ep = parent.getOwnerEP();
			parent.attack(entity, null);
			world.createExplosion(ep, entity.posX, entity.posY, entity.posZ, 1.5F,false);


			Random rand = world.rand;
			for(int i=0;i<10;i++){
				XYZPos ta = XYZPos.entityPosToXYZ(entity);
				XYZPos ppos = new XYZPos(ta.x+rand.nextInt(3)-1,ta.y+(i*3),ta.z+rand.nextInt(3)-1);
				PacketParticle pp = new PacketParticle(ppos,3,6);
				Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(entity));
				//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getParticleToPosPacket(pp, 3, 6));
			}
			ScanHelper scan = new ScanHelper(entity,3,3);
			for(;scan.hasNext();scan.next()){
				XYZPos scanpos = scan.getAsXYZPos();
				PairID blockdata = this.worldHelper.getBlockDatas(scanpos);
				PairID newblockdata = getAssociatedFallBlock(blockdata);
				Unsaga.debug(scan.getAsXYZPos()+":"+blockdata,this.getClass());

				if(blockSet.contains(blockdata.getBlockObject())){
					Unsaga.debug(scanpos.addPos(0, 6, 0));

					if(!world.isRemote){
						if(this.worldHelper.isAirBlock(scanpos.addPos(0, 3, 0))){
							this.worldHelper.setBlock(scanpos.addPos(0, 3, 0), newblockdata);
							this.worldHelper.setBlockToAir(scanpos);
						}

					}

				}


			}




		}

		protected PairID getAssociatedFallBlock(PairID blockdata){
			PairID ret = new PairID(blockdata.getBlockObject(),blockdata.getMeta());
			if(blockdata.getBlockObject()==Blocks.stone | blockdata.getBlockObject()==Blocks.cobblestone){
				ret.setData(Unsaga.blocks.fallStone, 0);
			}
			if(blockdata.getBlockObject()==Blocks.dirt | blockdata.getBlockObject()==Blocks.grass){
				ret.setData(Unsaga.blocks.fallStone, 3);
			}
			if(blockdata.getBlockObject()==Blocks.netherrack){
				ret.setData(Unsaga.blocks.fallStone, 5);
			}
			return ret;
		}
	}


	protected PairID getAssociatedFallBlock(PairID blockdata){
		PairID ret = new PairID();
		if(blockdata.getBlockObject()==Blocks.stone | blockdata.getBlockObject()==Blocks.cobblestone){
			ret.setData(Unsaga.blocks.fallStone, 0);
		}
		if(blockdata.getBlockObject()==Blocks.dirt | blockdata.getBlockObject()==Blocks.grass){
			ret.setData(Unsaga.blocks.fallStone, 3);
		}
		if(blockdata.getBlockObject()==Blocks.netherrack){
			ret.setData(Unsaga.blocks.fallStone, 5);
		}
		return ret;
	}

	public class SkillSkyDrive extends SkillMelee{

		public SkillSkyDrive(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){
			if(!ep.onGround){
				return super.canInvoke(world, ep, is, pos);
			}
			return false;
		}

		@Override
		public boolean hasFinishedPrepare(InvokeSkill parent){
			//if(!parent.world.isRemote){
			return LivingDebuff.hasDebuff(parent.owner, Unsaga.debuffManager.flyingAxe) && parent.owner.isSneaking();
			//}

			//return false;
		}

		@Override
		public void prepareSkill(InvokeSkill parent){

			if(!LivingDebuff.hasDebuff(parent.owner, Unsaga.debuffManager.flyingAxe)){
				this.setReadyToSkyDrive(parent.getOwnerEP());
			}
		}

		@Override
		public void invokeSkill(InvokeSkill parent) {
			LivingDebuff.removeDebuff(parent.owner, Unsaga.debuffManager.flyingAxe);
			EntityLivingBase target = LockOnHelper.searchEnemyNear(parent.owner, Unsaga.debuffManager.weaponTarget);
			parent.damageWeapon();
			if(parent.weapon==null){
				return;
			}
			ItemStack copyaxe = parent.weapon.copy();

			if(LivingDebuff.getLivingDebuff(parent.owner, Unsaga.debuffManager.weaponTarget).isPresent()){
				LivingStateTarget state = (LivingStateTarget)LivingDebuff.getLivingDebuff(parent.owner, Unsaga.debuffManager.weaponTarget).get();
				target = (EntityLivingBase) parent.world.getEntityByID(state.targetid);
			}

			EntityFlyingAxeNew entityflyingaxe = new EntityFlyingAxeNew(parent.world, parent.owner, 0.0F,copyaxe,true);
			entityflyingaxe.setDamage(parent.getModifiedAttackDamage());
			entityflyingaxe.setLPDamage(parent.getAttackDamageLP());
			if(target!=null){
				entityflyingaxe.setTarget(target);
			}




			if (!parent.world.isRemote)
			{

				if(entityflyingaxe.getAxeItemStack()!=null){
					parent.weapon.stackSize --;
					parent.world.spawnEntityInWorld(entityflyingaxe);
					//ItemStack aitemstack = null;
					//parent.ownerSkill.inventory.setInventorySlotContents(parent.ownerSkill.inventory.currentItem, null);
				}

			}


		}

		protected void setReadyToSkyDrive(EntityPlayer ep){
			ep.motionY += 1.0;
			LivingDebuff.addLivingDebuff(ep, new LivingStateFlyingAxe(Unsaga.debuffManager.flyingAxe,30,(int)ep.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()));
			LivingDebuff.addLivingDebuff(ep, new LivingState(Unsaga.debuffManager.antiFallDamage,30,true));
		}

	}


	public class SkillWoodBreaker extends SkillMelee{

		public SkillWoodBreaker(Type type) {
			super(type);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public boolean canInvoke(World world,EntityPlayer ep,ItemStack is,XYZPos pos){
			if(!ep.onGround){
				return super.canInvoke(world, ep, is, pos);
			}
			return false;
		}

		@Override
		public void prepareSkill(InvokeSkill parent){
			if(parent.world.isRemote){
				parent.owner.swingItem();
				Unsaga.debug("wood breaker:きてます",this.getClass());
				PacketSkill ps = new PacketSkill(PacketSkill.PACKETID.WOODBREAKER,parent.usepoint);
				Unsaga.packetPipeline.sendToServer(ps);
			}

		}


		@Override
		public void invokeSkill(InvokeSkill parent) {
			EntityLivingBase attacker = parent.owner;

			if(attacker.getHeldItem()!=null && attacker.getHeldItem().getItem() instanceof ItemAxeUnsaga){
				Random rand = parent.world.rand;
				ItemStack is = parent.weapon;
				XYZPos xyz = parent.usepoint;
				Unsaga.debug(xyz,this.getClass());

				parent.world.playSoundEffect((double)xyz.x, (double)xyz.y, (double)xyz.z, "random.explode", 4.0F, (1.0F + (parent.world.rand.nextFloat() -parent.world.rand.nextFloat()) * 0.2F) * 0.7F);

				PairID pairid = PairID.getBlockFromWorld(parent.world, xyz);
				Block block = pairid.getBlockObject();
				boolean isLog = false;

				if(block instanceof BlockLog)isLog = true;

				int[] oreIDs = OreDictionary.getOreIDs(new ItemStack(pairid.getBlockObject(),1,pairid.getMeta()));

				for(int id:oreIDs){
					isLog = OreDictionary.getOreName(id).equals("logWood");
				}

				if(!isLog)return;

				SoundAndSFX.playBlockBreakSFX(parent.world, xyz, pairid,true);
				RangeDamageUnsaga rangeDamage = new RangeDamageUnsaga(parent.world);
				rangeDamage.causeDamage(parent.getDamageSource(), HSLibs.getBounding(xyz, 2.0D, 1.0D), parent.getModifiedAttackDamage());

				for(int i=0;i<9;i++){
					ItemUtil.dropItem(parent.world, new ItemStack(Items.stick), xyz.getGaussian(rand));
				}



			}


		}

	}


}
