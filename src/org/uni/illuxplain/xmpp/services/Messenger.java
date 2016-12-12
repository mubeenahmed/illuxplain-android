package org.uni.illuxplain.xmpp.services;

public class Messenger {

	
	private String username;
	private String message;
	private String date;
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	public void setDate(String date){
		this.date = date;
	}
	
	
	public String getMessage(){
		return message;
	}
	public String getUsername(){
		return username;
	}
	public String getDate(){
		return date;
	}
	
	
}
