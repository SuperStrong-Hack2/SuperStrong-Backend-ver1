package SuperStrong.coinWallet.controller;


import SuperStrong.coinWallet.repository.MemberRepository;
import SuperStrong.coinWallet.service.MemberService;
import SuperStrong.coinWallet.service.TransactionService;
import SuperStrong.coinWallet.validation.Encryption;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    Encryption encryption;

    @PostMapping("/api/send")
    public Object register(@RequestParam HashMap<String, Object> data){
        //return encryption.getAES256encode(transactionService.send(encryption.getAES256decode(data)));

        return transactionService.send(data);
    }

    @PostMapping("/api/send_input") //토큰 유효성 검사, 인터셉트 필요******
    public Object sendInput(@RequestBody HashMap<String, Object> data) {
        return transactionService.calculate(data);
    }
        //return encryption.getAES256encode(transaction.calculate(encryption.getAES256decode(data)));


}