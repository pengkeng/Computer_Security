import java.awt.desktop.SystemSleepEvent;
import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {
    private static final int ORDER = 10000;// 随机数的数量级
    private static final int MIN = 1000; // 选择的随机数的最小值

    /**
     * 快速幂计算指数模运算 a^m % n
     *
     * @param a 底数
     * @param b 指数
     * @param n 被mod数
     * @return
     */
    public static long quick_mod(double a, double b, double n) {

        double s = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                s = s % n;
                a = a % n;
                s = s * a;
            }
            a = a % n;
            a = a * a;
            b = b / 2;
        }
        return (long) (s % n);
    }

    /**
     * 快速幂计算指数模运算 a^m % n
     *
     * @param a 底数
     * @param b 指数
     * @param n 被mod数
     * @return
     */
    public static BigInteger quick_mod(BigInteger a, BigInteger b, BigInteger n) {
        return a.modPow(b, n);
    }

    /**
     * 随机选择一个奇数
     */
    public static int getRandom() {
        int x = 3;
        Random rd = new Random();
        do {
            x = rd.nextInt(ORDER);
        } while (x < MIN || x % 2 == 0);
        return x;
    }

    /**
     * 验证一个数是否为素数，将n-1改写为2^k * m的形式，其中m是奇数，在{2,...,n-1}中随机选取一个整数a;
     *
     * @param n
     * @return 如果是素数返回true, 否则返回false
     */
    public static boolean isPrime(int n) {
        int[] arr = intTOIndex(n - 1);// n-1 用2的幂表示
        int k = arr[0];
        int m = arr[1];
        Random r = new Random();// 在{2,...,n-1}随机选择一个整数a
        int a = 0;
        do {
            a = r.nextInt(n - 1);
        } while (a < 2);
        int b = (int) quick_mod(a, m, n);
        if (b == 1) return true;
        for (int i = 0; i < k; i++) {
            if (b == (n - 1)) return true;
            else b = (b * b) % n;
        }
        return false;
    }

    /**
     * 将一个数改为2^k * m的形式，其中m是奇数
     *
     * @param n
     * @return arr[0]=k,arr[1]=m
     */
    public static int[] intTOIndex(int n) {
        int[] arr = new int[2];
        int k = 0;
        int x;
        // 当n为奇数是停止循环
        do {
            k++;
            n >>= 1;
            x = n & 1;
        } while (x == 0);
        arr[0] = k;
        arr[1] = n;
        return arr;
    }

    /**
     * 获取一个随机数为并且检查其为素数
     *
     * @return
     */
    public static double getPrime() {
        int x = 0;
        while (x % 2 == 0 || !isPrime(x)) {
            x = getRandom();
        }
        return x;
    }

    /**
     * 判断两个数是否互质
     */

    public static boolean isRelativePrime(double a, double b) {
        if (a < b) {
            double temp = a;
            a = b;
            b = temp;
        }
        return gcd(a, b) == 1;
    }

    /**
     * 判断两个数是否互质
     */

    public static boolean isRelativePrime(BigInteger a, BigInteger b) {
        if (a.compareTo(b) < 0) {
            BigInteger temp = a;
            a = b;
            b = temp;
        }
        return a.gcd(b).equals(new BigInteger("1"));
    }

    /**
     * 辗转相除法求最大公约素
     *
     * @param a
     * @param b
     * @return
     */
    private static double gcd(double a, double b) {
        if (b == 0) return a;
        else return gcd(b, a % b);
    }


    /**
     * 求两个数的乘法逆元
     *
     * @param n
     * @param b
     * @return
     */
    public static double multiplicativeInverse(double n, double b) {
        if (gcd(n, b) != 1) {
            return 0;
        }
        double r1 = n, r2 = b, t1 = 0, t2 = 1, q, r, t;
        while (r2 > 0) {
            q = r1 / r2;
            r = r1 - q * r2;
            r1 = r2;
            r2 = r;
            t = t1 - q * t2;
            t1 = t2;
            t2 = t;
        }
        if (t1 < 0) {
            t1 += n;
        }
        return t1;
    }


    /**
     * 求两个数的乘法逆元
     *
     * @param n
     * @param b
     * @return
     */
    public static BigInteger multiplicativeInverse(BigInteger n, BigInteger b) {
        BigInteger r1 = new BigInteger(String.valueOf(n)), r2 = new BigInteger(String.valueOf(b)), t1 = new BigInteger("0"), t2 = new BigInteger("1"), q, r, t;
        while (r2.compareTo(new BigInteger("0")) > 0) {
            q = r1.divide(r2);
            r = r1.subtract(q.multiply(r2));
            r1 = r2;
            r2 = r;
            t = t1.subtract(q.multiply(t2));
            t1 = t2;
            t2 = t;
        }
        if (t1.compareTo(new BigInteger("0")) < 0) {
            t1 = t1.add(n);
        }
        return t1;
    }


    /**
     * 计算两个质数的欧拉函数
     *
     * @param p
     * @param q
     * @return
     */
    public static double Euler_totient(double p, double q) {
        return (p - 1) * (q - 1);
    }

    /**
     * 计算两个质数的欧拉函数
     *
     * @param p
     * @param q
     * @return
     */
    public static BigInteger Euler_totient(BigInteger p, BigInteger q) {
        return p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
    }

}