package org.uni.email.illuxplain;



public class EmailReport {

//	private String user ="";
//	private String pass ="";
//
//	private String to="";
//	private String from="";
//
//	private int port =465;
//	private int sPort =465;
//
//	private String subject ="";
//	private String body="";
//
//	private String host ="smtp.gmail.com";
//	private boolean auth;
//	private boolean debuggable;
//	private Multipart multiPart;
//
//	public EmailReport() {
//		host = "";
//		//port = "";
//		//sPort = "";
//		user = "";
//		pass = "";
//		from = "";
//		subject = "";
//		body = "";
//		debuggable = true;
//		auth = true;
//		multiPart = new MimeMultipart();
//
//		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
//				.getDefaultCommandMap();
//		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
//		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
//		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
//		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
//		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
//		CommandMap.setDefaultCommandMap(mc);
//	}
//	public EmailReport(String user,String pass){
//		this();
//		this.user = user;
//		this.pass = pass;
//	}
//	
//	public boolean send() throws Exception{
//		Properties prop = setProperties();
//		if(!user.equals("") &&!pass.equals("") && to.length()>0 && pass.length()>0 && !subject.equals("") && !body.equals("")){
//			 Session session = Session.getInstance(prop,this);
//			 MimeMessage msg = new MimeMessage(session);
//			 
//			 msg.setFrom(new InternetAddress(from));
//			 InternetAddress addressTo = new InternetAddress(this.to);
//			 
//			 msg.setRecipient(RecipientType.TO, addressTo);
//			 msg.setSubject(subject);
//			 msg.setSentDate(new Date());
//			 
////			 BodyPart bodyPart = new MimeBodyPart();
////			 bodyPart.setText(body);
//			 
//			 msg.setText(body);
//			 msg.setContent(multiPart);
//			 
//			 Transport transport = session.getTransport("smtp");
//			 transport.connect(user, pass);
//			 transport.sendMessage(msg, msg.getAllRecipients());
//			 transport.close();
////			 Transport.send(msg);
//			 
//			 return true;
//		}
//		return false;
//	}
//
//	
//	private Properties setProperties(){
//		Properties props = new Properties();
//
//		 if(this.debuggable) { 
//		      props.put("mail.debug", "true"); 
//		    } 
//		 
//		 if(this.auth) { 
//		      props.put("mail.smtp.auth", "true"); 
//		    } 
//		    props.put("mail.smtp.user",user);
//		    props.put("mail.smtp.host", this.host);
//		    props.put("mail.smtp.port", this.port); 
//		    props.put("mail.smtp.socketFactory.port", this.sPort); 
//		    props.put("mail.smtp.starttls.enable","true");
//		    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
//		    props.put("mail.smtp.socketFactory.fallback", "false"); 
//		    
//		return props;
//	}
//	
//	@Override 
//	public PasswordAuthentication getPasswordAuthentication() { 
//	    return new PasswordAuthentication(this.user, this.pass); 
//	  } 
//	
////	public void addAttachment(String filename) throws Exception { 
////		  BodyPart messageBodyPart = new MimeBodyPart(); 
////		  DataSource source = new FileDataSource(filename); 
////		  messageBodyPart.setDataHandler(new DataHandler(source)); 
////		  messageBodyPart.setFileName(filename); 
////		 
////		  multiPart.addBodyPart(messageBodyPart); 
////		} 
//	
//	public void setBody(String body){
//		this.body = body;
//	}
//	public String getBody(){
//		return this.body;
//	}
//	
//	public void setTo(String to){
//		this.to = to;
//	}
//	public String getTo(){
//		return to;
//	}
//	public void setSubject(String subject){
//		this.subject = subject;
//	}
//	public String getSubject(){
//		return this.subject;
//	}
//	public void setFrom(String from){
//		this.from = from;
//	}
//	public String getFrom(){
//		return this.from;
//	}
}
