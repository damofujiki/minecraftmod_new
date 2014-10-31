package com.hinasch.unlsaga.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

import com.hinasch.unlsaga.entity.EntityTreasureSlime;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.translation.Translation;

public class ChestTraps {

	public static final Map<Integer,TrapChest> trapMap = new HashMap();
	
	public static final TrapChest dummy = new TrapChest(-1,"dummy",null){
		
	};
	
	public static final TrapChest explode = new TrapChest(0,"explode",trapMap){
		@Override
		public void activate(TileEntityChestUnsagaNew chest,EntityPlayer ep){
			if(!chest.getWorldObj().isRemote){

				float explv = ((float)chest.level * 0.06F);
				explv = MathHelper.clamp_float(explv, 1.0F, 4.0F);
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.chest.burst"));
				chest.getWorldObj().createExplosion(null, chest.xCoord, chest.yCoord, chest.zCoord, 1.5F*explv, true);
				chest.setTrapOccured(true);
			}
		}
	};
	
	public static final TrapChest needle = new TrapChest(1,"needle",trapMap){
		@Override
		public void activate(TileEntityChestUnsagaNew chest,EntityPlayer ep){
			int damage = chest.getWorldObj().rand.nextInt(MathHelper.clamp_int(chest.level/15,3,100))+1;
			damage = MathHelper.clamp_int(damage, 1, 10);
			ep.attackEntityFrom(DamageSource.cactus, damage);
			ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.chest.needle"));
			if(ep.getRNG().nextInt(100)<50){
				chest.setTrapOccured(true);
			}
		}
	};
	
	public static final TrapChest poison = new TrapChest(2,"poison",trapMap){
		@Override
		public void activate(TileEntityChestUnsagaNew chest,EntityPlayer ep){
			ep.addPotionEffect(new PotionEffect(Potion.poison.id,10*(chest.level/2+1),1));
			ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.chest.poison"));

			chest.setTrapOccured(true);
		}
	};
	
	public static final TrapChest slime = new TrapChest(-2,"slime",null){
		private int spawnRange = 2;
		
		@Override
		public void activate(TileEntityChestUnsagaNew chest,EntityPlayer ep){
			chest.setSlimeTrapOccured(true);
			Entity var13 = null;
			//if(doChance(this.worldObj.rand,40)){
				var13 = new EntityTreasureSlime(chest.getWorldObj(),chest.level);
			//}else{
			//	var13 = new EntitySlime(this.worldObj);
			//}

			if(var13!=null){		
				double var5 = (double)chest.xCoord + (chest.getWorldObj().rand.nextDouble() - chest.getWorldObj().rand.nextDouble()) * (double)spawnRange;
				double var7 = (double)(chest.yCoord+ chest.getWorldObj().rand.nextInt(3) - 1);
				double var9 = (double)chest.zCoord + (chest.getWorldObj().rand.nextDouble() - chest.getWorldObj().rand.nextDouble()) * (double)spawnRange;
				EntityLiving var11 = var13 instanceof EntityLiving ? (EntityLiving)var13 : null;
				var13.setLocationAndAngles(var5, var7, var9, chest.getWorldObj().rand.nextFloat() * 360.0F, 0.0F);
				//if(var11.getCanSpawnHere()){
				if(!chest.getWorldObj().isRemote){
					chest.getWorldObj().spawnEntityInWorld(var13);
				}
				//}

			}
		}
	};

	public static TrapChest getTrap(int num){
		if(num>0){
			return trapMap.get(num);
		}

		return dummy;
	}
}
