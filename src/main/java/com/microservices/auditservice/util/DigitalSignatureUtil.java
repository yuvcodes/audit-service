package com.microservices.auditservice.util;

import lombok.experimental.UtilityClass;

import java.security.*;
import java.util.Base64;

@UtilityClass
public class DigitalSignatureUtil {
    private final String ALGORITHM = "SHA256withRSA";

    public String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verifySignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance(ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(Base64.getDecoder().decode(signature));
    }
}