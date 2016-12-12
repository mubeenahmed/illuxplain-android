package org.uni.illuxplain.xmpp.file;

import java.io.File;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.si.provider.StreamInitiationProvider;
import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.xmpp.services.XmppManager;
import org.uni.illuxplain.xmpp.services.XmppManager.DrawingReceiver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class FileTransferOneToOne {
	
	public interface OnFileTransferOneToOne{
		public void incomingOneToOneFile(String fileName);
	}

	DrawingReceiver receiver;
	private OutgoingFileTransfer outgoing;
	private FileTransferManager fileManager;

	private static XMPPConnection conn = XmppManager.conn;
	private static String fileName;
	private Context context;
	private OnFileTransferOneToOne listener;
	
	private String uri;
	
	private FileTransferNegotiator negotiator;

	public FileTransferOneToOne(Context context) {
		this.context = context;
		
		fileManager = FileTransferManager.getInstanceFor(conn);
		fileManager.addFileTransferListener(new MyFileTransfering());

		ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(conn);
		if (sdm == null) {
			sdm.addFeature("http://jabber.org/protocol/disco#item");
			sdm.addFeature("http://jabber.org/protocol/disco#info");
	        sdm.addFeature("jabber:iq:privacy");
	        sdm.addFeature("http://jabber.org/protocol/bytestreams");
	        sdm.addFeature("http://jabber.org/protocol/ibb");
		}

		ProviderManager.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
		ProviderManager.addIQProvider("query","http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
		ProviderManager.addIQProvider("query","http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
		ProviderManager.addIQProvider("query","http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
		
		ProviderManager.addIQProvider("open", "http://jabber.org/protocol/ibb",new OpenIQProvider());
		ProviderManager.addIQProvider("close", "http://jabber.org/protocol/ibb", new CloseIQProvider());
		ProviderManager.addExtensionProvider("data", "http://jabber.org/protocol/ibb",new DataPacketProvider.PacketExtensionProvider());
		
		listener = (OnFileTransferOneToOne) context;
		negotiator = FileTransferNegotiator.getInstanceFor(conn);

	}

	public void sendImage(String resource,String uri) {

		if(resource == null){
			return;
		}
		this.uri = uri;
		
		outgoing = fileManager.createOutgoingFileTransfer(resource);
		final File file = new File(this.uri);
		final ProgressDialog progressDialog = new ProgressDialog(context);
		new AsyncTask<Void, Void, Boolean>() {
			
			private String uri;

			protected void onPreExecute() {
				progressDialog.show();
				progressDialog.setMessage("Sending Please Wait");
				progressDialog.setCancelable(true);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface di) {
						cancel(true);
					}
					
				});
				
			};
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					outgoing.sendFile(file, "Check this out");
				} catch (SmackException e) {
					e.printStackTrace();
				}

				while (!outgoing.isDone()) {

					if (outgoing.getStatus().equals(FileTransfer.Status.error)) {
						outgoing.cancel();
						return true;
					} 
					else if (outgoing.getStatus().equals(FileTransfer.Status.cancelled)|| outgoing.getStatus().equals(FileTransfer.Status.refused)) {
						outgoing.cancel();
						return true;
					}
				}

				if (outgoing.getStatus().equals(FileTransfer.Status.refused)) {
 					outgoing.cancel();
					return true;
				}
				else if(outgoing.getStatus().equals(FileTransfer.Status.error)){
					outgoing.cancel();
					return true;
				}
				else if(outgoing.getStatus().equals(FileTransfer.Status.cancelled)){
					outgoing.cancel();
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				progressDialog.dismiss();
				
				if (result) {
					progressDialog.dismiss();
					//AlertDialog.Builder builder = new AlertDialog.Builder(context);
					//AlertDialog dialog = builder.setTitle("Error!").setMessage("There was some problem in Sending File but We will manage "+ outgoing.getStatus()).create();
					//dialog.show();
					Toast.makeText(context, "Sending failed.. Trying Again ", Toast.LENGTH_LONG).show();
					FileTransferVolley volley = new FileTransferVolley(context, this.uri, CanvasActivity.WEB_SERVICE_URL);
					volley.shareFile();
					
				}
				
			}
		}.execute(null,null,null);

	}
	
	private class MyFileTransfering implements FileTransferListener {
		@Override
		public void fileTransferRequest(final FileTransferRequest fileRequest) {
			final File dir = Environment.getExternalStorageDirectory();
			final File folder = new File(dir.getAbsoluteFile() + "/Illuxplain/");

			boolean success = true;
			if (!folder.exists()) {
				success = folder.mkdir();
			}

			if (success) {
				new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
						IncomingFileTransfer incoming = fileRequest.accept();
						fileName =  incoming.getFileName();

						try {
							File file = new File(folder, incoming.getFileName());

							incoming.recieveFile(file);
							while (!incoming.isDone()) {
								try {
									Thread.sleep(1000L);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (incoming.getStatus().equals(FileTransfer.Status.error)) {
									incoming.cancel();
									return true;
								}
								if (incoming.getException() != null) {
									incoming.cancel();
									return true;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							return true;
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						if (result)
							showAlertDialog();
						listener.incomingOneToOneFile(fileName);
					}
				}.execute(null, null, null);

			} else {
				showAlertDialog();
			}
		}

		private void showAlertDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog dialog = builder.setMessage("Error While Receiving File")
					.setTitle("Error!").create();
			
			dialog.show();
		}

	}
}
