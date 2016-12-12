package org.uni.illuxplain;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.uni.illuxplain.xmpp.services.PrivateChat;
import org.uni.illuxplain.xmpp.services.XmppManager;

import android.graphics.Bitmap;


public class GlobalApplication {
	private static GlobalApplication globalApp;
	public static String username;
	public static Bitmap bitmap;
	public static final String APPLICATION_FOLDER = "illuxplain";
	
//	public static final String SERVER_NAME = "localhost";
//	public static final String SERVER_IP = "192.168.1.3";
	
	public static final String SERVER_NAME = "illuxplain.cloudapp.net";
	public static final String SERVER_IP = "40.84.199.55";
	
//	public static final String SERVER_NAME = "illuxplain.p1.im";
//	public static final String SERVER_IP ="184.73.173.246";
	public static final int PORT  = 5222;
	
	public static String userURL;
	private static XmppManager manager;
	
	private static Object obj = new Object();
	
	
	private static PrivateChat chat;
	
	private GlobalApplication(){
		userURL = username+"@"+SERVER_NAME;
	}
	
	public static GlobalApplication getInstance() {
		if (globalApp == null) {
			
			synchronized (obj) {
				if(globalApp == null){
					return new GlobalApplication();
				}
			}
		} 
		return globalApp;
		
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public void setXmppManager(XmppManager manager){
		this.manager = manager;
	}
	
	public XmppManager getManager(){
		
		if(XmppManager.conn.isConnected() || XmppManager.conn != null){
			return manager;
		}
		
		manager.init();
		try {
			manager.getLogin();
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manager;
	}
	
	
	public void setPrivateChat(PrivateChat chat){
		this.chat = chat;
	}
	
	public PrivateChat getPrivateChat(){
		if(chat == null){
			return new PrivateChat();
		}
		return chat;
	}
	
}
