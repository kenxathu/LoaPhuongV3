
package vn.mobifone.loaphuong.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 *
 * @author ChienDX
 */
//Mã hóa chuỗi kí tự
public class EncryptionService {

    private static final String ALG = "DES";
    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    public EncryptionService() throws GeneralSecurityException {
        init("smartlib.util.crypto".substring(0, 8).getBytes());
    }

    private void init(byte[] password) throws GeneralSecurityException {
        DESKeySpec desKeySpec = new DESKeySpec(password);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
        SecretKey pbeKey = keyFac.generateSecret(desKeySpec);

        encryptionCipher = Cipher.getInstance("DES");
        decryptionCipher = Cipher.getInstance("DES");

        encryptionCipher.init(1, pbeKey);
        decryptionCipher.init(2, pbeKey);
    }

   public String encrypt(String strPassword) throws IllegalBlockSizeException, BadPaddingException {
        byte[] res = encryptionCipher.doFinal(strPassword.getBytes());
        
        
        
       // BASE64Encoder encoder = new BASE64Encoder();
        String base64 = Base64.getEncoder().encodeToString(res);
        return base64;
    }

    public String decrypt(String strEncryptPassword) throws IllegalBlockSizeException, Exception, IOException {
       // BASE64Decoder encoder = new BASE64Decoder();
        byte[] inputBytes = Base64.getDecoder().decode(strEncryptPassword);
        byte[] retVal;
        try {
            retVal = decryptionCipher.doFinal(inputBytes);

        } catch (BadPaddingException e) {
            throw new Exception();
        }
        return new String(retVal);
    }
    public static void main(String[] args) {
        try {
            System.out.println(new EncryptionService().encrypt("loa1235"));
            System.out.println(new EncryptionService().decrypt("PItfc58e9m3JtvhD9oe11g=="));
        } catch (Exception ex) {
            Logger.getLogger(EncryptionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
