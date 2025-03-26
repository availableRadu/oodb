import java.io.*; 
import java.net.*;
import java.util.*;

public class UserThread extends Thread{
	
	private Socket sock;
	private User client;
	private String pseudo;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private BufferedReader in;
	private PrintWriter out;
	
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
		CustomClass<String> c = new CustomClass<String>("test");
		c.setField01("v1","7");
		sendObject(c);
		
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
		
		in.close();
		out.close();
		dataInputStream.close();
		dataOutputStream.close();
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String recieveText() throws Exception
	{
		return new BufferedReader(new InputStreamReader(this.sock.getInputStream())).readLine();
	}
	
	private void sendObject(Object o) 
	{
		try{
		ObjectOutputStream out = new ObjectOutputStream( sock.getOutputStream() );
		out.writeObject(o);
		}catch(Exception e){e.printStackTrace();}
	}
	
	private Object getObject() throws Exception
	{
		ObjectInputStream in = new ObjectInputStream( sock.getInputStream() );
		return in.readObject();
	}
}