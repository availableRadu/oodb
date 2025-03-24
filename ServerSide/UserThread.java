import java.io.*; 
import java.net.*;
import java.util.*;

public class UserThread extends Thread{
	
	Socket sock;
	User client;
	String pseudo;
	DataOutputStream dataOutputStream;
	DataInputStream dataInputStream;
	BufferedReader in;
	PrintWriter out;
	
	public UserThread(Socket sock, User utilisateurs)
	{
		this.sock = sock;
		this.client = utilisateurs;
		try{
		this.dataOutputStream = new DataOutputStream(this.sock.getOutputStream());
		this.dataInputStream = new DataInputStream(this.sock.getInputStream());
		this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
		this.out = new PrintWriter(new BufferedWriter( new OutputStreamWriter( this.sock.getOutputStream() ) ), true);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void run()
	{
		try {
		//gestion compte utilisateur :
		Boolean a = true;
		int infoCode = 0;
		while(a) {
			
			infoCode = /**recieveText();**/dataInputStream.readInt();

			switch(infoCode) {
				case 0:
					//sendConfirmation();
					String username = recieveText();
					Boolean b = client.isExisting(username);
					dataOutputStream.writeBoolean(b);
					if(b) {
						do {
							String password = recieveText();
							b = client.isPasswordCorrect(username, password);
							dataOutputStream.writeBoolean(b);
						} while(!b);
						pseudo = username;
						a = false;
					}
					break;
				case 1:
					username = recieveText();
					b = client.isExisting(username);
					dataOutputStream.writeBoolean(b);
					if(!b) {
						String password = recieveText();
						client.createUser(username, password);
					}
					break;
				default :
					System.out.println("comment ?");
			}
		}
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String recieveText() throws Exception
	{
		return new BufferedReader(new InputStreamReader(this.sock.getInputStream())).readLine();
	}
}