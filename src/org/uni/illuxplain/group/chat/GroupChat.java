package org.uni.illuxplain.group.chat;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.address.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.commands.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.iqprivate.PrivateDataManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.packet.GroupChatInvitation;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.muc.packet.MUCUser.Decline;
import org.jivesoftware.smackx.muc.provider.MUCAdminProvider;
import org.jivesoftware.smackx.muc.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.muc.provider.MUCUserProvider;
import org.jivesoftware.smackx.offline.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.offline.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.privacy.provider.PrivacyProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.sharedgroups.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.si.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.provider.DataFormProvider;
import org.jivesoftware.smackx.xhtmlim.provider.XHTMLExtensionProvider;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.opentok.OpenTokConfig;
import org.uni.illuxplain.xmpp.services.XmppManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GroupChat {

	public static MultiUserChatManager multiUserManager;
	public static MultiUserChat multiChat;
	public static final int LENGTH_OF_ROOM = 10;

	public static String roomName;
//	public static String subDomain = "conference.illuxplain.p1.im";
//	public static String subDomain = "conference.illuxplain.cloudapp.net";
	public static String subDomain = "conference.localhost";
	public static List<String> owner = new ArrayList<String>();
	public static List<String> users = new ArrayList<String>();

	public static MultiUserChat iMUC;
	private Context context;
	private Handler handler;
	
	public static final String TOKEN_SERVICE = "http://illuxplain-mubeen.rhcloud.com/webservice/illuxplain/opentok/token";
	public static String mSessionID;
	
	public static boolean isAccept = false;

	public GroupChat(Context context, Handler handler) {
		this.context = context;
		
		multiUserManager = MultiUserChatManager.getInstanceFor(XmppManager.conn);
		this.handler = handler;
		listenerInit();
	}

	private void listenerInit() {
		multiUserManager.addInvitationListener(new InvitationListener() {
			@Override
			public void invitationReceived(XMPPConnection conn,
					final MultiUserChat muc, final String inviter,
					final String reason, final String password, final Message message) {

				handler.post(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Incomming Request From: " + inviter);
						builder.setMessage("Reason: " + reason);
						builder.setPositiveButton("OK", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								isAccept = true;
								try {
									
									iMUC = muc;
									Message newMessage = message;
									mSessionID = newMessage.getBody();
									getToken(mSessionID);
									
									muc.join(GlobalApplication.username,password);
									Intent intent = new Intent(context,CanvasActivity.class);
									context.startActivity(intent);

								} catch (XMPPErrorException e) {
									e.printStackTrace();
								} catch (SmackException e) {
									e.printStackTrace();
								}
							}

							private void getToken(final String mSessionID) {
								RequestQueue queue = Volley.newRequestQueue(context);
								StringRequest req = new StringRequest(TOKEN_SERVICE+"?session_id="+mSessionID, new Listener<String>() {

									@Override
									public void onResponse(String str) {
										OpenTokConfig.sessionId = mSessionID;
										OpenTokConfig.tokenKey = str;
									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(VolleyError error) {
										Toast.makeText(context, "Error In Server", Toast.LENGTH_LONG).show();
									}
								});
								queue.add(req);
							}
						});
						builder.setNegativeButton("No", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								MUCUser user = new MUCUser();
								user.setDecline(new Decline());
							}

						});
						AlertDialog alert = builder.create();
						alert.show();
					}
				});
			}
		});

	}

	public void getGroupChatDomain() {

		configure(new ProviderManager());
		Date date = new Date();
		long time = date.getTime();
		roomName = GlobalApplication.username + "_" + time + "@" + subDomain;
		multiChat = multiUserManager.getMultiUserChat(roomName);
		try {
			// Create a chat room
			multiChat.createOrJoin(GlobalApplication.username); // RoomName room
																// name
			// To obtain the chat room configuration form
			Form form = multiChat.getConfigurationForm();
			// Create a new form to submit the original form according to the.
			Form submitForm = form.createAnswerForm();
			// To submit the form to add a default reply
			List<FormField> formField = form.getFields();
			for (Iterator<FormField> fields = formField.iterator(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.Type.hidden.equals(field.getType())
						&& field.getVariable() != null) {
					// Set default values for an answer
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// Set the chat room of the new owner
			owner.add(XmppManager.conn.getUser());
			try{
				submitForm.setAnswer("muc#roomconfig_roomowners", owner);
//				submitForm.setAnswer("muc#roomconfig_moderatedroom", owner);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
			
			multiChat.sendConfigurationForm(submitForm);

		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		}
	}

	public static List<Occupant> getOccup() {
		List<Occupant> participants;
		try {
			if(multiChat == null){
				//meaning the participants wants to touch
				return iMUC.getParticipants();
			}
			participants = multiChat.getParticipants();
			return participants;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static List<String> getParticipantJID() {
		List<Occupant> jid;
		List<String> mJid = new ArrayList<String>();
		try {
			if(multiChat == null){
				jid = iMUC.getParticipants();
			}else{
				jid = multiChat.getParticipants();
			}
			for (Occupant list : jid) {
				mJid.add(list.getJid());
			}
			return mJid;
		} 
		catch (NoResponseException e) {
			e.printStackTrace();
			return null;
		} 
		catch (XMPPErrorException e) {
			e.printStackTrace();
			return null;
		} 
		catch (NotConnectedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getParticipantNick() {
		List<Occupant> jid;
		try {
			if(multiChat == null){
				jid = iMUC.getParticipants();
			}
			else{
				jid = multiChat.getParticipants();
			}
			
			List<String> mJid = new ArrayList<String>();
			
		
			for (Occupant list : jid) {
				mJid.add(list.getNick());
			}
			return mJid;
		} 
		catch (NoResponseException e) {
			e.printStackTrace();
			return null;
		} 
		catch (XMPPErrorException e) {
			e.printStackTrace();
			return null;
		} 
		catch (NotConnectedException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static void setUsers(String username,  String sessionId) {
		int lengthOfRoom = users.size();
		if (lengthOfRoom >= LENGTH_OF_ROOM) {
			return;
		} else {
			try {
				Message message = new Message();
				message.setBody(sessionId);
				multiChat.invite(message, username + "@" + "localhost","Hey! I want to discuss with you");
//				multiChat.invite(message,username+"@"+GlobalApplication.SERVER_NAME, "Hey! I can you join us?");
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}
	}
	public void setUsers(List<String> userList, String sessionId) {
		int sizeOfListInParam = userList.size();

		if (LENGTH_OF_ROOM < sizeOfListInParam) {
//			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//			dialog.show();
			return;

		} else {
			for (String s : userList) {
				try {
					Message message = new Message();
					message.setBody(sessionId);
					multiChat.invite(message, s + "@" + "localhost","Hey! I want to discuss with you");
//					multiChat.invite(message, s + "@" + GlobalApplication.SERVER_NAME,"Hey! I want to discuss with you");
				} catch (NotConnectedException e) {
					e.printStackTrace();
				}
			}

			multiChat.addInvitationRejectionListener(new InvitationRejectionListener() {
						@Override
						public void invitationDeclined(final String invitee,final String reason) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									AlertDialog.Builder dialog = new AlertDialog.Builder(context);
									dialog.setMessage(invitee + " Reject Your Request " + reason);
									dialog.setPositiveButton("OK",
											new OnClickListener() {
												@Override
												public void onClick(DialogInterface dailog,int which) {
													dailog.dismiss();
												}
											});
									
									AlertDialog alert = dialog.create();
									alert.show();
								}
							});
						}
					});
		}
	}

	
	private static void configure(ProviderManager pm) {

		ProviderManager.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {

			ProviderManager.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));

		} catch (ClassNotFoundException e) {

			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");

		}
		// Roster Exchange
		// pm.addExtensionProvider("x", "jabber:x:roster",
		// new RosterExchangeProvider());

		// Message Events
		// pm.addExtensionProvider("x", "jabber:x:event",
		// new MessageEventProvider());

		// Chat State
		ProviderManager.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());

		ProviderManager.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());

		ProviderManager.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		ProviderManager.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		ProviderManager.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		ProviderManager.addExtensionProvider("html",
				"http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		ProviderManager.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		ProviderManager.addIQProvider("query",
				"http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		ProviderManager.addIQProvider("query",
				"http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		ProviderManager.addExtensionProvider("x", "jabber:x:data",
				new DataFormProvider());

		// MUC User
		ProviderManager.addExtensionProvider("x",
				"http://jabber.org/protocol/muc#user", new MUCUserProvider());

		// MUC Admin
		ProviderManager.addIQProvider("query",
				"http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

		// MUC Owner
		ProviderManager.addIQProvider("query",
				"http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

		// Delayed Delivery
		ProviderManager.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {

			ProviderManager.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));

		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		ProviderManager.addIQProvider("vCard", "vcard-temp",
				new VCardProvider());
		// Offline Message Requests
		ProviderManager.addIQProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		ProviderManager.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		ProviderManager.addIQProvider("query", "jabber:iq:last",
				new LastActivity.Provider());
		// User Search
		ProviderManager.addIQProvider("query", "jabber:iq:search",
				new UserSearch.Provider());
		// SharedGroupsInfo
		ProviderManager.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		ProviderManager.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		// FileTransfer
		ProviderManager.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		ProviderManager.addIQProvider("query",
				"http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		ProviderManager.addIQProvider("open", "http://jabber.org/protocol/ibb",
				new OpenIQProvider());
		ProviderManager.addIQProvider("close",
				"http://jabber.org/protocol/ibb", new CloseIQProvider());
		ProviderManager.addExtensionProvider("data",
				"http://jabber.org/protocol/ibb",
				new DataPacketProvider.PacketExtensionProvider());

		// Privacy
		ProviderManager.addIQProvider("query", "jabber:iq:privacy",
				new PrivacyProvider());
		ProviderManager.addIQProvider("command",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		ProviderManager.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		ProviderManager.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		ProviderManager.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		ProviderManager.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		ProviderManager.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
}
