import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class HybridEncryptionExample {

    public static void main(String[] args) {

        SecretKey claveSimetrica = obtenerClaveSecretaSimetrica();
        KeyPair parDeClaves = obtenerParDeClavesAsimetricas();
        PublicKey clavePublica = parDeClaves.getPublic();
        PrivateKey clavePrivada = parDeClaves.getPrivate();

        Scanner teclado = new Scanner(System.in);
        String mensajeUsuario;

        try {
            System.out.println("Introduce el mensaje que desees encripta:");
            mensajeUsuario = teclado.nextLine();

            byte[] mensajeEncriptadoAES = encrypt(mensajeUsuario, claveSimetrica);

            byte[] claveSimetricaByte = claveSimetrica.getEncoded();
            byte[] claveSimetricaEncriptadaRSA = cifrarConRSA(claveSimetricaByte,clavePublica);

            System.out.println( Base64.getEncoder().encodeToString(mensajeEncriptadoAES));
            System.out.println( Base64.getEncoder().encodeToString(claveSimetricaEncriptadaRSA));

            byte[] claveSimetricaByteDes = descifrarConRSA(claveSimetricaEncriptadaRSA,clavePrivada);
            SecretKey claveSimetricaDescifrada = new SecretKeySpec(claveSimetricaByteDes,"AES");

            String decryptedMessage = decrypt(mensajeEncriptadoAES, claveSimetricaDescifrada);

            System.out.println("Mensaje descifrado: " + decryptedMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static SecretKey obtenerClaveSecretaSimetrica(){
        String clavePersonalizada = "claveSecreta1234";

        // Convertir la clave a bytes
        byte[] claveBytes = clavePersonalizada.getBytes();

        // Crear una instancia de SecretKeySpec con la clave
        SecretKey secretKey = new SecretKeySpec(claveBytes, "AES");

        return secretKey;
    }

    public static byte[] encrypt(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message.getBytes());
    }

    public static String decrypt(byte[] encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
        return new String(decryptedBytes);
    }

    public static KeyPair obtenerParDeClavesAsimetricas(){
        try {
            KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] cifrarConRSA(byte[] datos, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cipher.doFinal(datos);
    }

    private static byte[] descifrarConRSA(byte[] datosCifrados, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        return cipher.doFinal(datosCifrados);
    }


}
