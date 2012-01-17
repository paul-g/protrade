package org.ic.protrade.authentication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EncryptTest{
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
    
    @Test
    public void testMainNoArguments() throws Exception{ 
        Encrypt.main(new String[]{});
    }
}
