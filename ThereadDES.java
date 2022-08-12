package paket;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.*;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThereadDES implements Runnable{


    int a;
    String sifrovanText;

    public ThereadDES(int a, String sifrovanText) {
        this.a = a;
        this.sifrovanText = sifrovanText;
    }

    @Override
    public void run() {



        String regex = "^[a-zA-Z0-9\\s]+$";

        Pattern pattern = Pattern.compile(regex);

        String karakteri = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String aa = "aaaaaaaa";
        ArrayList<Character> ch = new ArrayList<>();
        char key[] = aa.toCharArray();
        for(char c: karakteri.toCharArray()){
            ch.add(c);
        }


        // deli petlju na 4 dela
        int i,ii, j, jj;
        if(a == 0){
            i = 0;
            ii = 15;
            j = 0;
            jj = 32;
        } else if (a == 1) {
            i = 14;
            ii = 31;
            j = 32;
            jj = 62;
        } else if (a == 2) {
            i = 32;
            ii = 46;
            j = 0;
            jj = 32;
        }else {
            i = 46;
            ii = 62;
            j = 32;
            jj = 62;
        }

        //prolazi kroz sve kombinacije slova i brojeva
        for(; i<ii; i++){
            key[0] = ch.get(i);
            for(; j<jj; j++){
                key[1] = ch.get(j);
                for(int k = 0; k<62; k++){
                    key[2] = ch.get(k);
                    for(int l = 0; l<62; l++){
                        key[3] = ch.get(l);
                        for(int m = 0; m<62; m++){
                            key[4] = ch.get(m);
                            for(int n = 0; n<62; n++){
                                key[5] = ch.get(n);
                                for(int p = 0; p<62; p++){
                                    key[6] = ch.get(p);
                                    for(int q = 0; q<62; q++){
                                        key[7] = ch.get(q);
                                        String keyString = String.valueOf(key);

                                        try{

                                            String desifrova = desifrujDES(keyString,sifrovanText);
                                            //provera da li su svi karakteri u string slova i brojevi
                                            Matcher matcher = pattern.matcher(desifrova);
                                            System.out.println("Thread No."+(a+1)+" "+ keyString);

                                             //zaustavlja petlju ako znamo deo poruke koja treba da se desifruje
                                            /*(if(desifrova.contains("ovo")){
                                                
                                                Server.exit(desifrova, keyString);
                                                return;
                                            }*/
                                            // zaustavlja perulju ako se desifrovan tekst sastoji samo od brojeva i karaktera
                                            if(matcher.matches()){
                                                
                                                Server.exit(desifrova, keyString);
                                                return;
                                            }

                                        }catch (BadPaddingException ex){
                                            System.out.println("Thread No."+(a+1)+" "+ keyString);

                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }



    private String desifrujDES(String key, String encrypted) throws Exception{

        ///////KORAK 1/////

        byte[] keyBytes=key.getBytes();

        Base64.Decoder decoder=Base64.getDecoder();
        byte[] encryptedBytes=decoder.decode(encrypted);

        ///////KORAK 2/////

        KeySpec ks=new DESKeySpec(keyBytes);
        SecretKeyFactory skf=SecretKeyFactory.getInstance("DES");
        SecretKey sk=skf.generateSecret(ks);

        /////KORAK 3///// DESIFROVANJE

        Cipher cipher=Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, sk);
        byte[] plaintextBytes=cipher.doFinal(encryptedBytes);

        String plaintext=new String(plaintextBytes);

        return plaintext;






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

}