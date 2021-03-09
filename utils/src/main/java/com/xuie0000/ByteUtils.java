package com.xuie0000;

/**
 * char在java中是2个字节。java采用unicode，2个字节（16位）来表示一个字符。
 *
 * @author Jie Xu
 * @date 2019/8/8
 */
public class ByteUtils {
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static char byteToChar(byte[] b) {
        return (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
    }
}
