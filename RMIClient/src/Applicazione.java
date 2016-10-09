import java.util.LinkedList;

public interface Applicazione {
	
	public boolean validate(String user, String pwd);
	
	
	public ClientInterface getClient();
	
	public LinkedList<String> getUsersList();
	
	public void updateUsersOn();
	
	public void setUserName(String user);
	
	
	/** send/deliver message **/
	
	public void sendMessageTo(int type, String dest, String msg);
	
	public void deliverMessage(int type, String mitt, byte[] msg);
	
	/** UI **/
	
	public void openListUsers();

	public void openChat(String dest);
		
	public void exit();
	
	public void exitOnErr();
	
}