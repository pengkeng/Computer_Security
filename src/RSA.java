import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private int e;
    private int d;
    private int n;

    public RSA() {
        int p = MillerRabin.getPrime();
        int q;
        do {
            q = MillerRabin.getPrime();
        } while (p == q);
        e = 2;
        while (!MillerRabin.isRelativePrime(e, (p - 1) * (q - 1))) {
            e++;
        }
        d = MillerRabin.multiplicativeInverse((p - 1) * (q - 1), e);
        n = p * q;
    }

    public String encrypt(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            stringBuilder.append(MillerRabin.Square_and_Multiply(str.charAt(i), e, n) + " ");
        }
        return stringBuilder.toString();
    }

    public String decrypt(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = str.split(" ");
        for (int i = 0; i < strings.length; i++) {
            int x = MillerRabin.Square_and_Multiply(Integer.valueOf(strings[i], 10), e, n);
            stringBuilder.append(x);
        }
        return stringBuilder.toString();
    }


    public int encrypt(int c) {
        int E = e;
        int N= n;
        return MillerRabin.Square_and_Multiply(c, E, N);
    }

    public int decrypt(int m) {
        int D = d;
        int N= n;
        return MillerRabin.Square_and_Multiply(m, D, N);
    }
}
