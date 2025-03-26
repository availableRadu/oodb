import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.time.*;

public class ClientMain{
	public static void main(String[] args) {
		try{
		
		Socket serveur = new Socket("192.168.1.100",4200);
		/*
		CustomClass<String> cc = (CustomClass<String>) getObject(serveur);
		System.out.println(cc.toString());
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test"));
		objectOutputStream.writeObject(cc);
		*/
		
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("C:/Users/Radu/Desktop/cours_fac/2024-2025/semestre_4/info4b/projet/UserSide/test")));
		@SuppressWarnings("unchecked")
		CustomClass<String> cc = (CustomClass<String>) objectInputStream.readObject();
		System.out.println(cc.toString());
		sendObject(cc, serveur);
		
		//initialise files
		File create = new File("Download");
		if(!create.exists()) {
			create.mkdir();
		}
		create = new File("fileToSave.txt");
		if(!create.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(create))) {
				writer.write("-1\n");
				writer.write(Long.toString(System.currentTimeMillis()) + "\n");
			} catch (IOException e) {e.printStackTrace();}
		}
		create = new File("toSend.txt");
		if(!create.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(create))) {
				writer.write("-1\n");
				writer.write("false\n");
			} catch (IOException e) {e.printStackTrace();}
		}
		
		//get ip of server	
		System.out.println("Pour entrer l'adresse ip de votre serveur de sauvegarde, tapper 1\npour tester le programme sur un serveur local, tapper 2");
		boolean a = true;
		String answer;
		String ip = "localhost";
		while(a) {
			answer = new Scanner(System.in).nextLine();
			switch(answer) {
				case "1":
					System.out.println("Entrez l'adresse ip dans le format suivant : 255.255.255.255");
					while(a) {
						ip = new Scanner(System.in).nextLine();
						if(isValidIPAddress(ip)) {
							a = false;
						}
						else {
							System.out.println("L'adresse ip est invalide");
						}
					}
					break;
				case "2":
					a = false;
					break;
				default:
					System.out.println("Reponse invalide");
			}
		}
		//Socket serveur = new Socket(ip,4200);
		//Socket serveur = new Socket("192.168.1.16",4200);
		
		//log in process
		String username;
		String password;
		
		PrintWriter printWriter = new PrintWriter(new BufferedWriter( new OutputStreamWriter( serveur.getOutputStream() ) ), true);
		
		DataInputStream dataInputStream = new DataInputStream(serveur.getInputStream());
		a = true;
		int etat = 1;
		while(a) {
			switch(etat) {
				case 1:
					System.out.println("Rentrez votre nom d'utilisateur");
					username = new Scanner(System.in).nextLine();
					sendInfoCode(0, serveur);
					
					sendText(username, serveur);

					if(dataInputStream.readBoolean()) {
						etat = 2;
					}
					else {
						etat = 3;
					}
					break;
				case 2:
					System.out.println("Rentrez votre mot de passe");
					password = new Scanner(System.in).nextLine();
					sendText(password, serveur);
					if(dataInputStream.readBoolean()) {
						a = false;
					}
					else {
						System.out.println("Mauvais mot de passe");
					}
					break;
				case 3:
					System.out.println("Nom d'utilisateur incorrect");
					System.out.println("Pour entrer le nom d'utilisateur une nouvelle fois, taper 1");
					System.out.println("pour creer un nouveau compte, taper 2");
					
					answer = new Scanner(System.in).nextLine();
					if(answer.equals("1")) {
						etat = 1;
					}
					else if(answer.equals("2")) {
						etat = 4;
					}
					else {
						etat = 3;
					}
					break;
				case 4:
					sendInfoCode(1, serveur);
					System.out.println("Rentrez un nom d'utilisateur");
					username = new Scanner(System.in).nextLine();
					sendText(username, serveur);
					
					if(dataInputStream.readBoolean()) {
						System.out.println("Ce nom d'utilisateur est deja pris");
					}
					else {
						etat = 5;
					}
					break;
				case 5:
					System.out.println("Rentrez un mot de passe");
					password = new Scanner(System.in).nextLine();
					sendText(password, serveur);
					
					etat = 1;
					break;
				default:
					System.out.println("erreur automate login");
					a = false;
			}
		}
		
		dataInputStream.close();
		
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public static boolean isValidIPAddress(String ip)
    {
		if (ip == null) {
            return false;
        }
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
		return Pattern.compile(regex).matcher(ip).matches();
    }
	
	public static void sendInfoCode(int n, Socket serveur) throws Exception
	{
		DataOutputStream dataOutputStream = new DataOutputStream(serveur.getOutputStream());
		dataOutputStream.writeInt(n);	
		dataOutputStream.flush();
		//sendText(Integer.toString(n), serveur);
	}
	
	private static void sendText(String text, Socket serveur) throws Exception
	{
		PrintWriter printWriter = new PrintWriter(new BufferedWriter( new OutputStreamWriter( serveur.getOutputStream() ) ), true);
		printWriter.println(text);
		printWriter.flush();
	}
	
	private static void sendObject(Object o, Socket serveur) {
		try {
		ObjectOutputStream out = new ObjectOutputStream( serveur.getOutputStream() );
		out.writeObject(o);
		}catch(Exception e){e.printStackTrace();}
	}
	
	private static Object getObject(Socket serveur)
	{
		Object o = null;
		try{
		ObjectInputStream in = new ObjectInputStream( serveur.getInputStream() );
		o = in.readObject();
		}catch(Exception e){e.printStackTrace();}
		return o;
	}
	
}