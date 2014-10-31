package com.hinasch.lib;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.base.Optional;
import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class HSLibs {

	public static HSLibs instance;
	public static String JPKEY = "ja_JP";

	public static class FLAG_SETBLOCK{
		public static final int NORMAL = 3;
	}

	public static final ForgeDirection[] UPDOWN = {ForgeDirection.DOWN,ForgeDirection.UP};
	public static final ForgeDirection[] WE = {ForgeDirection.WEST,ForgeDirection.EAST};
	public static final ForgeDirection[] NS = {ForgeDirection.SOUTH,ForgeDirection.NORTH};
	
	public static void registerKeyEvent(Object event){
		// InputEvent.KeyInputEvent 
		FMLCommonHandler.instance().bus().register(event);
	}
	//read lwjql.input.Keyboard
	public static KeyBinding getNewKeyBinding(String name,int key,String modid){
		return new KeyBinding(name,key,modid);
		
	}
	
	public static List<String> getOreNames(int[] ids){
		List<String> list = new ArrayList();
		for(int id:ids){
			list.add(OreDictionary.getOreName(id));
		}
		return list;
	}
	
	public static boolean containsDirections(int side,ForgeDirection... directions){
		for(ForgeDirection dir:directions){
			if(ForgeDirection.getOrientation(side)==dir){
				return true;
			}
		}
		return false;
	}
	
	public static Item getItemFromBlock(Block block){
		ItemStack is = new ItemStack(block,1);
		System.out.println("getitemfromblock:"+is.getItem());
		return is.getItem();
	}
	public static boolean isEntityStopped(Entity entity){
		
		System.out.println(entity.posX + entity.posY + entity.posZ);
		return entity.posX + entity.posY + entity.posZ <= 0.00001D;
	}
	
	public static void sendDescriptionPacketToAllPlayer(World world,TileEntity te){
		if(!world.isRemote){
			for(Object obj:world.playerEntities){
				EntityPlayerMP ep = (EntityPlayerMP)obj;
				ep.playerNetServerHandler.sendPacket(te.getDescriptionPacket());
			}
		}
	}
	
	public static boolean notNull(Object... objs){
		for(Object obj:objs){
			if(obj==null){
				return false;
			}
		}
		return true;
	}
	public static boolean isSameTeam(EntityLivingBase owner,EntityLivingBase livingToCompare){
		if(owner==null || livingToCompare==null)return false;
		Team teamOwner = owner.getTeam();
		Team teamCompareLiving = livingToCompare.getTeam();


		if(livingToCompare instanceof EntityHorse){
			EntityHorse horse = (EntityHorse) livingToCompare;
			Unsaga.logger.log(horse.func_152119_ch());
			//UUID uuid = UUID.fromString(((EntityHorse) livingToCompare).func_152119_ch());
			//return uuid == null? false : owner.getUniqueID()==uuid;
			
		}
		if(livingToCompare.isOnSameTeam(owner)){
			return true;
		}

		return false;
		
	}
	public static <T> boolean listContains(Collection<T> c,T... elements){
		int var1 = 0;
		for(T element:elements){
			if(c.contains(element)){
				var1 += 1;
			}
		}
		if(var1>=c.size()){
			return true;
		}
		return false;
	}
	
	public static <T> boolean instanceOf(T par1,Collection<Class<? extends T>> classes){
		//int var1 = 0;
		
		for(Class cls:classes){
			//System.out.println(par1.getClass().getSimpleName()+":"+cls.getSimpleName());
			if(cls.isInstance(par1) || par1.getClass()==cls){
				
				return true;
			}
		}

		return false;
	}

	
	public static boolean isArrowInGround(EntityArrow arrow,boolean debug){
		
		Class arrowClass = arrow.getClass();
		try {
			//String fieldName = debug ? "inGround" : "field_70254_i";
			Field f = ReflectionHelper.findField(arrowClass, "field_70254_i","i","inGround");//ReflectionHelper.findField(arrowClass, "inGround","field_70254_i","i");
			
			//Field f = arrowClass.getDeclaredField("inGround");

			return f.getBoolean(arrow);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int getArrowTickInGround(EntityArrow arrow,boolean debug){
		Class arrowClass = arrow.getClass();
		try {
			//String fieldName = debug ? "ticksInGround" : "field_70252_j";
			Field f = ReflectionHelper.findField(arrowClass, "ticksInGround","field_70252_j","j");
			//Field f = arrowClass.getDeclaredField("ticksInGround");

			return f.getInt(arrow);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	
	public static boolean isOreBlock(PairID blockdata){
		if(blockdata.getBlockObject() instanceof BlockOre ){
			return true;
		}
		if(blockdata.getBlockObject()==Blocks.redstone_ore || blockdata.getBlockObject()==Blocks.lit_redstone_ore ){
			return true;
		}
		ItemStack is = new ItemStack(blockdata.getBlockObject(),1,blockdata.getMeta());
		if(OreDictionary.getOreName(OreDictionary.getOreID(is)).contains("ore")){
			return true;
		}
		return false;
	}

	public static boolean canBreakAndEffectiveBlock(World world,EntityPlayer ep,String toolclass,XYZPos pos){
		PairID blockdata = HSLibs.getBlockDatas(world, pos);
		int harvestLevel = blockdata.getBlockObject().getHarvestLevel(blockdata.getMeta());
		if(ep.getHeldItem()==null){
			return false;
		}
		
		int toolHarvestLevel = ep.getHeldItem().getItem().getHarvestLevel(ep.getHeldItem(), toolclass);
		//Unsaga.debug(blockdata.toString());
		//Unsaga.debug("harvestlevel:"+harvestLevel+" toolHArvestLevel:"+toolHarvestLevel);
		//Unsaga.debug(toolclass+"effective:"+blockdata.blockObj.isToolEffective(toolclass, blockdata.metadata));
		boolean flag1 = harvestLevel<=toolHarvestLevel;
		boolean flag2 = blockdata.getBlockObject().isToolEffective(toolclass, blockdata.getMeta());
		boolean flag3 = blockdata.getBlockObject().getBlockHardness(world, pos.x,pos.y,pos.z)>0;
		if(blockdata.getBlockObject()==Blocks.redstone_ore || blockdata.getBlockObject()==Blocks.lit_redstone_ore ){
			flag2 = true;
		}
		return flag1 && flag2 && flag3;
	}
	public static PairID getBlockDatas(World world,XYZPos pos){
		return new PairID(world.getBlock(pos.x, pos.y, pos.z),world.getBlockMetadata(pos.x, pos.y, pos.z));
	}
	public static float getEntityAttackDamage(EntityLivingBase living){
		return (float)living.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
	}

	public static boolean isHardBlock(Block block){
		if(block==Blocks.bedrock){
			return true;
		}
		if(block==Blocks.obsidian){
			return true;
		}
		return false;
	}



	public static EntityDragonPart getEntityPartFromEntityDragon(World world,EntityDragon enemy){
		EntityDragonPart part = null;
		AxisAlignedBB bb = enemy.boundingBox.expand(3.0D, 3.0D, 3.0D);
		List<EntityDragonPart> list = world.getEntitiesWithinAABB(EntityDragonPart.class, bb);
		if(!list.isEmpty() && list!=null){
			part = list.get(0);
		}
		return part;
	}

	public static boolean isServer(World world){
		return !world.isRemote;
	}
	public static boolean checkAroundMaterial(World world,XYZPos pos,Material material){
		if(world.getBlock(pos.x-1, pos.y, pos.z).getMaterial()==material){
			return true;
		}
		if(world.getBlock(pos.x+1, pos.y, pos.z).getMaterial()==material){
			return true;
		}
		if(world.getBlock(pos.x, pos.y, pos.z-1).getMaterial()==material){
			return true;
		}
		if(world.getBlock(pos.x, pos.y, pos.z+1).getMaterial()==material){
			return true;
		}
		if(world.getBlock(pos.x, pos.y+1, pos.z+1).getMaterial()==material){
			return true;
		}
		if(world.getBlock(pos.x, pos.y-1, pos.z+1).getMaterial()==material){
			return true;
		}
		return false;
	}

//	public static String getToolMaterialNameFromTool(Item par1){
//		if(par1 instanceof ItemSword){
//			return ((ItemSword)par1).getToolMaterialName();
//		}
//		if(par1 instanceof ItemTool){
//			return ((ItemTool)par1).getToolMaterialName();
//		}
//		if(par1 instanceof ItemArmor){
//			return ((ItemArmor)par1).getArmorMaterial().toString();
//		}
//		if(par1 instanceof ItemHoe){
//			return ((ItemHoe)par1).getToolMaterialName();
//		}
//		return null;
//	}

	public static boolean isEntityLittleMaidAvatar(EntityLivingBase entity){
		if(entity==null)return false;
		String clsname = entity.getClass().getSimpleName();
		if(clsname.equals("LMM_EntityLittleMaidAvatar")){
			HSLibs.log("entity is LMMAVATAR",true);
			return true;
		}
		HSLibs.log("entity is not LMMAVATAR",true);
		HSLibs.log(clsname,true);
		return false;
	}

	public static Optional<IExtendedEntityProperties> getExtendedData(String key,Entity target){
		if(target.getExtendedProperties(key)!=null){
			return Optional.of(target.getExtendedProperties(key));
		}
		return Optional.absent();
	}


	public static boolean isEntityLittleMaidAndFortune(EntityLiving entity){
		if(isEntityLittleMaidAvatar(entity)){
			if(entity.getRNG().nextInt(3)==0){
				return true;
			}
		}
		return false;
	}

	public static EntityLiving getLMMFromAvatar(EntityLivingBase entity){
		if(entity==null)return null;
			Class avatarcls = entity.getClass();
			try {
				Field lmmfield = avatarcls.getDeclaredField("avatar");
				try {
					EntityLiving el = (EntityLiving)lmmfield.get(entity);
					return el;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}
	public static boolean isSide(int side){

		if(side!=0 && side!=1){
			return true;
		}
		return false;
	}


	public static int exceptZero(int par1){

		if(par1==0){
			return 1;
		}


		return par1;

	}

	public static float exceptZero(float par1,float cut){
		if(par1==0){
			return cut;
		}
		return par1;
	}

	public static XYZPos getSideHitPos(XYZPos xyz,MovingObjectPosition mop){
		XYZPos newxyz = xyz;

		if (mop.sideHit == 0)
		{
			newxyz.y--;
		}

		if (mop.sideHit == 1)
		{
			newxyz.y++;
		}

		if (mop.sideHit == 2)
		{
			newxyz.z--;
		}

		if (mop.sideHit == 3)
		{
			newxyz.z++;
		}

		if (mop.sideHit == 4)
		{
			newxyz.x--;
		}

		if (mop.sideHit == 5)
		{
			newxyz.x++;
		}
		return newxyz;
	}



	public static void log(String par1,boolean debug){
		if(debug){
			System.out.println(par1);

		}
	}
	

	
	
//	public static void spawnEntity(World world,Entity entity,XYZPos xyz){
//		entity.setPosition(xyz.dx, xyz.dy, xyz.dz);
//		if(!world.isRemote){
//			world.spawnEntityInWorld(entity);
//		}
//		
//	}

	

	//	public static int getDamageFromColorName(String var1){
	//		int result = -1;
	//		for(int i=0;i<ItemDye.dyeColorNames.length;i++){
	//			if(ItemDye.dyeColorNames[i].equals(var1)){
	//				result = i;
	//			}
	//		}
	//		System.out.println(result);
	//		return result;
	//	}

	public static AxisAlignedBB getBounding(XYZPos xyz,double range,double rangeY){
		return getBounding(xyz.dx,xyz.dy,xyz.dz,range,rangeY);
	}
	public static AxisAlignedBB getBounding(int x,int y,int z,double range,double rangeY){


		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)x-range,(double)y-rangeY , (double)z-range,
				(double)x+range, (double)y+rangeY, (double)z+range);
		return aabb;
	}

	public static AxisAlignedBB getBounding(double x,double y,double z,double range,double rangeY){


		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)x-range,(double)y-rangeY , (double)z-range,
				(double)x+range, (double)y+rangeY, (double)z+range);
		return aabb;
	}

	public static boolean isEnemy(Entity par1,Entity player){
		if(par1!=player && !(par1 instanceof EntityTameable) && !(par1 instanceof INpc)){
			if(par1 instanceof EntityLivingBase){
				return true;
			}

		}
		return false;
	}

	public static boolean isWood(String id){
		boolean var1 = false;
		//		if(id==Blocks.log){
		//			var1 = true;
		//		}
		int[] oreids = OreDictionary.getOreIDs(new ItemStack(Block.getBlockFromName(id)));
		for(Integer oreid:oreids){
			if(OreDictionary.getOreName(oreid)!=null){
				if(OreDictionary.getOreName(oreid).equals("logWood")){
					var1 = true;
				}
			}
		}

		return var1;
	}

	public static void registerEvent(Object par1){
		MinecraftForge.EVENT_BUS.register(par1);
	}

//	public static boolean isEnemy(EntityLivingBase en){
//		if(en instanceof IMob || en instanceof EntityMob){
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean isEnemy(EntityPlayer ep,EntityLivingBase en){
//		if(ep == en)return false;
//		if(en instanceof IMob || en instanceof EntityMob){
//			return true;
//		}
//		return false;
//	}

	public static String getCurrentLang(){
		return Minecraft.getMinecraft().gameSettings.language;
	}

}
