package com.hinasch.unlsaga.client;
//package hinasch.mods.unlsaga.client;
//
//import hinasch.mods.unlsaga.DebugUnsaga;
//import hinasch.mods.unlsaga.Unsaga;
//import hinasch.mods.unlsaga.network.PacketGuiOpen;
//import hinasch.mods.unlsaga.network.PacketHandler;
//
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.EnumSet;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityClientPlayerMP;
//import net.minecraft.client.settings.KeyBinding;
//import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
//import cpw.mods.fml.common.TickType;
//import cpw.mods.fml.common.network.PacketDispatcher;
//
//public class KeyHandlerTest extends KeyHandler{
//
//	public DebugUnsaga debugdata ;
//	static int zkey = 44;
//	static int xkey = 45;
//	static int ckey = 46;
//	static int vkey = 47;
//	static int bkey = 48;
//	static int nkey = 49;
//	static int mkey = 50;
//	static int kammakey = 51;
//	static KeyBinding keyBinding0 = new KeyBinding("increaseKey", zkey);
//	static KeyBinding keyBinding1 = new KeyBinding("decreaseKey", xkey);
//	static KeyBinding keyBinding2 = new KeyBinding("increaseKey2", ckey);
//	static KeyBinding keyBinding3 = new KeyBinding("decreaseKey2", vkey);
//	static KeyBinding keyBinding4 = new KeyBinding("increaseKey3", bkey);
//	static KeyBinding keyBinding5 = new KeyBinding("decreaseKey3", nkey);
//	static KeyBinding keyBinding6 = new KeyBinding("increaseKey4", mkey);
//	static KeyBinding keyBinding7 = new KeyBinding("decreaseKey4", kammakey);
//	
//	
//	public KeyHandlerTest() {
//		super(new KeyBinding[]{keyBinding0,keyBinding1,keyBinding2,keyBinding3,keyBinding4,keyBinding5,keyBinding6,
//				keyBinding7},
//				new boolean[]{true,true,false,true,true,true,true,true});
//		this.debugdata = Unsaga.proxy.getDebugUnsaga().get();
//		// TODO Auto-generated constructor stub
//		
//	}
//
//	@Override
//	public String getLabel() {
//		// TODO Auto-generated method stub
//		return "TestKeyBindings";
//	}
//
//	@Override
//	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
//			boolean tickEnd, boolean isRepeat) {
//		if(Unsaga.debug.get()){
//		      if (kb.keyCode == zkey){
//
//		    	  debugdata.increaseFloat(debugdata.index,debugdata.incr);
//		       }
//		      if (kb.keyCode == xkey){
//		    	  debugdata.decreaseFloat(debugdata.index,debugdata.incr);
//
//		       }
//		      if (kb.keyCode == ckey){
//
//		    	  if(tickEnd)debugdata.nextIndex();
//		    	  
//		    	  
//
//		       }
//		      if (kb.keyCode == vkey){
//
//		    	  debugdata.increaseFloat(debugdata.index,1.0F);
//		       }
//		      if (kb.keyCode == bkey){
//		    	  debugdata.decreaseFloat(debugdata.index,1.0F);
//		       }
//		      if (kb.keyCode == nkey){
//
//		       }
//		      if(kb.keyCode == mkey && tickEnd){
//
//		    	  EntityClientPlayerMP clientPlayer = (EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer;
//		    	  if(clientPlayer.isSneaking()){
//		    		  PacketDispatcher.sendPacketToServer(PacketHandler.getAbilityPacket());
//		    	  }
//		    	
//		    	   
//		      }
//		}
//
//	      if(kb.keyCode == kammakey && tickEnd){
//	    	  
//	    	  EntityClientPlayerMP clientPlayer = (EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer;
//
//	    	  if(Minecraft.getMinecraft().currentScreen !=null)return;
//	    	  //clientPlayer.openGui(Unsaga.instance, Unsaga.GuiEquipment, clientPlayer.worldObj,(int)clientPlayer.posX, (int)clientPlayer.posY, (int)clientPlayer.posZ);
//	    	  PacketGuiOpen pg = new PacketGuiOpen(Unsaga.GuiEquipment);
//	    	  PacketDispatcher.sendPacketToServer(pg.getPacket());
//	    	  //FMLClientHandler.instance().displayGuiScreen(clientPlayer, new GuiEquipment(clientPlayer));
//	      }
//	}
//
//	@Override
//	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
//	      if (kb.keyCode == ckey){
//	    	  
//	    		 
//	    	  
//	    	  
//
//	       }
//		
//	}
//
//	@Override
//	public EnumSet<TickType> ticks() {
//		// TODO Auto-generated method stub
//		return EnumSet.of(TickType.CLIENT);
//	}
//
//	public void writePacketData(DataOutputStream dos,EntityClientPlayerMP clientPlayer) {
//		// TODO 自動生成されたメソッド・スタブ
//		try {
//			dos.writeByte((byte)Unsaga.GuiEquipment);
//		} catch (IOException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//	}
//
//}
