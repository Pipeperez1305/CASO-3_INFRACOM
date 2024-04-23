package Clases;

import java.security.*;
import java.security.spec.*;

import javax.crypto.KeyAgreement;

public class KeyGenerator {

    public static KeyPair generateRSAKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static KeyAgreement generateDiffieHellmanKeys() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(keyPair.getPrivate());
        return keyAgree;
    }
}

