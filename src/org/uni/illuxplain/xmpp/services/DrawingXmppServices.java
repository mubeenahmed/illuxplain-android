package org.uni.illuxplain.xmpp.services;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.Utils;
import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.canvas.SurfaceViewCanvas;
import org.uni.illuxplain.group.chat.GroupChat;
import org.uni.illuxplain.xmpp.services.XmppManager.DrawingReceiver;

import android.content.Context;

public class DrawingXmppServices {
	
	
	public interface OnDrawingRecieved{
		public void onRecevieDrawing(String from , String drawing);
	}
	
	private static boolean isSender = false;
	
	DrawingReceiver receiver;
	ChatManager chatManager;
	public static MultiUserChat muc;
	private OnDrawingRecieved received;

	public DrawingXmppServices(Context context ,SurfaceViewCanvas surfaceView) {
		receiver = surfaceView;
		received = (OnDrawingRecieved) context;

		if (GroupChat.isAccept) {
			muc = GroupChat.iMUC;
			muc.addMessageListener(new MyDrawingChatListener());
		} else {
			muc = GroupChat.multiChat;
			muc.addMessageListener(new MyDrawingChatListener());
		}

	}

	public void sendDrawingData(String drawingData) {
		try {
			isSender = true;
			muc.sendMessage(drawingData);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}

	}

	private class MyDrawingChatListener implements MessageListener {

		@Override
		public void processMessage(Message message) {
			String from = message.getFrom();
			String drawing = message.getBody();
			String nick = Utils.ManyToManyStriper(from);
			boolean isSameSender = Utils.ManyToManyStriper(from).equals(GlobalApplication.username); //Utils.conferenceNameStriper(from).equals(GlobalApplication.username);
			if (drawing == null || isSameSender) {
				return;
			}
			received.onRecevieDrawing(nick, drawing);

		}

	}
}
