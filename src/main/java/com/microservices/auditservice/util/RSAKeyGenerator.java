package com.microservices.auditservice.util;

import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class RSAKeyGenerator {
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Secure 2048-bit key size
        return keyPairGenerator.generateKeyPair();
    }
}
