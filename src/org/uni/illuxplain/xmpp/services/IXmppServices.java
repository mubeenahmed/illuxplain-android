package org.uni.illuxplain.xmpp.services;

public interface IXmppServices {
	public void sendDrawingData(String drawingData, String buddyId);
	public void sendMessage(String mMessage,String buddyID);
}
