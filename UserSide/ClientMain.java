import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ClientMain{
	public static void main(String[] args) throws Exception {
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
		Socket serveur = new Socket(ip,4200);
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
	}
}