import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

public class RSA {

    private static double e;
    private static double d;
    private static double n;

    private static BigInteger E;
    private static BigInteger D;
    private static BigInteger N;
    private static BigInteger F;


    /**
     * 初始化生成共钥和密钥
     */
    RSA() {
        init();
        BigIntegerInit();
    }


    /**
     * int类型数据初始化
     */
    private void init() {
        double p = MillerRabin.getPrime();
        double q;
        do {
            q = MillerRabin.getPrime();
        } while (p == q);
        while (true) {
            e = new Random().nextInt(100);
            if (MillerRabin.isRelativePrime(e, MillerRabin.Euler_totient(p, q)) && e < MillerRabin.Euler_totient(p, q) && e > 1) {
                break;
            }
        }
        d = MillerRabin.multiplicativeInverse(MillerRabin.Euler_totient(p, q), e);
        n = p * q;
    }


    /**
     * 大数初始化
     */
    private void BigIntegerInit() {
        Random rnd = new Random(new Date().getTime());
        BigInteger p = BigInteger.probablePrime(1024, rnd);
        BigInteger q = BigInteger.probablePrime(1024, rnd);
        N = p.multiply(q);
        F = MillerRabin.Euler_totient(p, q);
        int x;
        while (true) {
            x = new Random().nextInt(1000);
            E = new BigInteger(String.valueOf(x));
            if (MillerRabin.isRelativePrime(E, F) && E.compareTo(F) < 0 && x > 1) {
                break;
            }
        }
        D = MillerRabin.multiplicativeInverse(F, E);
    }


    /**
     * 字符串小数加密
     *
     * @param str
     * @return
     */
    public String encrypt(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            stringBuilder.append(MillerRabin.quick_mod(str.charAt(i), e, n) + " ");
        }
        return stringBuilder.toString();
    }

    /**
     * 字符串小数解密
     *
     * @param str
     * @return
     */
    public String decrypt(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = str.split(" ");
        for (String string : strings) {
            double x = MillerRabin.quick_mod(Integer.valueOf(string, 10), d, n);
            stringBuilder.append((char) x);
        }
        return stringBuilder.toString();
    }


    /**
     * 数字加密
     *
     * @param c
     * @return
     */
    public double encrypt(double c) {
        return (double) MillerRabin.quick_mod(c, e, n);
    }

    /**
     * 数字解密
     *
     * @param m
     * @return
     */
    public double decrypt(double m) {
        return (double) MillerRabin.quick_mod(m, d, n);
    }


    /**
     * 大数加密
     *
     * @param c
     * @return
     */
    public BigInteger encryptBigInteger(BigInteger c) {
        return c.modPow(E, N);
    }

    /**
     * 大数解密
     *
     * @param m
     * @return
     */
    public BigInteger decryptBigInteger(BigInteger m) {
        return m.modPow(D, N);
    }

    /**
     * 字符串加密
     *
     * @param str
     * @return
     */
    public String encryptBigInteger(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            stringBuilder.append(MillerRabin.quick_mod(new BigInteger(String.valueOf((int) str.charAt(i))), E, N) + " ");
        }
        return stringBuilder.toString();
    }


    /**
     * 字符串解密
     *
     * @param str
     * @return
     */
    public String decryptBigInteger(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = str.split(" ");
        for (String string : strings) {
            double x = MillerRabin.quick_mod(new BigInteger(string), D, N).doubleValue();
            stringBuilder.append((char) x);
        }
        return stringBuilder.toString();
    }


    /**
     * 加密文件
     *
     * @param path
     */
    public void encrypt_file(String path) {
        File file = new File(path);

        //创建加密文件
        String cipherFileName = file.getPath().replace(".txt", "_cipher.txt");
        File cipherFile = new File(cipherFileName);
        cipherFile.delete();

        try {
            cipherFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取文件加密并写入文件
        try {
            BufferedReader originalTextWriter = new BufferedReader(new FileReader(file));
            BufferedWriter cipherTextReader = new BufferedWriter(new FileWriter(cipherFile));
            String originalText = null;
            while ((originalText = originalTextWriter.readLine()) != null) {
                String cipherText = encryptBigInteger(originalText);
                cipherTextReader.write(cipherText + "\n");
            }
            originalTextWriter.close();
            cipherTextReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解密文件
     *
     * @param path
     */
    public void decrypt_file(String path) {
        File cipherFile = new File(path);

        //创建解密文件
        String newFileName = cipherFile.getPath().replace(".txt", "_des.txt");
        File newFile = new File(newFileName);
        newFile.delete();

        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取文件加密并写入文件
        try {
            BufferedReader cipherTextReader = new BufferedReader(new FileReader(cipherFile));
            BufferedWriter originalTextWriter = new BufferedWriter(new FileWriter(newFile));
            String cipherText = null;
            while ((cipherText = cipherTextReader.readLine()) != null) {
                String originalText = decryptBigInteger(cipherText);
                originalTextWriter.write(originalText + "\n");
            }
            cipherTextReader.close();
            originalTextWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
