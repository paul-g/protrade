package org.ic.tennistrader.authentication;

import junit.framework.TestCase;

import org.junit.Test;

public class EncryptTest extends TestCase{
    @Test
    public void testEncryptDecrypt() throws Exception{
        String password = "dummy!pass1234";
        String encrypted = Encrypt.encrypt(password);
        String decrypted = Encrypt.decrypt(encrypted);
        assertEquals(password, decrypted);
    }
    
    @Test
    public void testMain() throws Exception{
        Encrypt.main(new String[]{"dummy"});
    }
}
