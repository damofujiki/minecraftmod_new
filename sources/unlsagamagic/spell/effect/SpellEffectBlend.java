package com.hinasch.unlsagamagic.spell.effect;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import com.google.common.collect.Lists;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.ItemUtil;
import com.hinasch.lib.PairID;
import com.hinasch.lib.ScanHelper;
import com.hinasch.lib.WorldHelper;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.block.BlockChestUnsagaNew;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingBuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateFireStorm;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateRandomThrow;
import com.hinasch.unlsaga.util.ChatUtil;
import com.hinasch.unlsaga.util.LockOnHelper;
import com.hinasch.unlsaga.util.translation.Translation;

public class SpellEffectBlend {

	public static SpellEffectBlend INSTANCE;

	public final SpellBase crimsonFlare = new SpellCrimsonFlare();
	public final SpellBase detectTreasure = new SpellDetectTreasure();
	public final SpellBase goldFinger = new SpellGoldFinger();
	public final SpellBase iceNine = new SpellIceNine();
	public final SpellBase leavesShield = new SpellLeavesShield();
	public final SpellBase megaPower = new SpellMegaPowerRise();
	public final SpellBase reflesh = new SpellReflesh();
	public final SpellBase stoneShower = new SpellStoneShower();
	public final SpellBase thudnerCrap = new SpellThunderCrap();
	public final SpellBase restoration = new SpellRestoration();
	
	public class SpellRestoration extends SpellHealing{

		@Override
		public void hookOnHealed(InvokeSpell parent, EntityLivingBase target) {
			
			
		}
		
	}
	public class SpellThunderCrap extends SpellBase{

		public SpellThunderCrap() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell invoke) {
			if(!LivingDebuff.hasDebuff(invoke.getInvoker(), Unsaga.debuffManager.thunderCrap)){
				LivingDebuff.addLivingDebuff(invoke.getInvoker(), new LivingStateRandomThrow(Unsaga.debuffManager.thunderCrap,100,10, 1));
				
			}
			
		}
		
	}
	public class SpellStoneShower extends SpellBase{

		public SpellStoneShower() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell spell) {
//			if(spell.getAmp()>1.5F){
//				spell.world.playSoundAtEntity(spell.getInvoker(), "mob.ghast.fireball", 1.0F, 1.0F / (spell.world.rand.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);
//				
//				EntityBoulderNew var8 = new EntityBoulderNew(spell.world, spell.getInvoker(), 1.0F * 2.0F,true);
//				var8.canBePickedUp = 0;
//				var8.setDamage(var8.getDamage()+(1.0*spell.getAmp()));
//				var8.setRangeStoneShower(Math.round(10*spell.getAmp()));
//				if (!spell.world.isRemote)
//				{
//					spell.world.spawnEntityInWorld(var8);
//				}
//			}else{
				LivingDebuff.addLivingDebuff(spell.getInvoker(), new LivingStateRandomThrow(Unsaga.debuffManager.stoneShower,100,18,(int)(1.0F*spell.getAmp())));
			//}
			

			return;
			
		}
		
	}
	public class SpellLeavesShield extends SpellBase{

		public SpellLeavesShield() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell parent) {
			int remain = (int)((float)15 * parent.getAmp());
			int amp = (int)((float)30 * parent.getAmp());
			if(parent.getTarget().isPresent()){
				LivingDebuff.addLivingDebuff(parent.getTarget().get(), new LivingBuff(Unsaga.debuffManager.leavesShield,remain,amp));
			}else{
				LivingDebuff.addLivingDebuff(parent.getInvoker(), new LivingBuff(Unsaga.debuffManager.leavesShield,remain,amp));
			}
			
		}
		
	}
	public class SpellIceNine extends SpellBase{

		protected Map<Block,Block> blockTransformMap = new HashMap();
		
		public SpellIceNine() {
			super();
			this.blockTransformMap.put(Blocks.water, Blocks.ice);
			this.blockTransformMap.put(Blocks.flowing_water, Blocks.ice);
			this.blockTransformMap.put(Blocks.lava, Blocks.obsidian);
			this.blockTransformMap.put(Blocks.flowing_lava, Blocks.cobblestone);
			this.blockTransformMap.put(Blocks.fire, Blocks.air);
			
		}

		@Override
		public void invokeSpell(InvokeSpell parent) {
			int amp = 1;

			if(parent.getInvoker().isBurning()){
				parent.getInvoker().setFire(0);
			}

			ScanHelper scan = new ScanHelper(parent.getInvoker(),11,5);
			scan.setWorld(parent.world);

			for(;scan.hasNext();scan.next()){
				if(scan.isValidHeight() && !scan.world.isRemote){
					if(this.blockTransformMap.containsKey(scan.getBlock())){
						Block blockTransformTo = this.blockTransformMap.get(scan.getBlock());
						scan.setBlockHere(blockTransformTo);
					}

					if(scan.isOpaqueBlock() && scan.isAirBlockUp() && !scan.isPlayerPos()){
						System.out.println("true");
						
						this.worldHelper.setBlock(scan.getAsXYZPos().add(WorldHelper.UP), new PairID(Blocks.snow_layer,0));
						//parent.world.setBlock(scan.sx, scan.sy+1, scan.sz, Blocks.snow,0,2);
					}
					if(scan.getBlock()==Blocks.snow_layer && !scan.isPlayerPos()){
						int meta = scan.getMetadata();
						if(meta<=15){
							this.worldHelper.addBlockMetadata(scan.getAsXYZPos(), 3);
						}
					}


				}
			}
			
		}
		
	}
	public class SpellReflesh extends SpellHealing{

		public SpellReflesh() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void hookOnHealed(InvokeSpell parent, EntityLivingBase target) {
			for(Potion potion:Potion.potionTypes){
				if(potion!=null){
					if(potion.isBadEffect() && target.isPotionActive(potion.id)){
						target.removePotionEffect(potion.id);
					}
				}
			}
		}
		
	}
	
	public class SpellCrimsonFlare extends SpellBase{

		public SpellCrimsonFlare() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell parent) {
			int amp =(int) parent.getAmp();

			int targetid = -1;
			XYZPos xyz = null;
			if(parent.getTarget().isPresent()){
				xyz = XYZPos.entityPosToXYZ(parent.getTarget().get());
				targetid = parent.getTarget().get().getEntityId();

			}else{
				EntityLivingBase nearent = LockOnHelper.searchEnemyNear(parent.getInvoker(), Unsaga.debuffManager.spellTarget);
				if(nearent!=null){
					xyz = XYZPos.entityPosToXYZ(nearent);
					targetid = nearent.getEntityId();
				}
			}
			if(targetid!=-1 && !LivingDebuff.hasDebuff(parent.getInvoker(), Unsaga.debuffManager.crimsonFlare)){
				LivingDebuff.addLivingDebuff(parent.getInvoker(), new LivingStateFireStorm(Unsaga.debuffManager.crimsonFlare,100,xyz.x,xyz.y,xyz.z,amp,targetid));

			}

			return;
			
		}
		
	}
	
	
	public class SpellPowerRise extends SpellAddBuff{
		public SpellPowerRise(){
			this.potions = Lists.newArrayList(Potion.digSpeed);
		}
	}
	
	public class SpellMegaPowerRise extends SpellAddBuff{
		public SpellMegaPowerRise(){
			this.potions = Lists.newArrayList(Potion.digSpeed,Potion.damageBoost,Potion.jump,Potion.moveSlowdown);
		}
	}
	
	public class SpellDetectTreasure extends SpellBase{

		public SpellDetectTreasure() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell parent) {
			int range = Math.round((16*9)*parent.getAmp());
			if(range<1){
				range = 1;
			}
			if(range>16*16){
				range = 16*16;
			}
			Unsaga.debug(range);

			boolean found = false;
			ScanHelper scan = new ScanHelper(parent.getInvoker(),range,range);
			scan.setWorld(parent.world);
			for(;scan.hasNext();scan.next()){
				if(scan.isValidHeight()){
					if(!scan.isAirBlock() && scan.getBlock()!=null){
						Block block = scan.getBlock();
						if(block instanceof BlockChest || block instanceof BlockChestUnsagaNew){
							Unsaga.debug("発見");
							XYZPos distance_pos = scan.getAsXYZPos().subtract(XYZPos.entityPosToXYZ(parent.getInvoker()));
							distance_pos.setAsBlockPos(true);
							Vec3 vec = Vec3.createVectorHelper((int)scan.sx, (int)scan.sy, (int)scan.sz);
							Vec3 vec2 = parent.getInvoker().getPosition(1.0F);
							//int distance = (int) (vec2.distanceTo(vec));
							ChatUtil.addMessageNoLocalized(parent.getInvoker(), "Detect Chest:"+distance_pos.toString());
							found = true;
						}

					}
				}

			}


			if(!found){
				ChatUtil.addMessage(parent.getInvoker(), "msg.spell.chest.notfound");
				
			}
			return;
		}
		
	}
	public static SpellEffectBlend getInstance(){
		if(INSTANCE==null){
			INSTANCE = new SpellEffectBlend();
		}
		return INSTANCE;

	}
	
	public class SpellGoldFinger extends SpellBase{

		public SpellGoldFinger() {
			super();
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void invokeSpell(InvokeSpell parent) {
			if(parent.getTarget().isPresent()){
				EntityLivingBase target = parent.getTarget().get();
				if(target instanceof IBossDisplayData){
					return;
				}
				int prob = (int)70 - (int)(target.getHealth()/HSLibs.exceptZero(target.getMaxHealth(),1.0F) * 100);
				
				prob = MathHelper.clamp_int(prob, 1, 100);
				ItemStack goldnugget = new ItemStack(Items.gold_nugget,1);
				if(parent.world.rand.nextInt(100)<prob){
					ItemUtil.dropItem(parent.world, goldnugget, target.posX, target.posY, target.posZ);
					target.setDead();
					String str = Translation.localize(Translation.localize("msg.spell.touchgold.succeeded"));
					String formatted = String.format(str, target.getCommandSenderName());
					ChatUtil.addMessageNoLocalized(parent.getInvoker(), formatted);
				}else{


					ChatUtil.addMessage(parent.getInvoker(), "msg.spell.touchgold.failed");
					//parent.getInvoker().addChatMessage(Translation.localize("msg.spell.touchgold.failed"));
				}
				
			}
			
		}
		
	}
	

}
