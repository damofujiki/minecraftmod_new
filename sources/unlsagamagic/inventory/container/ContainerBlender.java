package com.hinasch.unlsagamagic.inventory.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.Unsaga.guiNumber;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsagamagic.UnsagaMagic;
import com.hinasch.unlsagamagic.client.GuiBlender;
import com.hinasch.unlsagamagic.inventory.InventoryBlender;
import com.hinasch.unlsagamagic.inventory.slot.SlotBlender;
import com.hinasch.unlsagamagic.item.ItemSpellBook;
import com.hinasch.unlsagamagic.spell.Spell;
import com.hinasch.unlsagamagic.spell.SpellBlend;
import com.hinasch.unlsagamagic.spell.SpellMixTable;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerBlender extends ContainerBase
{
	public UnsagaMagic core = Unsaga.magic;
	public static String AMP = "amplifier";

	public static String COST = "cost";
	public static String ELEMENTS = "elements";

	protected InventoryBlender invBlender;
	
	private InventoryPlayer invp;
	private EntityPlayer playerBlending;
	private World world;

	public ContainerBlender(EntityPlayer player,World world)
	{
		super(player, new InventoryBlender());
		int slotnum = 0;
		//is.world  = world;

		this.invBlender = (InventoryBlender)this.inv;
		this.world = player.worldObj;
		this.playerBlending = player;
		this.invp = player.inventory;

		for(int i=0;i<3 ;i++){
			for(int j=0;j<3 ;j++){
				addSlotToContainer(new SlotBlender(this.invBlender,j + i * 3, 62 + j * 18, 17 + i * 18));
			}
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{


		return this.playerBlending == entityPlayer;
	}

	public void consumeSpellBooks(){
		for(int i=0;i<this.invBlender.getSizeInventory();i++){
			ItemStack is = this.invBlender.getStackInSlot(i);
			if(is!=null){
				this.invBlender.decrStackSize(i, 1);
			}
		}
	}




	

    
    public void doBlendSpells(){
		Spell transformed = this.getSpellTransformed().get();
		HashMap<String,SpellMixTable> tables = this.getCurrentAllElement();
		float costfloat = tables.get(COST).get(transformed.element);
		float ampfloat = tables.get(AMP).get(transformed.element);
		float blendedcost = 1;
		float blendedamp = 1;
		blendedcost += blendedcost * (costfloat/100);
		blendedamp += blendedamp * (ampfloat/100);
		blendedcost = MathHelper.clamp_float(blendedcost, 0.1F, 4.0F);
		blendedamp = MathHelper.clamp_float(blendedamp, 0.1F, 4.0F);
		
		ItemStack isBlended = new ItemStack(core.items.spellBook,1);
		this.initializeNewBlendedSpell(isBlended, transformed, blendedamp, blendedcost);
		List<Spell> spellList = new ArrayList();
		this.gatherSpellsForHistory(spellList);
		ItemSpellBook.setBlendedSpellsHistory(isBlended, spellList);
		if(!this.world.isRemote){
			this.playerBlending.entityDropItem(isBlended,0.2F);
		}
		if(!this.playerBlending.capabilities.isCreativeMode){
			this.consumeSpellBooks();
		}
	}
	
	public void gatherSpellsForHistory(List<Spell> spellList){
		for(int i=0;i<this.invBlender.getSizeInventory();i++){
			if(this.invBlender.getStackInSlot(i)!=null){
				spellList.add(ItemSpellBook.getSpell(this.invBlender.getStackInSlot(i)));
			}
		}
	}
	
	public int getAmountSlotSpellBook(){
		int amount = 0;
		for(int i=0;i<this.invBlender.getSizeInventory();i++){
			if(this.invBlender.getStackInSlot(i)!=null){
				amount +=1;
			}
		}
		return amount;
	}

	public Optional<Spell> getBaseMagic(){
		if(this.invBlender.getStackInSlot(0)!=null){
			return Optional.of(ItemSpellBook.getSpell(this.invBlender.getStackInSlot(0)));
		}
		return Optional.absent();
	}
	public ItemStack getBeseSpellBookItemStack(){
		return this.invBlender.getStackInSlot(0);
	}

	public HashMap<String,SpellMixTable> getCurrentAllElement(){
		SpellMixTable table = new SpellMixTable(0,0,0,0,0,0);
		SpellMixTable cost = new SpellMixTable(0,0,0,0,0,0);
		SpellMixTable amp = new SpellMixTable(0,0,0,0,0,0);
		HashMap<String,SpellMixTable> allMap = new HashMap();
		for(int i=0;i<this.invBlender.getSizeInventory();i++){
			ItemStack is = this.invBlender.getStackInSlot(i);
			if(is!=null){
				Spell spell = ItemSpellBook.getSpell(is);
				if(!(spell instanceof SpellBlend)){
					table.add(spell.elementsTable);
					if(i!=0){
						cost.add(spell.elementsCost);
						amp.add(spell.elementsAmp);
					}

				}
			}
		}
		table.cut(0, 50);
		cost.cut(-99, 300);
		amp.cut(-99, 200);
		allMap.put(ELEMENTS, table);
		allMap.put(AMP, amp);
		allMap.put(COST, cost);
		return allMap;
	}
	public SpellMixTable getCurrentElement(){
		return this.getCurrentAllElement().get(ELEMENTS);
	}
	
	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return guiNumber.BLENDER;
	}
	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			Object... args) {
		// TODO 自動生成されたメソッド・スタブ
		return new PacketGuiButtonNew(guiID,buttonID);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}


	
	public Optional<Spell> getSpellTransformed(){
		if(this.getBaseMagic().isPresent()){
			for(SpellBlend blend:Unsaga.magic.spellManager.getAllBlendsMap()){
				if(blend.getRequireMap().containsKey(this.getBaseMagic().get())){
					Unsaga.debug("キーがありました");
					if(this.getCurrentElement().isBiggerThan(blend.getRequireMap().get(this.getBaseMagic().get()))){
						Unsaga.debug("でかい");
						return Optional.of((Spell)blend);
					}
				}
			}
			return Optional.of(this.getBaseMagic().get());
		}

		return Optional.absent();
	}
	

	public boolean hasNoBlendSpell(){
		boolean flag = true;
		for(int i=0;i<this.invBlender.getSizeInventory();i++){
			ItemStack is = this.invBlender.getStackInSlot(i);
			if(is!=null){
				if(ItemSpellBook.hasMixed(is)){
					flag = false;
				}
			}
		}
		return flag;
	}

	public void initializeNewBlendedSpell(ItemStack isBlended,Spell transformed,float blendedamp,float blendedcost){
		ItemSpellBook.writeSpell(isBlended,transformed);
		ItemSpellBook.setAmp(isBlended, blendedamp);
		ItemSpellBook.setCost(isBlended, blendedcost);
		ItemSpellBook.setBlended(isBlended, true);
	}

	//パケット受け取った時に一緒に実効される
	public void onPacketData() {
		switch(this.buttonID){
		case GuiBlender.BUTTON_UNDO:
			if(getBeseSpellBookItemStack()!=null){
				if(ItemSpellBook.hasMixed(this.invBlender.getStackInSlot(0))){

					this.undoBlendedSpell();
				}
			}
			break;
		case GuiBlender.BUTTON_BLEND:
			if(this.hasNoBlendSpell() && this.getAmountSlotSpellBook()>1){
				if(this.getSpellTransformed().isPresent()){

					this.doBlendSpells();

				}
				
			}
			break;
		}

	}

	public void undoBlendedSpell(){
		List<Spell> spells = ItemSpellBook.getBlendedSpells(this.invBlender.getStackInSlot(0));
		for(Spell spell:spells){
			ItemStack newstack = new ItemStack(core.items.spellBook,1);
			ItemSpellBook.writeSpell(newstack, spell);
			if(!this.world.isRemote){
				this.playerBlending.entityDropItem(newstack,0.2F);
			}
		}
		this.invBlender.decrStackSize(0, 1);
	}
	

}
