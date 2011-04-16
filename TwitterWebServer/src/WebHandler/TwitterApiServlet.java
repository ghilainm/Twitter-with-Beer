package WebHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DHT.DebugHandler;
import DHT.DhtAPI;
import TwitterApiImplementation.TwitterAPI;
import TwitterApiImplementation.TwitterImplementation;

/**
 * Servlet implementation class TwitterApiServlet
 */
@WebServlet("/TwitterApiServlet")
@ServletSecurity(value=@HttpConstraint(transportGuarantee=TransportGuarantee.CONFIDENTIAL))
public class TwitterApiServlet extends HttpServlet {
	// https://localhost:443
	private static final long serialVersionUID = 1L;
   private HashMap<String,TwitterAPI> twitterAPIMap;
   private HashMap<String,Integer> sessionTimer;
   private static DhtAPI dhtAPI;
   protected static final String twitterSessionID = "twitterSessionID";
   //Life time before a sesion is destroyed in second
   private static final Integer SESSIONLIFETIME = 3600;
   private static int nbrSessions = 0;
   
   /**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterApiServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException { 
		super.init(config);
		dhtAPI = new DebugHandler();
		twitterAPIMap = new HashMap<String, TwitterAPI>(500);
		sessionTimer = new HashMap<String, Integer>(1000);
		log("Initialised");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parse request
		log(request.getRequestURI());
		response.setContentType("text/xml");
		int numArgs;
		try{
			numArgs = Integer.parseInt(request.getParameter("nbrArgs"));
		}
		catch (NumberFormatException e){
			numArgs = -1;
		}
		String idToResend = request.getParameter("idToResend");
		String[] args = null;
		if(numArgs>0)
			args = new String[numArgs];
		else 
			args = new String[0];
		String meth = request.getParameter("meth");
		String sessionID = request.getParameter(twitterSessionID);
		TwitterAPI twitterAPI = null;
		if(sessionID != null)
			twitterAPI = twitterAPIMap.get(sessionID);
		int argIndex = 0;
		while(argIndex<numArgs){
			args[argIndex] = request.getParameter("arg"+argIndex);
			argIndex++;
		}
		
		
		//Check if this is a deconnection request
		if(meth==null || meth.equals("disconnect") || idToResend==null || numArgs < 0){
			System.out.println("deconnection");
			if(twitterAPIMap.containsKey(sessionID)){
				twitterAPIMap.remove(sessionID);
				nbrSessions--;
			}
			if(sessionTimer.containsKey(sessionID))
				sessionTimer.remove(sessionID);
		}
		else{
			sessionTimer.put(sessionID, SESSIONLIFETIME);
			//Check if this is a logIn request
			if(meth.equals("logIn") || meth.equals("createUser")){
				nbrSessions++;
				sessionID  = UUID.randomUUID().toString();
				while(twitterAPIMap.containsKey(sessionID))
					sessionID  = UUID.randomUUID().toString();
				twitterAPIMap.put(sessionID, new TwitterImplementation(dhtAPI));
				twitterAPI = twitterAPIMap.get(sessionID);
			}
		}
		
		String resultStr = null;
		if(twitterAPI == null){
			System.out.println("Some user tries to do something unauthorized");
			resultStr = Parser.makeresult("", "", "", true, Parser.logerror,"");
		}
		else
			resultStr = Parser.handle(numArgs,idToResend,args,meth,twitterAPI,sessionID);
		PrintWriter pw = new PrintWriter(response.getOutputStream());
		pw.write(resultStr);
		pw.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
