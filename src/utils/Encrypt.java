package src.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encrypt {
  
  private static char[] KEY = "na1sd3asd11ai789ugh;angadflgawer./sdf".toCharArray();
  
  public static final byte[] SALT = {
      (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
      (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
  };
  
  public static void main(String args[]) throws GeneralSecurityException, IOException {
    if (args.length != 1 ){
      System.out.println("usage: java Encrypt <password>");
      return;
    }
    
    String password = args[0];
    log("password: " + password);
    log("key: " + KEY.toString());
     
    decryptTest(password);
  }
  
  private static String decryptTest(String password) throws GeneralSecurityException, IOException{
    String originalPassword = password;
    log("Original password: " + originalPassword);
    String encryptedPassword = encrypt(originalPassword);
    log("Encrypted password: " + encryptedPassword);
    String decryptedPassword = decrypt(encryptedPassword);
    log("Decrypted password: " + decryptedPassword);
    return encryptedPassword;
  }


  private static String encrypt(String property) throws GeneralSecurityException {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey key = keyFactory.generateSecret(new PBEKeySpec(KEY));
      Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
      pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
      return base64Encode(pbeCipher.doFinal(property.getBytes()));
  }
  

  public static String decrypt(String property) throws GeneralSecurityException, IOException {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey key = keyFactory.generateSecret(new PBEKeySpec(KEY));
      Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
      pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
      return new String(pbeCipher.doFinal(base64Decode(property)));
  }

  private static byte[] base64Decode(String property) throws IOException {
      return new BASE64Decoder().decodeBuffer(property);
  }
  
  private static String base64Encode(byte[] bytes) {
    return new BASE64Encoder().encode(bytes);
}
  
  private static void log(String message){
    System.out.println(message);
  }

}
