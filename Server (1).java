package paket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;


public class Server {

    public static long start = System.nanoTime();

    public Server() throws Exception{

        String karakteri = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String aa = "aaaaaaaa";
        ArrayList<Character> ch = new ArrayList<>();
        char key[] = aa.toCharArray();
        for(char c: karakteri.toCharArray()){
            ch.add(c);
        }

        String sifrovanText = sifrujDES("aaaab587","ovo je tekst");
        for(int i = 0; i<4; i++){
            ThereadDES thDes = new ThereadDES(i,sifrovanText);
            Thread th = new Thread(thDes);
            th.start();
        }


    }
    private String sifrujDES(String key, String plaintext) throws Exception{

        /////////KORAK 1/ PRETVARANJE U BYTOVE

        byte[] keyBytes=key.getBytes();
        byte[] plaintextBytes=plaintext.getBytes();

        ///////KORAK 2 //// GENERISANJE KLJUCA

        KeySpec ks= new DESKeySpec(keyBytes);
        SecretKeyFactory skf=SecretKeyFactory.getInstance("DES");
        SecretKey sk=skf.generateSecret(ks);

        ////////KORAK 3/// SIFROVANJE

        Cipher cipher=Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        byte[] encryptedBytes=cipher.doFinal(plaintextBytes);

        Base64.Encoder encoder= Base64.getEncoder();
        String encrypted=encoder.encodeToString(encryptedBytes);

        return encrypted;



    }
    public static void exit(String text, String key){

        long end = System.nanoTime();
        long time = (end - start) / 1000000000;
        // u konzoli mozda bude potrebno da se skroluje malo
        System.out.println("-------------------------------------------------------\nExecution time: " + time + " seconds" + "\nEncripted text is: "+ text + "\nAnd key is: "+ key+"\n-------------------------------------------------------");
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {

        new Server();
    }
}