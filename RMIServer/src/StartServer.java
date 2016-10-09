import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;


public class StartServer {
	
	public static void main(String[] args){
		try {
			System.setSecurityManager(new RMISecurityManager()); 
			ServerImpl server = new ServerImpl();
			
		} catch (RemoteException e) {
			System.out.println("Errore Server!");
			e.printStackTrace();
		}
	}
}
