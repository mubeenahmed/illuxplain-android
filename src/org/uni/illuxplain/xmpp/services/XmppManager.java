package org.uni.illuxplain.xmpp.services;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import android.graphics.Bitmap;

public class XmppManager {

	public interface DrawingReceiver {
		public void onReceiveDrawingMessage(String from,String drawing);
		public void onImageReceive(Bitmap bitmap);
		public void onStartNewCanvas();
		public void onFileReceived(String fileName);
	}

	public interface MessageReceiver {
		public void onReceiveMessage(String from, String message);
	}

	private static final int packetAlive = 500;
	private static String serverName;
	private static String serverIp;
	private static int mport;

	public static String username;
	public static String password;

	public DrawingReceiver receiver;
	public static String resource;
	public static XMPPTCPConnectionConfiguration config;
	public static XMPPTCPConnection conn;
	

	public XmppManager() {

	}

	public XmppManager(String Ip, String name, int port) {
		serverIp = Ip;
		serverName = name;
		mport = port;

	}
	
	public void init() {
		
		SASLMechanism mechanism = new SASLDigestMD5Mechanism();
		SASLAuthentication.registerSASLMechanism(mechanism);
//		SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");  
//		SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");
		
		SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
		SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
		
		config = XMPPTCPConnectionConfiguration.builder()
				.setServiceName(serverName).setHost(serverIp)
				.setSecurityMode(SecurityMode.disabled).setPort(mport)
				.setCompressionEnabled(false).build();
		conn = new XMPPTCPConnection(config);
		try {
			conn.connect();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	public void getLogin() throws XMPPException, SmackException, IOException{
			conn.login(username,password);
	}

	public void setPresenceStatus() {

	}

	public void destroy() {
		conn.disconnect();
	}
}
