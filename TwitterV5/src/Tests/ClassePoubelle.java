package Tests;

import java.util.HashSet;
import java.util.TreeSet;

import TwitterApiImplementation.StandardDate;
import TwitterApiImplementation.Translator;
import TwitterApiImplementation.Tweet;
import TwitterApiImplementation.TweetReference;

public class ClassePoubelle {

	public static void main(String[] argc) throws InterruptedException{
		Tweet x = new Tweet();
		x.setMsg("super long message that will be use to test the tweet message post building besr;k prourt un tuc rn latin");
		x.setDate(new StandardDate());
		x.setPosterName("coucou les amis");
		System.out.println(Translator.ObjectToString(x));
	}
}
