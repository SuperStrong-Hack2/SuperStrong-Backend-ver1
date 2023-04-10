package SuperStrong.coinWallet.validation;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.HashMap;

@Service
public class Encryption {
    /** ======= [aes128 비밀키 전역 변수 선언] ======= **/
    public static String aes256SecretKey = "01234567890123450123456789012345"; //TODO [aes128 = 16 byte / aes192 = 24 byte / aes256 = 32 byte]
    //    public static String aes128SecretKey = "7m8z98e80y93buzw"; //TODO [aes128 = 16 byte / aes192 = 24 byte / aes256 = 32 byte]
    public static byte[] aes256ivBytes = "0123456789012345".getBytes(); // TODO [일반 사용 방식]
    //public static byte[] aes128ivBytes = "0123456789abcdef".getBytes(); // TODO [16 byte Enter IV (Optional) 지정 방식]
//    private static byte[] ivBytes = "0123456789012345".getBytes();



    /** ======= [aes128 비밀키 사용해 인코딩 수행] ======= **/
    public Object getAES256encode (Object data){
        String res = String.valueOf(data);
        HashMap<String, String> result = new HashMap<>();

        try {
            byte[] textBytes = res.getBytes("UTF-8");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes256ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(aes256SecretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

            String result2 = Base64.getEncoder().encodeToString(cipher.doFinal(textBytes));
            result.put("e2e_res", result2);
            System.out.println(result);

            return new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /** ======= [aes128 비밀키 사용해 디코딩 수행] ======= **/
    public JSONObject getAES256decode (HashMap<String, Object> data){

        JSONObject jsonObject = new JSONObject(data);
        String req = ((String) jsonObject.get("e2e_req")).replace("\n", "");

        try {
            // TODO [인풋으로 들어온 base64 문자열 데이터를 가지고 디코딩 수행]
            byte[] textBytes = Base64.getDecoder().decode(req.getBytes());
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes256ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(aes256SecretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

            String result = new String(cipher.doFinal(textBytes), "UTF-8");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(result);
            JSONObject json = (JSONObject) obj;
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
