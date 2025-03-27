import java.io.*; 
import java.net.*;
import java.util.*;

public class ServerMain{
	public static void main(String[] args) throws Exception {
		ServerSocket sock = new ServerSocket(4200);
		
		//initialise files
		/*
		File create = new File("stokaj");
		if(!create.exists()) {
			create.mkdir();
		}
		*/
		
		User utilisateurs = new User();
		
		for(int i = 0; i < 999; i++) {
			UserThread th = new UserThread(sock.accept(), utilisateurs);
			th.start();
		}
		
	}
}