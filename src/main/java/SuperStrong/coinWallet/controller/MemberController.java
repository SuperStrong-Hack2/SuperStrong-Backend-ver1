package SuperStrong.coinWallet.controller;


import SuperStrong.coinWallet.service.MemberService;
import SuperStrong.coinWallet.validation.Encryption;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberController {

    @Autowired
    Encryption encryption;
    @Autowired
    MemberService memberService;
    @PostMapping("/api/register")
    public Object register(@RequestBody HashMap<String, Object> data){
        return memberService.signup(data);
    }

    @PostMapping("/api/register/auth")
    public Object register_auth(@RequestBody HashMap<String, Object> data){
        return memberService.signup_auth(data);
    }

    @PostMapping("/api/login")
    public Object login(@RequestBody HashMap<String, Object> data) throws ParseException {
        return encryption.getAES256encode(memberService.loginUser(encryption.getAES256decode(data)));
    }

//    @PostMapping("/api/tokentest") // 토큰 유효성 체크 함수를 검사하는 것. 나중에 없앰
//    public String testToken(@RequestBody HashMap<String, Object> data) {
//        String token = (String) data.get("token");
//        String pub_address = (String) data.get("pub_address");
//        boolean isValid = JwtUtil.CheckTokenValid(token, pub_address);
//        if (isValid) {
//            System.out.println("Valid token");
//            return "Valid token";
//        } else {
//            System.out.println("Invalid token");
//            return "Invalid token";
//        }
//    }



}
