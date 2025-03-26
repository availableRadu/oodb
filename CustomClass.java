import java.io.*;

class CustomClass<T> implements Serializable {
	
	private String nameClass;
	private String name01;
	private T field01;
	
	public CustomClass(String name) 
	{
		this.nameClass = name;
	}
	
	public T getField01() 
	{
		return this.field01;
	}
	
	public void setField01(String name, T i) 
	{
		this.name01 = name;
		this.field01 = i;
	}
	
	public String toString() 
	{
		return "[" + this.nameClass + "]" + 
		"{" + name01 + "=" + field01 + "}";
	}
}