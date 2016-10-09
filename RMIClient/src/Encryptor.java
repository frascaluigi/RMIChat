
import java.security.MessageDigest;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class Encryptor {
	private Cipher cipher;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private KeyPair pair;
	
	public Encryptor(){
		try{	
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			this.pair = keyGen.generateKeyPair();
			this.publicKey = pair.getPublic();
			this.privateKey = pair.getPrivate();	
			this.cipher = Cipher.getInstance("RSA");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public PublicKey getPublicKey(){
		return this.publicKey;
	}
	
	public byte[] encrypt(byte[] msg, byte[] key){	
		
		X509EncodedKeySpec ks = new X509EncodedKeySpec(key);
		
		try{
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey pk = kf.generatePublic(ks); 
			
			this.cipher.init(Cipher.ENCRYPT_MODE, pk);
			return cipher.doFinal(msg);	
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
		
	public String decrypt(byte[] msgEnc){		
		
		try{
			this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
			byte[] dec = cipher.doFinal(msgEnc);
			
			return new String(dec);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] MD5(byte[] bytesOfMessage) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(bytesOfMessage);
	}
	
}

























