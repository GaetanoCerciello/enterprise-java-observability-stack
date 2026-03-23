package com.observability;

import com.observability.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit Test per SecurityUtils
 * 
 * Testa:
 * - Hashing SHA-256
 * - Verifica di password
 * - Data masking con "VALENTINANAPPI"
 * 
 * @author Gaetano Cerciello
 */
@Slf4j
@DisplayName("SecurityUtils Unit Tests")
public class SecurityUtilsTest {

    private static final String TEST_PASSWORD = "MySecurePassword123!";
    private static final String DATA_MASKING_STRING = "VALENTINANAPPI";

    @Test
    @DisplayName("Dovrebbe eseguire l'hash SHA-256 di una password")
    void testHashPasswordSHA256_Success() {
        // Act
        String hashedPassword = SecurityUtils.hashPasswordSHA256(TEST_PASSWORD);

        // Assert
        assertThat(hashedPassword)
            .isNotNull()
            .isNotBlank()
            .hasSize(64) // SHA-256 produce sempre 64 caratteri esadecimali
            .doesNotContain(TEST_PASSWORD) // Non deve contenere la password originale
            .isUpperCase(); // SHA-256 in hex è generalmente maiuscolo o minuscolo

        log.info("✓ Test: hashPasswordSHA256 passed");
        log.info("  Original: {}", TEST_PASSWORD);
        log.info("  Hashed: {}", hashedPassword);
    }

    @Test
    @DisplayName("Dovrebbe agire in modo consistente con lo stesso input")
    void testHashPasswordSHA256_Consistent() {
        // Act
        String hash1 = SecurityUtils.hashPasswordSHA256(TEST_PASSWORD);
        String hash2 = SecurityUtils.hashPasswordSHA256(TEST_PASSWORD);

        // Assert
        assertThat(hash1).isEqualTo(hash2);
        log.info("✓ Test: hashPasswordSHA256 (Consistency) passed");
    }

    @Test
    @DisplayName("Dovrebbe generare hash differenti per input differenti")
    void testHashPasswordSHA256_DifferentInputs() {
        // Act
        String hash1 = SecurityUtils.hashPasswordSHA256("password123");
        String hash2 = SecurityUtils.hashPasswordSHA256("password124");

        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
        log.info("✓ Test: hashPasswordSHA256 (Different Inputs) passed");
    }

    @Test
    @DisplayName("Dovrebbe gestire stringhe vuote")
    void testHashPasswordSHA256_EmptyString() {
        // Act
        String hash = SecurityUtils.hashPasswordSHA256("");

        // Assert
        assertThat(hash).isBlank();
        log.info("✓ Test: hashPasswordSHA256 (Empty String) passed");
    }

    @Test
    @DisplayName("Dovrebbe verificare una password contro il suo hash")
    void testVerifyPassword_Success() {
        // Arrange
        String password = TEST_PASSWORD;
        String hash = SecurityUtils.hashPasswordSHA256(password);

        // Act
        boolean isVerified = SecurityUtils.verifyPassword(password, hash);

        // Assert
        assertThat(isVerified).isTrue();
        log.info("✓ Test: verifyPassword (Success) passed");
    }

    @Test
    @DisplayName("Dovrebbe fallire nella verifica con password errata")
    void testVerifyPassword_Failure() {
        // Arrange
        String password = TEST_PASSWORD;
        String hash = SecurityUtils.hashPasswordSHA256(password);

        // Act
        boolean isVerified = SecurityUtils.verifyPassword("WrongPassword", hash);

        // Assert
        assertThat(isVerified).isFalse();
        log.info("✓ Test: verifyPassword (Failure) passed");
    }

    @Test
    @DisplayName("Dovrebbe eseguire il data masking di 'VALENTINANAPPI'")
    void testDataMaskingDemo() {
        // Act
        String maskedValue = SecurityUtils.demonstrateDataMasking();

        // Assert
        assertThat(maskedValue)
            .isNotNull()
            .isNotBlank()
            .hasSize(64) // SHA-256 hash
            .doesNotContain(DATA_MASKING_STRING); // Non deve contenere il valore originale

        // Verifica che sia consistente
        String expectedHash = SecurityUtils.hashPasswordSHA256(DATA_MASKING_STRING);
        assertThat(maskedValue).isEqualTo(expectedHash);

        log.info("✓ Test: demonstrateDataMasking passed");
        log.info("  Original: {}", DATA_MASKING_STRING);
        log.info("  Masked (SHA-256): {}", maskedValue);
    }

    @Test
    @DisplayName("Dovrebbe gestire stringhe case-sensitive")
    void testHashPasswordSHA256_CaseSensitive() {
        // Act
        String hash1 = SecurityUtils.hashPasswordSHA256("password");
        String hash2 = SecurityUtils.hashPasswordSHA256("PASSWORD");

        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
        log.info("✓ Test: hashPasswordSHA256 (Case Sensitive) passed");
    }

    @Test
    @DisplayName("Dovrebbe gestire stringhe lunghe")
    void testHashPasswordSHA256_LongString() {
        // Arrange
        String longPassword = "This is a very long password with many characters " +
                             "!@#$%^&*()_+-=[]{}|;:',.<>?/`~0123456789";

        // Act
        String hashedPassword = SecurityUtils.hashPasswordSHA256(longPassword);

        // Assert
        assertThat(hashedPassword)
            .isNotNull()
            .hasSize(64);

        log.info("✓ Test: hashPasswordSHA256 (Long String) passed");
    }

    @Test
    @DisplayName("Dovrebbe gestire caratteri speciali")
    void testHashPasswordSHA256_SpecialCharacters() {
        // Arrange
        String specialPassword = "Psw@#$%&*()![]{}|;:',.<>?/`~";

        // Act
        String hashedPassword = SecurityUtils.hashPasswordSHA256(specialPassword);

        // Assert
        assertThat(hashedPassword)
            .isNotNull()
            .hasSize(64);

        log.info("✓ Test: hashPasswordSHA256 (Special Characters) passed");
    }
}
