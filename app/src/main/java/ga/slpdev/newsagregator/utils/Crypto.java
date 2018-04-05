package ga.slpdev.newsagregator.utils;

import java.security.MessageDigest;

public class Crypto {
    public static String Sha1(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] textBytes = data.getBytes("utf8");
            md.update(textBytes, 0, textBytes.length);
            byte[] hash = md.digest();
            return  hexString(hash);
        } catch (Exception e) {
            return "";
        }
    }
    private static String hexString(byte[] hash) {
        StringBuilder buf = new StringBuilder();
        for (byte b : hash) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
