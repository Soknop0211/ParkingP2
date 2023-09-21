package com.daikou.p2parking.sdk;

public class Util {

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length() / 2;
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            int index = i * 2;
            int byteValue = Integer.parseInt(hexString.substring(index, index + 2), 16);
            data[i] = (byte) byteValue;
        }
        return data;
    }

    public static String bytesToHexString(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0'); // Padding for single digit hex values
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
