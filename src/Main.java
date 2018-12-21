import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

//        AES aes = new AES();
//        String originalString = "hello! my name is pengqiancheng,nice to meet you!";
//        String cipherText = aes.aes(originalString, "qazwsxedcrfvtgby");
//        System.out.println("密文:" + cipherText);
//        System.out.println("解密后：" + aes.des(cipherText, "qazwsxedcrfvtgby"));
//
//        aes.aes_file("/Users/penney/Desktop/a.txt", "qazwsxedcrfvtgby");
//        aes.des_file("/Users/penney/Desktop/a_cipher.txt", "qazwsxedcrfvtgby");


        RSA rsa = new RSA();
       // System.out.println("解密后：" + rsa.decryptBigInteger(rsa.encryptBigInteger("hello my name is pengqiancheng")));
        rsa.encrypt_file("/Users/penney/Desktop/a.txt");
        rsa.decrypt_file("/Users/penney/Desktop/a_cipher.txt");
    }
}
