package org.uni.illuxplain.canvas;

public class ObjectArea {

	public int x1;
	public int y1;
	public String objectName;
	
	public ObjectArea(int x1,int y1,String objectName) {
		this.x1 = x1;
		this.y1 = y1;
		this.objectName = objectName;
	}
	
	@Override
	public String toString(){
		return objectName+"["+x1 +","+y1+"]";
	}
}
