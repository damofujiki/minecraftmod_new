package com.hinasch.unlsaga.block;

import java.util.Random;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockChestUnsagaNew extends BlockContainer{

	protected Random rand;

	public BlockChestUnsagaNew() {
		super(Material.wood);
		setHardness(3.0F);
		this.rand = new Random();
		setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block i1, int i2)
	{
		TileEntityChestUnsagaNew tileentitychest = (TileEntityChestUnsagaNew) world.getTileEntity(i, j, k);

		if (tileentitychest != null)
		{

			//tileentitychest.removeAdornments();
			if(!tileentitychest.hasChestOpened()){
				tileentitychest.reductionChestLevel();
				tileentitychest.obtainItem();
			}

			//dropContent(0, tileentitychest, world, tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord);
		}
		super.breakBlock(world, i, j, k, i1, i2);
	}

	
	//タイルエンティティが作られた時に呼ばれる。ロード時の復元時にも呼ばれるので、ここでイニシャライズをやらないほうがいい
	@Override
	public TileEntity createNewTileEntity(World w, int i)
	{
		TileEntityChestUnsagaNew chest = new TileEntityChestUnsagaNew();

		//		if(!chest.hasInitialized()){
		//			chest.initChest(w);
		//		}
		return chest;
	}

	//	@Override
	//	public TileEntity createTileEntity(World world, int metadata)
	//	{
	//		TileEntityChestUnsaga chest = new TileEntityChestUnsaga();
	//		chest.init(world);
	//		return chest;
	//	}
	//最初に配置された時のメソッド（最初のイニシャライズとかはここがよい）
	@Override
    public void onBlockAdded(World world, int i, int j, int k) {
    	
		TileEntity te = world.getTileEntity(i, j, k);
		if(te instanceof TileEntityChestUnsagaNew){
			((TileEntityChestUnsagaNew) te).initChest(world);
		}
    }
	public void dropContent(int newSize, IInventory chest, World world, int xCoord, int yCoord, int zCoord)
	{
		for (int l = newSize; l < chest.getSizeInventory(); l++)
		{
			ItemStack itemstack = chest.getStackInSlot(l);
			if (itemstack == null)
			{
				continue;
			}
			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;
			while (itemstack.stackSize > 0)
			{
				int i1 = rand.nextInt(21) + 10;
				if (i1 > itemstack.stackSize)
				{
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				EntityItem entityitem = new EntityItem(world, (float) xCoord + f, (float) yCoord + (newSize > 0 ? 1 : 0) + f1, (float) zCoord + f2,
						new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
				float f3 = 0.05F;
				entityitem.motionX = (float) rand.nextGaussian() * f3;
				entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) rand.nextGaussian() * f3;
				if (itemstack.hasTagCompound())
				{
					entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
				}
				world.spawnEntityInWorld(entityitem);
			}
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}


	@Override
	public int getRenderType()
	{
		return 22;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}



	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3)
	{
		TileEntity te = world.getTileEntity(i, j, k);



		if (te == null || !(te instanceof TileEntityChestUnsagaNew))
		{
			return true;
		}

		if (world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN))
		{
			return true;
		}

		if (world.isRemote)
		{
			return true;
		}

		TileEntityChestUnsagaNew tc = (TileEntityChestUnsagaNew)te;
		if(tc.hasChestOpened()){
			return true;
		}
		Unsaga.debug("同期します");
		

		Unsaga.debug("Chest Status>Level:"+tc.getChestLevel()+" Lock;"+!tc.isUnlocked()+" MagicLock:"+tc.isMagicLocked()+" Trap:"+tc.isTrapOccured()+" Trap Number:"+tc.getTrapNumber()+" Slime Trap:"+tc.hasOccuredSlimeTrap());
		Packet pecketTE = tc.getDescriptionPacket();
		((EntityPlayerMP)player).playerNetServerHandler.sendPacket(pecketTE);
		//PacketSyncChest ps = new PacketSyncChest(new XYZPos(par2,par3,par4),chestlevel);
		//Unsaga.packetPipeline.sendTo(ps, (EntityPlayerMP) par5EntityPlayer);
		Unsaga.debug("パケット送ります");

		PacketGuiOpen pg = new PacketGuiOpen(Unsaga.guiNumber.CHEST,new XYZPos(i,j,k));
		Unsaga.packetPipeline.sendToServer(pg);
		//PacketDispatcher.sendPacketToPlayer(PacketHandler.getChestSyncPacket(chestlevel, par2, par3, par4,false,var10.trapOccured,var10.unlocked,var10.defused,var10.magicLock,var10.hasItemSet), (Player) par5EntityPlayer);
		//PacketDispatcher.sendPacketToServer(PacketHandler.getChestGuiPacket(par2,par3,par4));


		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("planks_oak");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{

		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

	}
}
