package SuperStrong.coinWallet.jwttoken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkey";

    public static String generateToken(String id, String pub_address) { //id 파라미터는 데이터베이스에서 id 정보를 받아온다.
        String combinedKey = SECRET_KEY + pub_address;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setId(id)
                .setSubject(pub_address)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 3600000))
                .signWith(SignatureAlgorithm.HS256, combinedKey)
                .compact();
    }

    public static boolean CheckTokenValid(String token, String pub_address) {
        try {
            String combinedKey = SECRET_KEY + pub_address;
            Jwts.parser().setSigningKey(combinedKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Handle expired token exception
        } catch (JwtException e) {
            // Handle invalid token exception
        }
        return false;
    }



}
