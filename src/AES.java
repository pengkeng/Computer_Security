import java.io.*;

public class AES {

    //替换表
    private static final int[][] S = new int[][]{
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    /**
     * 逆S盒
     */
    private static final int[][] INVERSE_S = new int[][]{
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
    };


    //轮常量，用于扩展密钥
    private static final int[][] Rcon = new int[][]{
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {0x80, 0x00, 0x00, 0x00},
            {0x1B, 0x00, 0x00, 0x00},
            {0x36, 0x00, 0x00, 0x00}

    };

    //记录扩展后的密钥 (加密)
    private int[][] CIPHER_KEY = new int[44][4];
    //查找表，用于计算gf乘法
    private int[] table = new int[256];
    private int[] arc_table = new int[256];
    private int[] inverse_table = new int[256];

    /**
     * 列混合要用到的矩阵
     */
    private static final int[][] colM = new int[][]{
            {2, 3, 1, 1},
            {1, 2, 3, 1},
            {1, 1, 2, 3},
            {3, 1, 1, 2}
    };

    /**
     * 逆列混合用到的矩阵
     */
    private static final int[][] deColM = new int[][]{
            {0xe, 0xb, 0xd, 0x9},
            {0x9, 0xe, 0xb, 0xd},
            {0xd, 0x9, 0xe, 0xb},
            {0xb, 0xd, 0x9, 0xe}
    };


    /**
     * aes 加密
     *
     * @param originalString
     * @param key
     * @return
     */
    public String aes(String originalString, String key) {

        StringBuilder stringBuilder = new StringBuilder();
        //初始化GF乘法表
        initGFTable();
        originalString = appendOriginalString(originalString);
        extendKey(key, CIPHER_KEY);//扩展密钥
        for (int i = 0; i < originalString.length(); i += 16) {

            String text = originalString.substring(i, i + 16);

            int[][] originalInt = convertStringToInt(text);

            addRoundKey(originalInt, 0);//一开始的轮密钥加

            for (int r = 1; r < 10; r++) {

                subBytes(originalInt);//字节代换
                shiftRows(originalInt);//行移位
                mixColumns(originalInt);//列混合
                addRoundKey(originalInt, r);//密钥加密
            }

            //第10轮
            subBytes(originalInt);//字节代换
            shiftRows(originalInt);//行移位
            addRoundKey(originalInt, 10);
            for (int k = 0; k < 4; k++) {
                for (int j = 0; j < 4; j++) {
                    stringBuilder.append(originalInt[j][k]).append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }


    /**
     * aes 解密
     *
     * @param cipherText
     * @param key
     * @return
     */
    public String des(String cipherText, String key) {

        StringBuilder stringBuilder = new StringBuilder();

        //初始化GF乘法表
        initGFTable();
        extendKey(key, CIPHER_KEY);

        //处理密文数据
        String[] cipherString = cipherText.split(" ");
        for (int i = 0; i < cipherString.length; i += 16) {

            String[] strings = new String[16];
            for (int j = 0; j < 16; j++) {
                strings[j] = cipherString[i + j];
            }

            int[][] cipherInt = convertCipherHexToInt(strings);

            addRoundKey(cipherInt, 10);//一开始的轮密钥加

            for (int r = 9; r >= 1; r--) {
                deSubBytes(cipherInt);

                deShiftRows(cipherInt);

                deMixColumns(cipherInt);

                int[][] wArray = new int[4][4];

                getArrayFrom4W(r, wArray);

                deMixColumns(wArray);

                addRoundTowArray(cipherInt, wArray);
            }

            deSubBytes(cipherInt);

            deShiftRows(cipherInt);

            addRoundKey(cipherInt, 0);


            for (int k = 0; k < 4; k++) {
                for (int j = 0; j < 4; j++) {
                    stringBuilder.append((char) cipherInt[j][k]);
                }
            }

        }
        return stringBuilder.toString();
    }

    /**
     * 字符串填充，保证长度为16的倍数
     *
     * @param originalString 原文
     * @return 扩展后的内容，后面补"！"；
     */
    private String appendOriginalString(String originalString) {
        int length = originalString.length();
        if (length % 16 != 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(originalString);
            for (int i = length; i < length / 16 * 16 + 16; i++) {
                stringBuffer.append("!");
            }
            originalString = stringBuffer.toString();
        }
        return originalString;
    }

    /**
     * 将两个矩阵异或运算
     *
     * @param cipherInt
     * @param wArray
     */
    private void addRoundTowArray(int[][] cipherInt, int[][] wArray) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cipherInt[i][j] ^= wArray[i][j];
            }
        }
    }

    /**
     * 获取轮钥加密的矩阵
     *
     * @param rcon
     * @param wArray
     */
    private void getArrayFrom4W(int rcon, int[][] wArray) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                wArray[j][i] = CIPHER_KEY[rcon * 4 + i][j];
            }
        }
    }

    /**
     * 矩阵列混淆（解密）
     *
     * @param cipherInt
     */
    private void deMixColumns(int[][] cipherInt) {
        int[][] tempArray = new int[4][4];
        int i, j;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                tempArray[i][j] = cipherInt[i][j];

        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                cipherInt[i][j] = GF_mul(deColM[i][0], tempArray[0][j]) ^ GF_mul(deColM[i][1], tempArray[1][j])
                        ^ GF_mul(deColM[i][2], tempArray[2][j]) ^ GF_mul(deColM[i][3], tempArray[3][j]);
            }
    }

    /**
     * 矩阵行位移（）解密
     *
     * @param cipherInt
     */
    private void deShiftRows(int[][] cipherInt) {
        for (int i = 1; i < 4; i++) {
            int[] temp = new int[4];
            for (int k = 0; k < 4; k++) {
                temp[k] = cipherInt[i][k];
            }
            int index = 3 - i % 4;
            for (int j = 3; j >= 0; j--) {
                cipherInt[i][j] = temp[index];
                index--;
                index = index == -1 ? 3 : index % 4;
            }
        }
    }

    /**
     * 字节替换（解密）
     *
     * @param cipherInt
     */
    private void deSubBytes(int[][] cipherInt) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cipherInt[i][j] = INVERSE_S[cipherInt[i][j] / 16][cipherInt[i][j] % 16];
            }
        }
    }

    /**
     * 初始化GF运算的查找表（列混淆）
     */
    private void initGFTable() {
        table[0] = 1;
        for (int i = 1; i < 256; i++) {
            table[i] = (table[i - 1] << 1) ^ table[i - 1];
            if ((0x100 & table[i]) > 1) {
                table[i] ^= 0x11B;//用到了前面说到的乘法技巧
            }
        }
        for (int i = 0; i < 255; ++i) {
            arc_table[table[i]] = i;
        }

        for (int i = 1; i < 256; ++i)//0没有逆元，所以从1开始
        {
            int k = arc_table[i];
            k = 255 - k;
            k %= 255;//m_table的取值范围为 [0, 254]
            inverse_table[i] = table[k];
        }
    }


    /**
     * GF乘法运算（列混淆）
     *
     * @param x
     * @param y
     * @return
     */
    private int GF_mul(int x, int y) {
        if (x * y == 0)
            return 0;
        return table[(arc_table[x] + arc_table[y]) % 255];
    }

    /**
     * 列混淆（加密）
     *
     * @param originalInt
     */
    private void mixColumns(int[][] originalInt) {
        int[][] tempArray = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tempArray[i][j] = originalInt[i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                originalInt[i][j] =
                        GF_mul(tempArray[0][j], colM[i][0]) ^
                                GF_mul(tempArray[1][j], colM[i][1]) ^
                                GF_mul(tempArray[2][j], colM[i][2]) ^
                                GF_mul(tempArray[3][j], colM[i][3]);
            }
        }
    }

    /**
     * 行移位（加密）
     *
     * @param originalInt
     */
    private void shiftRows(int[][] originalInt) {
        for (int i = 1; i < 4; i++) {
            int[] temp = new int[4];
            for (int k = 0; k < 4; k++) {
                temp[k] = originalInt[i][k];
            }
            int index = i % 4;
            for (int j = 0; j < 4; j++) {
                originalInt[i][j] = temp[index];
                index++;
                index = index % 4;
            }
        }
    }

    /**
     * 字节替换（加密）
     *
     * @param originalInt
     */
    private void subBytes(int[][] originalInt) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                originalInt[i][j] = S[originalInt[i][j] / 16][originalInt[i][j] % 16];
            }
        }
    }


    /**
     * 把输入字符转成4x4矩阵
     *
     * @param text
     * @return
     */
    private int[][] convertStringToInt(String text) {
        int[][] temp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j][i] = text.charAt(i * 4 + j);
            }
        }
        return temp;
    }


    /**
     * 把输入密文转成4x4矩阵
     *
     * @param text
     * @return
     */
    private int[][] convertCipherHexToInt(String[] text) {
        int[][] temp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j][i] = Integer.parseInt(text[i * 4 + j]);
            }
        }
        return temp;
    }


    /**
     * 轮密钥异或运算
     *
     * @param text
     * @param rcon
     */
    private void addRoundKey(int[][] text, int rcon) {
        int[][] Rconi = new int[4][4];
        for (int i = 0; i < 4; i++) {
            Rconi[i] = CIPHER_KEY[rcon * 4 + i];
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                text[j][i] = text[j][i] ^ Rconi[i][j];
            }
        }
    }

    /**
     * 扩展密钥
     *
     * @param key
     * @param w
     */
    private void extendKey(String key, int[][] w) {
        String[] stringW = new String[44];
        for (int i = 0; i < 4; i++) {
            stringW[i] = key.substring(i * 4, i * 4 + 4);
            for (int j = 0; j < 4; j++) {
                w[i][j] = stringW[i].charAt(j);
            }
        }

        int RconCount = 0;
        for (int i = 4; i < 44; i++) {
            if (i % 4 == 0) {
                int[] temp = T(w[i - 1], RconCount);
                RconCount++;
                for (int j = 0; j < 4; j++) {
                    w[i][j] = w[i - 4][j] ^ temp[j];
                }
            } else {
                for (int j = 0; j < 4; j++) {
                    w[i][j] = w[i - 4][j] ^ w[i - 1][j];
                }
            }
        }
    }

    /**
     * 密钥扩展T运算
     *
     * @param w
     * @param rconCount
     * @return
     */
    private int[] T(int[] w, int rconCount) {
        int[] w3 = new int[4];
        for (int i = 0; i < 4; i++) {
            w3[i] = w[i];
        }
        int temp = w3[0];
        for (int i = 0; i < 3; i++) {
            w3[i] = w3[i + 1];
        }
        w3[3] = temp;
        for (int i = 0; i < 4; i++) {
            int top = w3[i] / 16;
            int bottom = w3[i] % 16;
            w3[i] = S[top][bottom];
        }
        for (int i = 0; i < 4; i++) {
            w3[i] ^= Rcon[rconCount][i];
        }
        return w3;
    }


    /**
     * 加密文件
     *
     * @param path
     * @param key
     */
    public void aes_file(String path, String key) {
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
                String cipherText = aes(originalText, key);
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
     * @param key
     */
    public void des_file(String path, String key) {
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
                String originalText = des(cipherText, key);
                originalTextWriter.write(originalText + "\n");
            }
            cipherTextReader.close();
            originalTextWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
