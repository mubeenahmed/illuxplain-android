package org.uni.illuxplain.xmpp.services;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.uni.illuxplain.canvas.SurfaceViewCanvas;
import org.uni.illuxplain.xmpp.services.XmppManager.DrawingReceiver;

public class NewCanvasStarter {
//	ChatManager chatManager = XmppManager.conn.getChatManager();
//	
//	String BUDDY = XmppManager.BUDDY;
//	Chat chat;
//	DrawingReceiver receiver;
//	
//	public NewCanvasStarter(SurfaceViewCanvas surfaceView) {
//		receiver = surfaceView;
//	}
//	
//	public void sendDrawingData(String drawingData, String buddyId) {
//		buddyId = BUDDY;
//		chat = chatManager.createChat(buddyId, new MyChatListener());
//		chat.addMessageListener(new MyChatListener());
//		try {
//			chat.sendMessage(drawingData);
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private class MyChatListener implements MessageListener {
//		@Override
//		public void processMessage(Chat chat, Message message) {
//			String from = message.getFrom();
//			String drawing = message.getBody();
//			if (drawing == null) {
//				return;
//			}
//			receiver.onStartNewCanvas();
//		}
//	}
}
