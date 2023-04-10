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
    public Object register(@RequestBody HashMap<String, Object> data) throws ParseException {
        return encryption.getAES256encode(memberService.signup(encryption.getAES256decode(data)));
    }

    @PostMapping("/api/register/auth")
    public Object register_auth(@RequestBody HashMap<String, Object> data) throws ParseException {
        return encryption.getAES256encode(memberService.signup_auth(encryption.getAES256decode(data)));
    }

    @PostMapping("/api/login")
    public Object login(@RequestBody HashMap<String, Object> data) throws ParseException {
        return encryption.getAES256encode(memberService.loginUser(encryption.getAES256decode(data)));
    }
}
