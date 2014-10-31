package com.hinasch.unlsaga.network.packet;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.ClientHelper;
import com.hinasch.lib.Statics;
import com.hinasch.lib.XYZPos;
import com.hinasch.lib.base.EntityFXBase;
import com.hinasch.lib.net.PacketUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.ParticlesUnsaga;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketParticle implements IMessage{
	
	public static final int PARTICLE_DENS = 0x00;
	public static final int PARTICLE_TO_POS = 0x01;
	
	
	protected int packetId;
	protected int particleNumber;
	protected int entityId;
	protected short dens;
	
	protected XYZPos xyz;
	
	public PacketParticle(){
		
	}
	
	public PacketParticle(int particlenum,int entityid,int dens){
		this.packetId = PARTICLE_DENS;
		this.particleNumber = particlenum;
		this.entityId = entityid;
		this.dens = (short)dens;
	}
	
	public PacketParticle(XYZPos pos,int particlenum,int dens){
		this.packetId = PARTICLE_TO_POS;
		
		this.particleNumber = particlenum;
		this.xyz = pos;
		this.dens = (short)dens;
		
	}
	
	public int getMode(){
		return this.packetId;
	}
	
	public int getParticleNumber(){
		return this.particleNumber;
	}
	
	public XYZPos getPos(){
		return this.xyz;
	}
	
	public int getDensity(){
		return this.dens;
	}


	public int getEntityID(){
		return this.entityId;
	}
	

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.packetId = buffer.readInt();
		this.particleNumber = buffer.readInt();
		this.dens = buffer.readShort();
		switch(this.packetId){
		case PARTICLE_DENS:
			this.entityId = buffer.readInt();
			break;
		case PARTICLE_TO_POS:
			this.xyz = PacketUtil.bufferToXYZPos(buffer);
			break;
		
		
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(packetId);
		buffer.writeInt(particleNumber);
		buffer.writeShort(dens);
		switch(this.packetId){
		case PARTICLE_DENS:
			buffer.writeInt(entityId);
			break;
		case PARTICLE_TO_POS:
			PacketUtil.XYZPosToPacket(buffer, this.xyz);
			break;
		
		
		}
		
	}

	public static class PacketParticleHandler implements IMessageHandler<PacketParticle,IMessage>{

		@Override
		public IMessage onMessage(PacketParticle message, MessageContext ctx) {
			EntityPlayer player = ClientHelper.getClientPlayer();
			if(message.getMode()==PARTICLE_DENS){

				String particlestr = "";
				if(message.getParticleNumber()>=200){
					particlestr = message.getParticleNumber()==200 ? "stone" : message.getParticleNumber()==201 ? "leave" : message.getParticleNumber()==202 ? "bubble" : "none";
				}else{
					particlestr = Statics.particleMap.get(message.getParticleNumber());
				}
				
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				Entity en = ep.worldObj.getEntityByID(message.getEntityID());
				if(en!=null){
					XYZPos epo = XYZPos.entityPosToXYZ(en);
					for (int i = 0; i < message.getDensity(); ++i)
					{
						if(message.getParticleNumber()>=200){
							Unsaga.debug("出てますパーティ");
							Random par5Random = ep.getRNG();
							double r = 0.8D + par5Random.nextDouble();
							double t = par5Random.nextDouble() * 2 * Math.PI;
				 
							double d0 = en.posX + 0.5D + r * Math.sin(t);
							double d1 = en.posY + 0.0D + par5Random.nextDouble();
							double d2 = en.posZ + 0.5D + r * Math.cos(t);

							double d3 = Math.sin(t) / 64.0D;
							double d4 = 0.03D;
							double d5 = Math.cos(t) / 64.0D;
							
							EntityFXBase entityFX = new EntityFXBase(ep.worldObj,d0,d1,d2,d3,d4,d5);
							entityFX.setParticleIcon(ParticlesUnsaga.getInstance().getIcon(particlestr));
							//Unsaga.debug(particlestr);
							FMLClientHandler.instance().getClient().effectRenderer.addEffect(entityFX);
						}else{
							boolean flag = false;

							if(particlestr.equals(Statics.particleBubble) && !flag){
								float f4 = 0.25F;
								flag = true;
								ep.worldObj.spawnParticle(Statics.particleBubble, en.posX - en.motionX * (double)f4, en.posY - en.motionY * (double)f4, en.posZ - en.motionZ * (double)f4, en.motionX, en.motionY, en.motionZ);
							}
							if(particlestr.equals(Statics.particleReddust) && !flag){
								flag = true;
								ep.worldObj.spawnParticle(particlestr, en.posX, en.posY + ep.worldObj.rand.nextDouble() * 2.0D, en.posZ, 0.0D,0.0D,0.0D);
							}
							if(particlestr.equals(Statics.particleExplode) && !flag){
								flag = true;
								ep.worldObj.spawnParticle(particlestr, en.posX, en.posY, en.posZ, 0.0D,0.0D,0.0D);
							}
							if(!flag){
								flag = true;
								ep.worldObj.spawnParticle(particlestr, en.posX, en.posY + ep.worldObj.rand.nextDouble() * 2.0D, en.posZ, ep.worldObj.rand.nextGaussian()*2, 0.0D, ep.worldObj.rand.nextGaussian()*2);
							}
								
							
							
						}
							
						
					}
				}

			}
			if(message.getMode()==PARTICLE_TO_POS){
				String particlestr = Statics.particleMap.get(message.getParticleNumber());
				EntityClientPlayerMP ep = (EntityClientPlayerMP)player;
				for (int i = 0; i < message.getDensity(); ++i)
				{
					ep.worldObj.spawnParticle(particlestr, (double)message.getPos().x, (double)message.getPos().y + ep.worldObj.rand.nextDouble() * 2.0D, (double)message.getPos().z, ep.worldObj.rand.nextGaussian()*2, 0.0D, ep.worldObj.rand.nextGaussian()*2);
				}


			}
			return null;
		}
		
	}
}
