package com.hinasch.unlsagamagic.item;

import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.network.packet.PacketGuiOpen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlender extends Item{

	public ItemBlender() {
		super();
		this.setTextureName(Unsaga.DOMAIN+":book_blender");
		
	}

    
    
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		PacketGuiOpen pg = new PacketGuiOpen(Unsaga.guiNumber.BLENDER);
		Unsaga.packetPipeline.sendToServer(pg);
		//PacketDispatcher.sendPacketToServer(pg.getPacket());

		return par1ItemStack;
	}
}
