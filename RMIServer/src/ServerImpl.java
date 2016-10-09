
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	
	private static HashMap<String, ClientInterface> usersOn = new HashMap<String, ClientInterface>();
	
	private static final int ONLINE = 1;
	
	private static final int OFFLINE = 0;
	
	private static final int MESSAGEOFF = 4;
	
	private LogWriter console;

	private void initRemoteObject(){
			
		try 
		{
			Naming.rebind("rmi://127.0.0.1:1099/Service", this);
		} catch (RemoteException e) 
		{
			e.printStackTrace();
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}

	}
	
	private void delUserOn(String user) {
		usersOn.remove(user);
		sendAllInfo(OFFLINE , user);
	}
	
	private boolean authCAS(String user, String pwd){
		 return true;
	}
	
	
	public ServerImpl() throws RemoteException{
		this.initRemoteObject();
		console = new LogWriter();
		System.out.println("Server attivo!");
	}
	
	
	/**
	 * usata nella validate().
	 * 
	 * Aggiunge nome utente e interfaccia nell hash map.
	 * 
	 * @param user
	 * @param clientInterface
	 */
	public void addUserOn(String user, ClientInterface clientInterface) {
		
		ServerImpl.usersOn.put(user, clientInterface); // da qui è online !! 
	 	this.console.log("["+user+" CONNECT]");
		this.sendAllInfo( ONLINE , user);
	}
	
	
	@Override
	public boolean validate(String user, String pwd, ClientInterface client) throws RemoteException {
		
		if(authCAS(user,pwd)){
			
			this.addUserOn(user, client);
			
			System.out.println("utenti connessi: "+usersOn.size());
			
			return true;
		}
		return false;
	}
	

	@Override
	public byte[] getPublicKeyOf(String user) throws RemoteException {
		return usersOn.get(user).getPublicKey();
	}
	
	@Override
	public void sendMessageTo(int mode, String mitt, String dest, byte[] message) {
		
		if (mode == MESSAGEOFF){ return; }
		
		ClientInterface destClient = usersOn.get(dest);
		
		try {
			destClient.tell(mode,mitt,message);
		} catch (RemoteException e) {
			delUserOn(dest);
			e.printStackTrace();
		}
		
	}	
	
	@Override
	public void sendAllInfo(int type, String mitt) {
		
		Iterator<String> i = usersOn.keySet().iterator();
		String user = null;

		while(i.hasNext()){
			try {
					user = i.next().toString();
					
					if (user == mitt) continue;  			// non inviare all'utente ke lo ha generato 
					
                	ClientInterface client = usersOn.get(user);
                
					client.tell(type, mitt, "".getBytes());
			} catch (Exception e) {
					delUserOn(user);
					e.printStackTrace();
			}
		}
	}

	
	@Override
	public LinkedList<String> getUsersOn() throws RemoteException {
		
		LinkedList<String> myList = new LinkedList<String>();
		
		for (Map.Entry<String, ClientInterface> entry : usersOn.entrySet()) myList.add(entry.getKey());
		
		return myList;	
		
	}
	
	@Override
	public void logout(String user) throws RemoteException {
		System.out.println("disconnetti " + user);
		delUserOn(user);
	 	console.log("["+user+" DISCONNECT]");
	}
	
}
