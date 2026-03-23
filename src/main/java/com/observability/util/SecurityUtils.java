package com.observability.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class per operazioni di security
 * 
 * Fornisce metodi per:
 * - Hash SHA-256 di stringhe (simulazione data masking)
 * - Validazione e verifica hash
 * 
 * @author Gaetano Cerciello
 */
@Slf4j
public class SecurityUtils {

    /**
     * Esegue l'hashing SHA-256 di una stringa
     * 
     * @param input Stringa da hashare
     * @return String rappresentante il valore hash in esadecimale
     */
    public static String hashPasswordSHA256(String input) {
        if (input == null || input.isEmpty()) {
            log.warn("Empty password provided for hashing");
            return "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Converte array di byte in stringa esadecimale
     * 
     * @param hash Array di byte
     * @return String rappresentante l'hash in formato esadecimale
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Esegue l'hashing e verifica un valore contro un hash noto
     * 
     * @param input Stringa da verificare
     * @param hash Hash noto in formato esadecimale
     * @return true se i valori corrispondono, false altrimenti
     */
    public static boolean verifyPassword(String input, String hash) {
        String inputHash = hashPasswordSHA256(input);
        return inputHash.equals(hash);
    }

    /**
     * Demo method: Hashizza la stringa "VALENTINANAPPI"
     * per demonstrare il data masking process
     * 
     * @return Hash SHA-256 di "VALENTINANAPPI"
     */
    public static String demonstrateDataMasking() {
        String dataToMask = "VALENTINANAPPI";
        String hashedValue = hashPasswordSHA256(dataToMask);
        log.info("Data Masking Demo - Original: {}, Hashed: {}", dataToMask, hashedValue);
        return hashedValue;
    }
}
