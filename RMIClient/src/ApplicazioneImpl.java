
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;


public class ApplicazioneImpl implements Applicazione{

	
	private static final String HOST = "127.0.0.1";	
	
	public static final int ONLINE = 1;
	
	public static final int OFFLINE = 0;
	
	public static final int MESSAGE = 2;
	
	public static final int CRYPTO = 3;

	public static final int MESSAGEOFF = 4;
	

	
	private ClientImpl client;
	
	private String username;
	
	private ServerInterface server;
	
	private Encryptor encrypter;
	
	
	private WindowUsers usersGUI;
	
	private HashMap<String, Chat> chatsOpen = new HashMap<String, Chat>();
	
	
	public ApplicazioneImpl() {
		
		try 
		{

			this.encrypter = new Encryptor();
			
			this.client = new ClientImpl(this,HOST,encrypter.getPublicKey().getEncoded());
			
			this.server = client.getServer();
			
			
		} catch (RemoteException re) {
			
			re.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}
	
	public void startLogin() {
		new Login(this);	
	}
	
	@Override
	public boolean validate(String user, String pwd) {
		
		try {
		
			return this.server.validate(user, pwd, client);
				
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}

	
	
	@Override
	public void setUserName(String user) {
		this.username = user;
	}
	
	@Override
	public ClientInterface getClient(){
		return this.client;
	}
	
	@Override
	public void updateUsersOn() {
		
		LinkedList<String> list = this.getUsersList();
		
		usersGUI.updateUsersOn(list);
		
	}
	
	@Override
	public LinkedList<String> getUsersList() {
		
		LinkedList<String> l = null;
		
		try {
			l = this.server.getUsersOn();
		} catch (RemoteException e) {
			System.out.println("getUsersList()");
			e.printStackTrace();
		}
		
		return l;
		
	}


	@Override
	public void sendMessageTo(int type, String dest, String msg) {
		
		try {
			
			byte[] message = msg.getBytes();
			if(type == CRYPTO){
				byte[] key = server.getPublicKeyOf(dest);
				message = encrypter.encrypt(msg.getBytes() , key);
			}
			server.sendMessageTo(type,this.username, dest, message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deliverMessage(int type, String user, byte[] msg) {
		
		String message = null;
		
		if((type == ONLINE)  || (type == OFFLINE)){
			if (chatsOpen.containsKey(user)) chatsOpen.get(user).showMessage("SEREVER",user + " go offline");
			this.updateUsersOn();
			return;
		}else if(type == MESSAGE){
			message = new String(msg);
		}else if(type == CRYPTO){
			message = this.encrypter.decrypt(msg);
		}
		
		
		if (chatsOpen.containsKey(user)){
			chatsOpen.get(user).setVisible(true);
			chatsOpen.get(user).showMessage(user,message);
		}else{
			openChat(user);
			chatsOpen.get(user).setVisible(true);
			chatsOpen.get(user).showMessage(user,message);
		}
	}

	/**
	 *
	 *  GUI
	 * 
	**/
	
	@Override
	public void openListUsers() {	
			
		LinkedList<String> list = this.getUsersList();
		
		usersGUI = new WindowUsers(this, this.username, list);
		
		usersGUI.setVisible(true);
		
	}
	
	@Override
	public void openChat(String dest) {
		
		if (chatsOpen.containsKey(dest)){
			chatsOpen.get(dest).setVisible(true);
			return;
		}
		WindowChat chat = new WindowChat(dest,this,this.username);
		chatsOpen.put(dest, chat);
	}
	
	@Override
	public void exit() {
		// TODO conferma uscita
		System.out.println("disconnetto "+ username);
		try {
			server.logout(username);
		} catch (RemoteException e) { }
		System.exit(0);
	}

	@Override
	public void exitOnErr() {
		System.exit(-1);
	}
	
	
	
	public static void main(String[] args ) {
		
		ApplicazioneImpl app = new ApplicazioneImpl();
		app.startLogin();
		
	}


}
