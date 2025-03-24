import java.io.*; 
import java.util.*;

public class User{
	
	private ArrayList<String> username;
	private ArrayList<String> password;
	private ArrayList<String> dataPath;
	
	public User() throws Exception
	{
		username = new ArrayList<String>();
		password = new ArrayList<String>();
		dataPath = new ArrayList<String>();
		
		File userDB = new File("userDB.java");
		if(!userDB.exists()) {
			userDB.createNewFile();
		}
		
		readDB();
		
		//initialise folders
		for(int i = 0; i < username.size(); i++) {
			
			File create = new File("stokaj" + File.separator + getUsername(i));
			if(!create.exists()) {
				create.mkdir();
			}
		}
	}
	
	private void readDB()
	{
		String fileSeparator = File.separator;
		
		try {
		BufferedReader textBuff=new BufferedReader(new InputStreamReader(new FileInputStream("userDB.java")));
		String ligne;
		boolean i = true;
		while ((ligne=textBuff.readLine())!=null){
			if(i) {
				username.add(ligne);
				dataPath.add("stokaj" + fileSeparator + ligne + fileSeparator);
				i = false;
			}
			else {
				password.add(ligne);
				i = true;
			}
		}
		textBuff.close();
		
		}catch (Exception e){e.printStackTrace();}
	}
	
	private void writeDB(String pseudo, String password) throws IOException
	{
		BufferedWriter myWriter = new BufferedWriter(new FileWriter("userDB.java", true));
		myWriter.write(pseudo+"\n");
		myWriter.write(password+"\n");
		myWriter.close();
	}
	
	public String getUsername(int i)
	{
		return this.username.get(i);
	}
	
	public String getPassword(String pseudo)
	{
		for(int i = 0; i < this.username.size(); i++) {
			if(getUsername(i).equals(pseudo)) {
				return this.password.get(i);
			}
		}
		return "Error : invalid username";
	}
	
	public String getDataPath(String pseudo)
	{
		for(int i = 0; i < this.username.size(); i++) {
			//if(getUsername(i).equals(pseudo)) {
			if(username.get(i).equals(pseudo)) {
				return this.dataPath.get(i);
			}
		}
		return "Error : invalid username";
	}
	
	public boolean isExisting(String pseudo)
	{
		for(int i = 0; i < this.username.size(); i++) {
			if(getUsername(i).equals(pseudo)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPasswordCorrect(String pseudo, String password)
	{
		return getPassword(pseudo).equals(password);
	}
	
	public void createUser(String pseudo, String password) throws IOException
	{
		writeDB(pseudo, password);
		this.username.add(pseudo);
		this.password.add(password);
		this.dataPath.add("stokaj" + File.separator + pseudo + File.separator);
		File newFolder = new File(getDataPath(pseudo), pseudo);
		newFolder.mkdir();
	}
	
}