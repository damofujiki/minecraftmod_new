package com.hinasch.unlsagamagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.hinasch.lib.CSVText;
import com.hinasch.lib.HSLibs;
import com.hinasch.lib.UtilNBT;
import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingDebuff;
import com.hinasch.unlsaga.debuff.livingdebuff.LivingStateTarget;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsagamagic.spell.Spell;
import com.hinasch.unlsagamagic.spell.effect.InvokeSpell;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpellBook extends Item{


	protected static String AMPKEY = "Unsaga.spellAmp";
	protected static String COSTKEY = "Unsaga.spellCost";
	protected static String MAGICKEY = "Unsaga.spell";
	protected static String MIXKEY = "Unsaga.spellMixed";
	protected static String SPELLSKEY = "Unsaga.spellscontain";
	protected static String POSKEY = "Unsaga.spellPointer";
	
	
	public static ItemStack getAbilityItem(EntityPlayer ep,Ability ab){
		if(ep.getExtendedProperties(ExtendedPlayerData.KEY)!=null){
			ExtendedPlayerData data = (ExtendedPlayerData)ep.getExtendedProperties(ExtendedPlayerData.KEY);
			for(ItemStack is:data.getAccessories()){
				if(is!=null){
					HelperAbility helper = new HelperAbility(is, ep);
					if(helper.hasAbility(ab)){
						return is;
					}
				}

			}
		}
		return null;
	}
	
	public static void setBlendedSpellsHistory(ItemStack is,List<Spell> spells){
		if(spells==null)return;
		List<Integer> intlist = new ArrayList();
		for(Spell spell:spells){
			intlist.add(spell.number);
		}
		String str = CSVText.exchangeCollectionToCSV(intlist);
		UtilNBT.setFreeTag(is, SPELLSKEY, str);
	}
	
	public static List<Spell> getBlendedSpells(ItemStack is){
		Unsaga.debug(UtilNBT.readFreeStrTag(is, SPELLSKEY));
		List<Integer> intlist = CSVText.csvToIntList(UtilNBT.readFreeStrTag(is, SPELLSKEY));
		List<Spell> spelllist = new ArrayList();
		for(Integer inte:intlist){
			spelllist.add(Unsaga.magic.spellManager.getSpell(inte));
		}
		return spelllist;
	}
	
	public static void strapOffSpells(ItemStack is,World world,EntityPlayer ep){
		if(UtilNBT.hasKey(is, SPELLSKEY)){
			List<Spell> spellList = getBlendedSpells(is);
			for(Spell spell:spellList){
				ItemStack newstack = new ItemStack(Unsaga.magic.items.spellBook,1);
				ItemSpellBook.writeSpell(newstack, spell);
				if(!world.isRemote){
					ep.entityDropItem(newstack,0.2F);
				}
				
			}
			
			if(!world.isRemote){
				is.stackSize --;
			}
			
		}
	}
	
	
	public static void setAmp(ItemStack is,float par1){
		UtilNBT.setFreeTag(is, AMPKEY, (float)par1);
	}
	
	public static void setCost(ItemStack is,float par1){
		UtilNBT.setFreeTag(is, COSTKEY, par1);
	}
	
	public static void setBlended(ItemStack is,boolean par1){
		UtilNBT.setFreeTag(is, MIXKEY, par1);
	}
	
	public static float getAmp(ItemStack is){
		if(UtilNBT.hasKey(is, AMPKEY)){
			return MathHelper.clamp_float((float)UtilNBT.readFreeFloat(is, AMPKEY),0.1F,10.0F);
		}
		return 1.0F;
	}
	public static float getCost(ItemStack is){
		if(UtilNBT.hasKey(is, COSTKEY)){
			return MathHelper.clamp_float(UtilNBT.readFreeFloat(is, COSTKEY),0.1F,10.0F);
		}
		return 1.0F;
	}

	public static ItemStack getFistMagicItem(EntityPlayer ep,FiveElements.Enums element){
		switch(element){
		case FIRE:
			return getAbilityItem(ep,Unsaga.abilityManager.fire);
		case WOOD:
			return getAbilityItem(ep,Unsaga.abilityManager.wood);
		case WATER:
			return getAbilityItem(ep,Unsaga.abilityManager.water);
		case EARTH:
			return getAbilityItem(ep,Unsaga.abilityManager.earth);
		case METAL:	
			return getAbilityItem(ep,Unsaga.abilityManager.metal);
		case FORBIDDEN:
			return getAbilityItem(ep,Unsaga.abilityManager.forbidden);
		}
		return null;
	}
	
	public static Spell getSpell(ItemStack is){
		int spellnum = UtilNBT.readFreeTag(is, MAGICKEY);
		return Unsaga.magic.spellManager.getSpell(spellnum);
	}
	
	public static boolean hasMixed(ItemStack is){
		if(UtilNBT.hasKey(is, MIXKEY)){
			return UtilNBT.readFreeTagBool(is, MIXKEY);
		}
		return false;
	}
	
	public static void writeSpell(ItemStack is,Spell spell){
		UtilNBT.setFreeTag(is, MAGICKEY, spell.number);
	}
	
	public static boolean writeSpellToBook(EntityPlayer ep,ItemStack is,Spell spell){
		if(ep.inventory.hasItem(Items.book)){
			ep.inventory.consumeInventoryItem(Items.book);
			if(!ep.worldObj.isRemote){
				
				ItemStack newbook = new ItemStack(Unsaga.magic.items.spellBook,1);
				writeSpell(newbook,spell);
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("message.tablet.write"));
				//ep.addChatMessage("message.tablet.write");
				ep.entityDropItem(newbook,0.2F);
				

				return true;
				
			}
			
			
		}
		return false;
	}
	
	public ItemSpellBook() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(100);

		this.setTextureName(Unsaga.DOMAIN+":book_spell");

		this.setNoRepair();

	}
	
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(UtilNBT.hasKey(par1ItemStack, MAGICKEY)){
			Spell spell = ItemSpellBook.getSpell(par1ItemStack);
			String spellname = spell.getName(HSLibs.getCurrentLang())+"<"+(spell.element.getLocalized())+">";
			par3List.add(spellname);
		}
		if(ItemSpellBook.hasMixed(par1ItemStack)){
			String str = "Amplifier:"+ItemSpellBook.getAmp(par1ItemStack)+"/Cost:"+ItemSpellBook.getCost(par1ItemStack);
			par3List.add(str);
		}
		
	}
	
	@Override
    public IIcon getIconIndex(ItemStack par1ItemStack)
    {
        return this.getIconFromDamage(par1ItemStack.getItemDamage());
    }
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}
	
	@Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(Spell spell:Unsaga.magic.spellManager.getMap().values()){
    		ItemStack is = new ItemStack(this,1);
    		ItemSpellBook.writeSpell(is, spell);
    		par3List.add(is);
    	}

        
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return ItemSpellBook.hasMixed(par1ItemStack);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		//par3EntityPlayer.playSound("mob.wither.shoot", 0.5F,
		//		1.8F / (par2World.rand.nextFloat() * 0.4F + 1.2F));
		
		par3EntityPlayer.setItemInUse(par1ItemStack,
				this.getMaxItemUseDuration(par1ItemStack));

		return par1ItemStack;
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
    	
        return ItemSpellBook.getSpell(par1ItemStack).element.getElementColor();
    }
    
	@Override
	public void onPlayerStoppedUsing(ItemStack is, World par2World,
			EntityPlayer ep, int par4) {
		int var6 = this.getMaxItemUseDuration(is) - par4;
		float var7 = (float) var6 / 20.0F;
		var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

		if ((double) var7 < 0.1D) {
			return;
		}
		if (var7 > 1.0F) {
			Spell spell = ItemSpellBook.getSpell(is);
			ItemStack magicItem = ItemSpellBook.getFistMagicItem(ep, spell.element);
			boolean canInvoke = false;
			if(ep.capabilities.isCreativeMode)canInvoke = true;
			if(!ep.capabilities.isCreativeMode && magicItem!=null)canInvoke = true;
			
			if(ep.isSneaking() && !par2World.isRemote){
				if(LivingDebuff.getLivingDebuff(ep, Unsaga.debuffManager.spellTarget).isPresent()){
					LivingStateTarget debuff = (LivingStateTarget) LivingDebuff.getLivingDebuff(ep, Unsaga.debuffManager.spellTarget).get();
					int targetid = debuff.targetid;
					if(targetid>0){
						EntityLivingBase target = (EntityLivingBase) par2World.getEntityByID(targetid);
						if(target!=null){
							Unsaga.debug(target.getCommandSenderName());
							
							if(!par2World.isRemote  && canInvoke){
								InvokeSpell invokeSpell = new InvokeSpell(spell, par2World, ep, is,target);
								invokeSpell.setMagicItem(magicItem);
								invokeSpell.run();
							}

						}
					}
					
				}
				
			}else{
				//なぜか追突ケンのあと昌栄できない？
				if(!par2World.isRemote  && canInvoke){
					InvokeSpell invokeSpell = new InvokeSpell(spell, par2World, ep, is);
					invokeSpell.setMagicItem(magicItem);
					invokeSpell.run();
				}

			}
			
		}
	}
	

    public static void writePosition(ItemStack is,XYZPos pos){
    	UtilNBT.setFreeTag(is, POSKEY, pos.toString());
    }
    
    public static XYZPos readPosition(ItemStack is){
    	if(UtilNBT.hasKey(is, POSKEY)){
    		return XYZPos.strapOff(UtilNBT.readFreeStrTag(is, POSKEY));
    	}
    	return null;
    }
    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	if(ItemSpellBook.getSpell(is).isUsePointer() && ep.isSneaking() && !world.isRemote){
    		ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.spell.setposition"));
    		//par2EntityPlayer.addChatMessage(Translation.localize("msg.spell.setposition"));
    		writePosition(is,new XYZPos(par4,par5,par6));
    	}
        return false;
    }
}
