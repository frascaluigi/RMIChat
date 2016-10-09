import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;


public interface ServerInterface extends Remote{
	
	public boolean validate(String user, String pwd, ClientInterface client) throws RemoteException;
	
	
	public byte[] getPublicKeyOf(String user) throws RemoteException;
	
	public void sendMessageTo(int mode, String mitt, String dest, byte[] msg) throws RemoteException;
	
	public void sendAllInfo(int type, String mitt) throws RemoteException;
	
	
	public LinkedList<String> getUsersOn() throws RemoteException;
	
	public void logout(String user) throws RemoteException;
	
}
