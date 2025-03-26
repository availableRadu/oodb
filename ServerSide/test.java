import java.io.*; 
import java.net.*;
import java.util.*;

public class test{
	public static void main(String[] args) throws Exception {
		CustomClass<Integer> cc = new CustomClass<Integer>("michel");
		cc.setField01("iq",7);
		System.out.println(cc.toString());
	}
}

