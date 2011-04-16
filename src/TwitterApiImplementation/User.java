package TwitterApiImplementation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.LinkedList;


public class User {
	//Pseudo
	private String username;
	//Real name
	private String name;
	
	//Liste des balises contenue dans l'element
	public final static String userBalise = "user";
	public final static String nameBalise = "name";	public final static LinkedList<String> balisesList = new LinkedList<String>();
	
	public boolean isValid(String str){
		return str!= null && str.compareTo("")!=0;
	}
	
	public static void initUserClass(){
		balisesList.add(userBalise);
		balisesList.add(nameBalise);
	}
	
	public User(String username, String name) throws NonValidNameException{
		if(!isValid(username) || !isValid(name))
			throw new NonValidNameException("A name should always be a least a character");
		this.username = username;
		this.name = name;
		
	}
	
	public String username(){
		return username;
	}
	
	public String name(){
		return name;
	}
	
	public String toString(){
		/* Format d'un User : 
		 * Username
		 * name
		 */
		return "<"+userBalise+">"+username+"</"+userBalise+">\n"
		+"<"+nameBalise+">"+name+"</"+nameBalise+">\n";
	}
	
	
	public static User parseUser(String strToParse) throws parsingException, NonValidNameException{
		System.out.println("parsing of the user");
		int i = 0;
		int[] baliseValidityCheck = new int[balisesList.size()*2];
		for(String balise:balisesList){
			baliseValidityCheck[i] = strToParse.indexOf("<"+balise+">");
			if(baliseValidityCheck[i]==-1)throw new parsingException("missing: <"+balise+">");
			baliseValidityCheck[i]=baliseValidityCheck[i]+2+balise.length();
			i++;
			baliseValidityCheck[i] = strToParse.indexOf("</"+balise+">");
			if(baliseValidityCheck[i]==-1)throw new parsingException("missing: </"+balise+">");
			i++;
		}
		
		for(int index = 0; index<baliseValidityCheck.length; index = index + 2){
			//Test to verify that no balise is included into another
			if(baliseValidityCheck[index+1] < baliseValidityCheck[index])throw new parsingException("Balise: <"+balisesList.get(index)+"> and Balise :</"+balisesList.get(index)+">, in wrong order");
		}
		
		User paul = new User(strToParse.substring(baliseValidityCheck[0],baliseValidityCheck[1]),
				strToParse.substring(baliseValidityCheck[2],baliseValidityCheck[3]));
		paul.setPublicKey(strToParse.substring(baliseValidityCheck[4],baliseValidityCheck[5]));
		return paul;
	}
	
}
