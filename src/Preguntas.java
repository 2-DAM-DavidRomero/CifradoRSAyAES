import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Preguntas {

    public static void main(String[] args) {

        try {
            // AES solo admite claves de 16, 24 o 32 bytes.
            // La clave usada tiene 13 bytes.
            // Java no puede iniciar el cifrado y lanza una excepción:
            // InvalidKeyException: Invalid AES key length

            cifrarConClaveInvalida();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            // En cifrado simétrico la clave de cifrado y descifrado debe ser la misma.
            // Se está usando una clave distinta para descifrar.
            // El descifrado falla porque los datos no coinciden con la clave.
            // Java lanza una excepción como:
            // BadPaddingException o IllegalBlockSizeException
            cifrarYDescifrarConClaveDistinta();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void cifrarConClaveInvalida() throws Exception {
        byte[] claveIncorrecta = "clave_invalida".getBytes(); // 13 bytes
        SecretKey claveAES = new SecretKeySpec(claveIncorrecta, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveAES); // ERROR
    }

    public static void cifrarYDescifrarConClaveDistinta() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");

        SecretKey claveCifrado = kg.generateKey();
        SecretKey claveDescifrado = kg.generateKey(); // distinta

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, claveCifrado);
        byte[] cifrado = cipher.doFinal("Hola".getBytes());

        cipher.init(Cipher.DECRYPT_MODE, claveDescifrado); // ERROR
        cipher.doFinal(cifrado);
    }


}
