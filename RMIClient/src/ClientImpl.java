

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
	
	private static final long serialVersionUID = 1L;


	private Applicazione applicazione;
	
	
	private String address;
	private byte[] key;					// chiave pubblica dell'applicazione
	private ServerInterface server;

	
	private void setServer(){
    	try{
    		System.setSecurityManager(new RMISecurityManager());
    		this.server = (ServerInterface) Naming.lookup("rmi://"+address+"/Service");
    	}catch (MalformedURLException murle){
			System.out.println();
			System.out.println("MalformedURLException");
			System.out.println(murle);
		}
		catch (RemoteException re){
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
		catch (NotBoundException nbe){
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		}
        catch (Exception e){
            System.out.println(e);
        }
    }
	
	
	
	
	public ClientImpl(Applicazione applicazione, String address, byte[] Pkey) throws RemoteException{
		
		this.applicazione = applicazione;
		
		this.address = address;
		
		this.key = Pkey;
		
		this.setServer();
		
		System.out.println("Costruttore ClientImpl");
	}
	
	
	@Override
	public void tell(int type, String user, byte[] msg) throws RemoteException {
		this.applicazione.deliverMessage(type, user, msg);
	}
	
	public ServerInterface getServer(){
		return this.server;
	}

	@Override
	public byte[] getPublicKey() throws RemoteException{
		return this.key;
	}
	
}
