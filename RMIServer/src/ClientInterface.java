import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientInterface extends Remote{

	public void tell(int type, String user,byte[] msg) throws RemoteException;

	public byte[] getPublicKey() throws RemoteException; 
	
}
