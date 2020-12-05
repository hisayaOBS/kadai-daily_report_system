package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

//SHA256ハッシュ化クラス
public class EncryptUtil {

    public static String getPasswordEncrypt(String plain_p, String pepper) {
        String ret = "";

        //引数で受け取ったパスワードに値が設定されていれば、ペッパー文字列を使いハッシュ化する
        if (plain_p != null && !plain_p.equals("")) {
            byte[] bytes; //byte(8bit)型配列を生成(アルファベット数字記号は1文字1byteなので)

            String password = plain_p + pepper;
            try {
                //暗号方式SHA-256のアルゴリズムを提供するMessageDigestクラスのインスタンスを取得
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                //pass+ペッパー文字列の値をgetBytesメソッドでバイト型配列に変換してdigestメソッドの引数に渡している
                //digestメソッドはバイト型配列を受け取り、暗号化した文字列64文字をバイト型配列として返す
                bytes = md.digest(password.getBytes());
                ret = DatatypeConverter.printHexBinary(bytes);// バイト型配列を文字列に変換
            } catch (NoSuchAlgorithmException ex) {
            }
        }
        return ret;
    }
}
