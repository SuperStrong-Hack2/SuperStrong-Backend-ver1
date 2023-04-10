package SuperStrong.coinWallet.controller;


import SuperStrong.coinWallet.entity.History;
import SuperStrong.coinWallet.service.MemberService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {
    @Autowired
    MemberService memberService;

    @PostMapping("/api/main_asset") // id와 jwt 받아옴, jwt에 대한 유효성 검사 intercept 필요함
    public Object main_asset(@RequestBody HashMap<String, Object> data) {
//        System.out.println(data);
        return memberService.assetInfo(data);

    }

    @PostMapping("/api/main_history")
    public Object main_history(@RequestBody HashMap<String, Object> data) {
//        System.out.println(data);
        return memberService.historyInfo(data);
    }
}