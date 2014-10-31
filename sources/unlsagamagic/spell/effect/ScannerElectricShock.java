package com.hinasch.unlsagamagic.spell.effect;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.hinasch.lib.AbstractScanner;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.PairID;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;
import com.hinasch.unlsagamagic.spell.Spells;

public class ScannerElectricShock extends AbstractScanner{

	protected EntityLivingBase owner;
	
	public ScannerElectricShock(World world,int length,PairID compareBlock, XYZPos startpoint,EntityLivingBase owner) {
		super(world,compareBlock, startpoint,length);
		this.owner = owner;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void hook(World world, Block currentBlock, XYZPos currentPos) {

		Spells spells = Unsaga.magic.spellManager;
		AxisAlignedBB bb = HSLibs.getBounding(currentPos, 2.0D, 2.0D);
		if(!world.getEntitiesWithinAABB(EntityLivingBase.class, bb).isEmpty()){
			Unsaga.debug("範囲内に発見");
			List<EntityLivingBase> livings = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			for(EntityLivingBase living:livings){
				DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.owner,spells.thunderCrap.getStrHurtLP(),DamageHelper.Type.MAGIC);
				ds.setElement(FiveElements.Enums.WOOD);
				living.attackEntityFrom(ds, spells.thunderCrap.getStrHurtHP());
			}

			
		}
	}

}
