package TwitterApiImplementation;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import com.thoughtworks.xstream.XStream;

public class Translator {
	//private static BASE64Encoder encode = new BASE64Encoder();
	//private static BASE64Decoder decode = new BASE64Decoder();
	
	private static XStream xStream = new XStream();
	//TODO encode in base64???
	public static String ObjectToString(Object obj){
		return xStream.toXML(obj);
	}
	
	public static Object StringToObject(String str){
		return xStream.fromXML(str);
	}
}
