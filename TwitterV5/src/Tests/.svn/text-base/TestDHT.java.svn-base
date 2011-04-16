package Tests;

import DHT.BeernetHandler;
import DHTExceptions.BadSecretException;
import DHTExceptions.ValueNotFoundException;

public class TestDHT {

	BeernetHandler beernet;
	
	public TestDHT(int aPort) {
		say("Connecting to oz process");
		beernet = new BeernetHandler(aPort);
	}
	public void say(String msg) {
		System.out.println(msg);
	}
	
	public void write(String secret, String key, String value) {
		try {
			say("writing s:"+secret+" k:"+key+" v:"+value);
			beernet.writeSecure(secret, key, value);
			say("it worked!");
		} catch (BadSecretException bse) {
			say("writing "+key+" didn't work because of bad secret "+secret);
		}
	}

	public void read(String key) {
		try {
			say("reading k:"+key);
			say(beernet.read(key));
		} catch (ValueNotFoundException vnfe) {
			say("key "+key+" not found");
		}		
	}
	
	public static void main(String []args) {
		TestDHT test = new TestDHT(9535);
		//test.read("orval");
		test.write("lucy", "orval", "8");
		test.say("using wrong secret");
		test.write("bad_secret", "orval", "9");
		test.read("orval");
		test.read("rochefort");
	}
}
