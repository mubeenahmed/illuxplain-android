package org.uni.illuxplain.xmpp.services;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.uni.illuxplain.GlobalApplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class PrivateChat {

	private GlobalApplication application = GlobalApplication.getInstance();
	private Chat chat;
	private ChatManager manager;
	public String msg;
	public String from;
	private Context context;
	
	private XmppManager xmppManager;
	
	public PrivateChat() {
		xmppManager = application.getManager();
		manager = ChatManager.getInstanceFor(XmppManager.conn);
		init();
	}

	public PrivateChat(Context context) {
		xmppManager = application.getManager();
		manager = ChatManager.getInstanceFor(XmppManager.conn);
		this.context = context;
		init();
	}
	
	private void init(){
		manager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean value) {
				if (!value) {
					chat.addMessageListener(new PrivateMessageListener());
				}
			}
		});
	}


	public void sendMessage(String jid, String message) {
		chat = manager.createChat(jid+"@"+GlobalApplication.SERVER_NAME);
		try {
			chat.sendMessage(message);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	private class PrivateMessageListener implements ChatMessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			msg = message.getBody();
			from = message.getFrom();
			Intent intent = new Intent("message");

			intent.putExtra("msg", msg);
			intent.putExtra("from", from);
			if (msg == null) {
				return;
			}		
				
			}
			
		}
	}

