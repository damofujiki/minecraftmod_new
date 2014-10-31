package com.hinasch.unlsagamagic.spell.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import com.hinasch.lib.ScanHelper;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.skill.effect.AbstractInvoker;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsaga.util.ChatUtil;
import com.hinasch.unlsaga.util.HelperChestUnsaga;
import com.hinasch.unlsaga.util.LockOnHelper;
import com.hinasch.unlsaga.util.damage.DamageHelper;
import com.hinasch.unlsaga.util.damage.DamageSourceUnsaga;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsagamagic.element.WorldElements;
import com.hinasch.unlsagamagic.item.ItemSpellBook;
import com.hinasch.unlsagamagic.spell.Spell;
import com.hinasch.unlsagamagic.spell.SpellMixTable;

public class InvokeSpell extends AbstractInvoker{

	//public EntityLivingBase invoker;
	protected ItemStack magicItem;
	protected int prob;
	protected Spell spell;
	protected ItemStack spellbook;
	protected SpellBase spellEffect;
	protected EntityLivingBase target;
	protected WorldElements worldElement;
	
	public InvokeSpell(Spell spell,World world,EntityLivingBase owner,ItemStack is){
		super(world,owner);
		this.spell = spell;
		this.spellbook = is;
		this.worldElement = Unsaga.magic.elementCalculator;
		this.spellEffect = this.spell.getSpellEffect();
		this.spellEffect.setWorldHelper(world);
		//this.isSneak = false;
	}
	
	public InvokeSpell(Spell spell,World world,EntityPlayer ep,ItemStack is,EntityLivingBase target){
		this(spell,world,ep,is);
		this.target = target;
		
	}



	public int figureSuccessProb(){
		SpellMixTable elementAround = worldElement.getFiguredTable(world, getInvoker());
		float probfloat = (float)this.spell.getBaseProb() + this.getProbModification();
		int prob = Math.round(probfloat);
		MathHelper.clamp_int(prob, 15, 100);
		this.prob = prob;
		return prob;
	}
	
	protected float getProbModification(){
		SpellMixTable elementAround = worldElement.getFiguredTable(world, getInvoker());
		int elm = elementAround.getInt(this.spell.element);
		if(elm==0){
			return -10;
		}
		if(elm>10){
			float f1 = (float)elm * 0.3F;
			return f1;
		}

		return 0;

		
	}
	
	protected float getAmpModification(){
		SpellMixTable elementAround = worldElement.getFiguredTable(world, getInvoker());
		int elm = elementAround.getInt(this.spell.element);
		if(elm==0){
			return -0.3F;
		}
		if(elm>10){
			float f1 = (float)elm * 0.01F;
			return f1;
		}

		return 0.0F;
		
	}
	public float getAmp(){
		
		float amp = this.spellbook!=null? ItemSpellBook.getAmp(this.spellbook) : 1.0F;
		amp += this.getAmpModification();
		if(LivingDebuff.hasDebuff(getInvoker(), Unsaga.debuffManager.downMagic)){
			amp = amp /2;
			amp = MathHelper.clamp_float(amp, 0.6F, 10.0F);
			
		}
		
//		worldElement.figureElements(world, getInvoker());
//		SpellMixTable elementAround = worldElement.getElementsTableFromWorld();
//		float f1 = (float)elementAround.getInt(this.spell.element)/10.0F;
//		if(f1<1.0F){
//			f1 = 1.0F;
//		}
//		amp *= f1;
//		Unsaga.debug(f1,this.getClass());
		return amp;
	}
	
	public int getCost(){
		//todo:まわりの五行地も反映するように
		int spellcost = this.spell.getDamageForItem();
		return Math.round(((float)spellcost*ItemSpellBook.getCost(this.spellbook)));
	}
	
	public void setTarget(EntityLivingBase target){
		this.target = target;
	}
	public DamageSourceUnsaga getDamageSource(){
		DamageSourceUnsaga ds = new DamageSourceUnsaga(null,this.getInvoker(),this.getSpell().getStrHurtHP(),DamageHelper.Type.MAGIC);
		ds.setMagicDamage();
		ds.setDamageBypassesArmor();
		return ds;
	}
	
	public EntityLivingBase getInvoker(){
		return this.owner;
	}
	
	public Optional<ItemStack> getMagicItem(){
		if(this.magicItem!=null){
			return Optional.of(this.magicItem);
		}
		return Optional.absent();
	}
	
	public int getProbability(){
		return this.prob;
	}
	
	public Spell getSpell(){
		return this.spell;
	}
	public Optional<EntityLivingBase> getTarget(){
		if(target!=null){
			return Optional.of(this.target);
		}
		return Optional.absent();
	}
	
	public EntityLivingBase getTargetOrfindTarget(){
		if(this.getTarget().isPresent()){
			return this.getTarget().get();
		}
		return LockOnHelper.searchEnemyNear(getInvoker(), Unsaga.debuffManager.spellTarget);
		
	}
	
	public void run(){
		if(!this.world.isRemote && this.tryInvoke()){
			this.tryUnlockMagicLock();
			ChatUtil.addMessageNoLocalized(getInvoker(), Translation.localize("msg.spell.succeeded")+"("+prob+"%)");
			PacketParticle pp = new PacketParticle(this.getSpell().element.getElementParticle(),this.getInvoker().getEntityId(),6);
			Unsaga.packetDispatcher.sendToAllAround(pp, PacketUtil.getTargetPointNear(this.getInvoker()));
			this.spellEffect.invoke(this);
			if(this.getMagicItem().isPresent()){
				this.getMagicItem().get().damageItem(this.getCost(), this.getInvoker());
			}
		}else{
			ChatUtil.addMessageNoLocalized(getInvoker(), Translation.localize("msg.spell.failed")+"("+prob+"%)");

		}
	}
	
	public void setMagicItem(ItemStack is){
		this.magicItem = is;
	}
	

	//成功は必ずするように
	public boolean tryInvoke(){
		if(this.owner instanceof EntityPlayer){
			return this.world.rand.nextInt(100)<this.figureSuccessProb() ? true : false;
		}
		return true;
	}
	
	
	public void tryUnlockMagicLock(){
		if(this.getInvoker() instanceof EntityPlayer){
			ScanHelper scan = new ScanHelper((EntityPlayer) this.getInvoker(),3,3);
			for(;scan.hasNext();scan.next()){
				TileEntity te = this.world.getTileEntity(scan.sx, scan.sy, scan.sz);
				if(te instanceof TileEntityChestUnsagaNew){
					TileEntityChestUnsagaNew chest = (TileEntityChestUnsagaNew)te;
					HelperChestUnsaga hc = new HelperChestUnsaga(chest);
					if(hc.tryUnlockMagicalLock()){
						ChatUtil.addMessage(getInvoker(), "msg.chest.magiclock.unlocked");
						
					}
				}
			}
		}

	}
}
