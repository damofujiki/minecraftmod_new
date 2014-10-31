package com.hinasch.unlsaga.util;

import net.minecraft.entity.player.EntityPlayer;

import com.hinasch.lib.ChatHandler;

//@author SackCastellon
//from https://github.com/SackCastellon/SKC-Core/blob/master/java/SackCastellon/core/handler/ChatMessageHandler.java
public class ChatMessageHandler {

//    private static final IChatComponent SkcChatComponent = createSckChatComponent("["+Unsaga.MODID+"]");
//
//    public static void iCommandSenderReply(ICommandSender player, String message) {
//        sendChatToPlayer((EntityPlayer)player, message);
//    }
//
//    private static IChatComponent createSckChatComponent(String string) {
//        ChatComponentText Component = new ChatComponentText(string);
//          return Component;
//    }
//
//    public static IChatComponent createChatComponent(String message) {
//        ChatComponentText component = new ChatComponentText(message);
//        return component; //SkcChatComponent.appendSibling(component);
//    }

    public static void sendChatToPlayer(EntityPlayer player, String message) {
        ChatHandler.sendChatToPlayer(player, "[Unsaga Mod]"+message);
    }

//    public static void broadcastMessageToPlayers(String message){
//        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(createChatComponent(message));
//    }
}
