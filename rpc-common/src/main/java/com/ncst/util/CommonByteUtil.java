package com.ncst.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * @author Lsy
 * @date 2022/1/13
 */
public class CommonByteUtil {

    private CommonByteUtil() {}

    public static byte[] short2Byte(short value) {
        byte[] bRe = new byte[2];
        bRe[0] = (byte) (0XFF & (value >> 8));
        bRe[1] = (byte) (0XFF & value);
        return bRe;
    }

    public static short byte2Short(byte[] iBytes) {
        short vRe = 0;
        if (iBytes.length != 2) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is not 2.");
        }
        return (short) (((vRe | (0xFF & iBytes[0])) << 8) | (0xFF & iBytes[1]));
    }

    public static short byte2Short(byte[] iBytes, int loc) {
        short vRe = 0;
        if (iBytes.length < loc + 2) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is not 2.");
        }
        return (short) (((vRe | (0xFF & iBytes[loc])) << 8) | (0xFF & iBytes[loc + 1]));
    }

    public static byte[] int2Byte(int value) {
        byte[] bRe = new byte[4];
        bRe[0] = (byte) (0XFF & (value >> 24));
        bRe[1] = (byte) (0XFF & (value >> 16));
        bRe[2] = (byte) (0XFF & (value >> 8));
        bRe[3] = (byte) (0XFF & value);
        return bRe;
    }

    public static int byte2Int(byte[] iBytes) {
        int vRe = 0;
        if (iBytes.length != 4) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is not 4.");
        }
        return ((((((vRe |
                (0xFF & iBytes[0])) << 8) |
                (0xFF & iBytes[1])) << 8) |
                (0xFF & iBytes[2])) << 8) |
                (0xFF & iBytes[3]);
    }

    public static int byte2Int(byte[] iBytes, int loc) {
        int vRe = 0;
        if (iBytes.length < loc + 4) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is not 4.");
        }
        return ((((((vRe | (0xFF & iBytes[loc])) << 8) | (0xFF & iBytes[loc + 1])) << 8) | (0xFF & iBytes[loc + 2])) << 8) | (0xFF & iBytes[loc + 3]);
    }

    public static byte[] long2Byte(long value) {
        byte[] bRe = new byte[8];
        bRe[0] = (byte) (0XFF & (value >> 56));
        bRe[1] = (byte) (0XFF & (value >> 48));
        bRe[2] = (byte) (0XFF & (value >> 40));
        bRe[3] = (byte) (0XFF & (value >> 32));
        bRe[4] = (byte) (0XFF & (value >> 24));
        bRe[5] = (byte) (0XFF & (value >> 16));
        bRe[6] = (byte) (0XFF & (value >> 8));
        bRe[7] = (byte) (0XFF & value);
        return bRe;
    }

    public static long byte2Long(byte[] iBytes) {
        long vRe = 0;
        if (iBytes.length != 8) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is not 8.");
        }
        return ((((((((((((((vRe | (0xFF & iBytes[0])) << 8) | (0xFF & iBytes[1])) << 8) | (0xFF & iBytes[2])) << 8) | (0xFF & iBytes[3])) << 8) | (0xFF & iBytes[4])) << 8) | (0xFF & iBytes[5])) << 8) | (0xFF & iBytes[6])) << 8) | (0xFF & iBytes[7]);
    }

    public static long byte2Long(byte[] iBytes, int offset) {
        long vRe = 0;
        if (iBytes.length < 8) {
            throw new IllegalArgumentException("Incorrect parameter, the length of the byte array passed in is less than 8.");
        }
        return ((((((((((((((vRe | (0xFF & iBytes[offset + 0])) << 8) | (0xFF & iBytes[offset + 1])) << 8) | (0xFF & iBytes[offset + 2])) << 8) | (0xFF & iBytes[offset + 3])) << 8) | (0xFF & iBytes[offset + 4])) << 8) | (0xFF & iBytes[offset + 5])) << 8) | (0xFF & iBytes[offset + 6])) << 8) | (0xFF & iBytes[offset + 7]);
    }

    /**
     * @param data   原始数据
     * @param offset 从哪个字节开始取
     * @param length 要取数据的长度
     * @return 截取数据字节数组
     */
    public static byte[] subByteArray(byte[] data, int offset, int length) {
        byte[] dataRe = new byte[length];
        System.arraycopy(data, offset, dataRe, 0, length);
        return dataRe;
    }

    public static int ip4ToInt(String ipAddr) {

        String[] parts = ipAddr.split(Pattern.quote("."));

        int vRe = 0;
        vRe = (vRe | Integer.parseInt(parts[0])) << 8;
        vRe = (vRe | Integer.parseInt(parts[1])) << 8;
        vRe = (vRe | Integer.parseInt(parts[2])) << 8;
        vRe = (vRe | Integer.parseInt(parts[3]));

        return vRe;
    }

    public static String ip4ToStr(int ipAddr) {

        String sRe = "";

        sRe += String.valueOf((ipAddr >> 24) & 0X00FF) + ".";
        sRe += String.valueOf((ipAddr >> 16) & 0X00FF) + ".";
        sRe += String.valueOf((ipAddr >> 8) & 0X00FF) + ".";
        sRe += String.valueOf(ipAddr & 0X00FF);

        return sRe;
    }

    public static void readData(InputStream is, byte[] data) throws IOException {
        int len = 0;
        while (len < data.length) {
            int lenTmp = is.read(data, len, data.length - len);
            if (lenTmp == -1) {
                throw new IllegalArgumentException("No enough data for the buffer.");
            } else {
                len += lenTmp;
            }
        }
    }
}
