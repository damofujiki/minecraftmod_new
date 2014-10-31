package com.hinasch.lib.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.hinasch.lib.XYZPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemCustomEntityEgg extends Item{
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;

	protected Map<Integer,CustomEggInfo> eggMap;

	public ItemCustomEntityEgg()
	{

		this.eggMap = new HashMap();
		this.setHasSubtypes(true);
	}
	
	public void addMaping(int num,Class<?extends Entity> clazz,String name,int color1,int color2){
		EntityEggInfo eggInfo = new EntityEggInfo(0,color1,color2);
		this.eggMap.put(num, new CustomEggInfo(clazz,eggInfo,name));
		
	}

	public Entity getEntityFromClass(World world,Class<?extends Entity> clazz){
		System.out.println(clazz.getName()+"を配置しようとしています");
		try {
			Constructor cons = clazz.getConstructor(World.class);
			Object obj = cons.newInstance(world);
			Entity entity = (Entity)obj;
			return entity;
		} catch (SecurityException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}
	public String getStringFromID(int num){
		return this.eggMap.get(num).name;
	}

    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName() + this.eggMap.get(par1ItemStack.getItemDamage()).name;
    }

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo)this.eggMap.get(Integer.valueOf(par1ItemStack.getItemDamage())).eggInfo;
		return entityegginfo != null ? (par2 == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor) : 16777215;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par3World.isRemote)
		{
			return true;
		}
		else
		{
			Block block = par3World.getBlock(par4, par5, par6);
			par4 += Facing.offsetsXForSide[par7];
			par5 += Facing.offsetsYForSide[par7];
			par6 += Facing.offsetsZForSide[par7];
			double d0 = 0.0D;

			if (par7 == 1 && block.getRenderType() == 11)
			{
				d0 = 0.5D;
			}

			Entity entity = this.getEntityFromClass(par3World, this.eggMap.get(par1ItemStack.getItemDamage()).entity);//spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);
			XYZPos pos = new XYZPos((double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);
			this.spawnCreature(par3World, entity, pos);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
				{
					((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
				}

				if (!par2EntityPlayer.capabilities.isCreativeMode)
				{
					--par1ItemStack.stackSize;
				}
			}

			return true;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par2World.isRemote)
		{
			return par1ItemStack;
		}
		else
		{
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

			if (movingobjectposition == null)
			{
				return par1ItemStack;
			}
			else
			{
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;

					if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
					{
						return par1ItemStack;
					}

					if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (par2World.getBlock(i, j, k) instanceof BlockLiquid)
					{
						Entity entity = this.getEntityFromClass(par2World, this.eggMap.get(par1ItemStack.getItemDamage()).entity);//spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);
						XYZPos pos = new XYZPos((double)i, (double)j, (double)k);
						this.spawnCreature(par2World, entity, pos);
						if (entity != null)
						{
							if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
							{
								((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
							}

							if (!par3EntityPlayer.capabilities.isCreativeMode)
							{
								--par1ItemStack.stackSize;
							}
						}
					}
				}

				return par1ItemStack;
			}
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	 * Parameters: world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World par0World, Entity entity, XYZPos pos)
	{


		if (entity != null && entity instanceof EntityLivingBase)
		{
			EntityLiving entityliving = (EntityLiving)entity;
			entity.setLocationAndAngles(pos.dx, pos.dy, pos.dz, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
			entityliving.rotationYawHead = entityliving.rotationYaw;
			entityliving.renderYawOffset = entityliving.rotationYaw;
			entityliving.onSpawnWithEgg((IEntityLivingData)null);
			par0World.spawnEntityInWorld(entity);
			entityliving.playLivingSound();
		}


		return entity;

	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int par1, int par2)
	{
		return Items.spawn_egg.getIconFromDamageForRenderPass(par1, par2);
		//return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		Iterator iterator = EntityList.entityEggs.values().iterator();

		for(Iterator<Integer> ite=this.eggMap.keySet().iterator();ite.hasNext();){
			int num = ite.next();
			list.add(new ItemStack(item,1,num));
		}

	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		super.registerIcons(par1IconRegister);
		this.theIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
	}

	public static class CustomEggInfo{

		public EntityEggInfo eggInfo;
		public String name;
		public Class<?extends Entity> entity;

		public CustomEggInfo(Class<?extends Entity> clazz,EntityEggInfo egginfo,String name){
			this.entity = clazz;
			this.eggInfo = egginfo;
			this.name = name;
		}
	}
}
