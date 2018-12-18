public class Main {

    public static void main(String[] args) {
        AES aes = new AES();
        String originalString = "hello! my name is pengqiancheng,nice to meet you!";
        String cipherText = aes.aes(originalString, "qazwsxedcrfvtgby");
        System.out.println("密文:" + cipherText);
        System.out.println("解密后：" + aes.des(cipherText, "qazwsxedcrfvtgby"));
//
//        aes.aes_file("/Users/penney/Desktop/a.txt", "qazwsxedcrfvtgby");
//        aes.des_file("/Users/penney/Desktop/a_cipher.txt", "qazwsxedcrfvtgby");
    }
}
