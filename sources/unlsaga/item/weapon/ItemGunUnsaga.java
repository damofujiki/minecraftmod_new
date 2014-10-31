package com.hinasch.unlsaga.item.weapon;


import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.entity.projectile.EntityBullet;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;


public class ItemGunUnsaga extends ItemBow implements IUnsagaMaterialTool{

	private String iconname;

	//MMM氏のFN5728Gunsをもとに
	protected final int RELOAD_END = 0x0010;
	protected final int RELOAD_START = 0x0008;
	protected final int FIRE = 0x0000;

	protected MaterialInfo info;

	public ItemGunUnsaga(String par2) {
		super();
		this.iconname = par2;
		this.setMaxDamage(384);
		Unsaga.proxy.registerMusketRenderer(this);
	}

	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		this.info = new MaterialInfo(par2ItemStack);
        return info.getMaterial().isPresent()?info.getMaterial().get()==this.getMaterial() : false;
    }
	
	
	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	{

		if(par1ItemStack==null){
			return;
		}

		if(this.getReload(par1ItemStack)==RELOAD_START){
			
			this.setReload(par1ItemStack, RELOAD_END);
			par3EntityPlayer.worldObj.playSoundAtEntity(par3EntityPlayer, "fire.ignite", 1.0F, 1.0F / (par3EntityPlayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);

			return;
		}
		if(this.getReload(par1ItemStack)<RELOAD_END){
			return;
		}
		
		int j = this.getMaxItemUseDuration(par1ItemStack) - par4;

		boolean flag = true;

		if (flag)
		{
			float f = (float)j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double)f < 0.1D)
			{
				return;
			}

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			EntityBullet entityarrow = new EntityBullet(par2World, par3EntityPlayer, 1.0F * 2.0F);

			if (f == 1.0F)
			{
				entityarrow.setIsCritical(true);
			}

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

			if (k > 0)
			{
				entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
			}

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

			if (l > 0)
			{
				entityarrow.setKnockbackStrength(l);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
			{
				entityarrow.setFire(100);
			}

			par1ItemStack.damageItem(1, par3EntityPlayer);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.explode", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			if (flag)
			{
				entityarrow.canBePickedUp = 2;
			}
			else
			{
				par3EntityPlayer.inventory.consumeInventoryItem(Items.arrow);
			}

			if (!par2World.isRemote)
			{
				par2World.spawnEntityInWorld(entityarrow);
			}
			this.setReload(par1ItemStack, FIRE);
			
			par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * 1.0F;
		}
	}


//	public void setBarrett(ItemStack is){
//		UtilSkill.setStateNBT(is, RELOAD_END);
//	}
//
//	public void setReady(ItemStack is){
//		UtilSkill.setStateNBT(is, FIRE);
//	}

	public boolean isWeaponReload(ItemStack itemstack, EntityPlayer entityplayer){
		cancelReload(itemstack,RELOAD_END);
		return canReload(itemstack,entityplayer);
	}
	
//	public boolean hasSetBarrett(ItemStack is){
//		return UtilSkill.readStateNBT(is)!=0;
//	}
//
//	public boolean isReadyToFire(ItemStack is){
//		return UtilSkill.readStateNBT(is)==this.FIRE;
//	}
//
//	public void setFinish(ItemStack is){
//		UtilSkill.setStateNBT(is, 0);
//	}

	public int getReload(ItemStack is){

		if(!UtilNBT.hasiInitState(is)){
			UtilNBT.setState(is, FIRE);
		}
		return UtilNBT.readState(is);
	}
	
	protected void setReload(ItemStack is,int val){
		Unsaga.debug(val);
		UtilNBT.setState(is, val);
	}
	
	protected void cancelReload(ItemStack itemstack, int force) {
		if (getReload(itemstack) >= force) {
			setReload(itemstack, FIRE);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if(this.getReload(par1ItemStack)==FIRE && !this.canReload(par1ItemStack, par3EntityPlayer)){
			return par1ItemStack;
		}
//
//		if(this.getReload(par1ItemStack)==RELOAD_START){
//			this.setReload(par1ItemStack, RELOAD_END);
//			par3EntityPlayer.worldObj.playSoundAtEntity(par3EntityPlayer, "fire.ignite", 1.0F, 1.0F / (par3EntityPlayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
//			return par1ItemStack;
//		}
		
//		int li = this.getReload(par1ItemStack);
//		if(li <= FIRE){
//			if(this.canReload(par1ItemStack, par3EntityPlayer)){
//				
//			}
//		}
//		ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
//		MinecraftForge.EVENT_BUS.post(event);
//		if (event.isCanceled())
//		{
//			return event.result;
//		}
//
//		if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(InitUnsagaTools.itemBarrett.itemID))
//		{
//			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
//		}
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}
//
//	@Override
//	public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count)
//	{
//
//		
//		if(stack!=null){
//			//System.out.println(count);
//			boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0
//					|| player.inventory.hasItem(InitUnsagaTools.itemBarrett.itemID);
//
//			if(!this.hasSetBarrett(stack) && this.getMaxItemUseDuration(stack)-count>=20 && flag){
//				if(!player.capabilities.isCreativeMode  || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0){
//					player.inventory.consumeInventoryItem(InitUnsagaTools.itemBarrett.itemID);
//				}
//				this.setBarrett(stack);
//				player.worldObj.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
//
//							
//			}
//		}
//	}

	protected void reloadBarrett(ItemStack itemstack, World world, EntityPlayer entityplayer){
		if(!world.isRemote){
			if (entityplayer == null || entityplayer.capabilities.isCreativeMode) {
				itemstack.setItemDamage(0);
			} else {
				boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
				int lk = getReload(itemstack);
				if(!linfinity){
					entityplayer.inventory.consumeInventoryItem(Unsaga.items.ammo);
				}
				itemstack.setItemDamage(itemstack.getItemDamage() - 1);
			}
			this.setReload(itemstack, RELOAD_START);
		}
		entityplayer.worldObj.playSoundAtEntity(entityplayer, "fire.ignite", 1.0F, 1.0F / (entityplayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
	}
	
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		// リロード完了
		

		if(this.getReload(par1ItemStack)<=FIRE){
			this.reloadBarrett(par1ItemStack, par2World, par3EntityPlayer);
		}
		return par1ItemStack;
	}
	
	@Override
	public void registerIcons(IIconRegister par1){
		this.itemIcon = par1.registerIcon(Unsaga.DOMAIN+":"+this.iconname);
	}

	@Override
	public IIcon getItemIconForUseDuration(int par1)
	{
		return this.itemIcon;
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return this.getReload(par1ItemStack)==FIRE ? 32 : super.getMaxItemUseDuration(par1ItemStack);
    }
    
//	@Override
//    public EnumAction getItemUseAction(ItemStack par1ItemStack)
//    {
//        return getReload(par1ItemStack)==RELOAD_START ? EnumAction.none : EnumAction.bow;
//    }
	
	protected boolean canReload(ItemStack is,EntityPlayer ep){
		if (ep.capabilities.isCreativeMode) return true;
		if(ep.inventory.hasItem(Unsaga.items.ammo)) return true;
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack==null)return;
			
//				if(par3Entity instanceof EntityLivingBase){
//					EntityPlayer ep = (EntityPlayer)par3Entity;
//					if(ep.getHeldItem()!=null){
//						if(ep.getHeldItem().getItem().itemID==this.itemID){
//							ItemStack is = ep.getHeldItem();
//							//ニュートラル時
//							if(this.getReload(is)==RELOAD_START){
//								this.setReload(is,RELOAD_END);
//								ep.worldObj.playSoundAtEntity(ep, "fire.ignite", 1.0F, 1.0F / (ep.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
//
//							}
//							
//						}
//					}
//					
//				}
			
		}
	
	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.GUN;
	}


	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.materialManager.iron;
	}
}
