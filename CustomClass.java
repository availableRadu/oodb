import java.io.*;

public class CustomClass implements Serializable {
	
	private int field01;
	
	public CustomClass() {}
	
	public int getField01() {
		return this.field01;
	}
	
	public void setField01(int i) 
	{
		this.field01 = i;
	}
	
	public String toString() 
	{
		return "Field01 : " + field01;
	}
}