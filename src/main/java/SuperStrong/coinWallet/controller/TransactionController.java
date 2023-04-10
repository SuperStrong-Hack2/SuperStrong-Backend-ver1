package SuperStrong.coinWallet.controller;


import SuperStrong.coinWallet.repository.MemberRepository;
import SuperStrong.coinWallet.service.MemberService;
import SuperStrong.coinWallet.service.TransactionService;
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

    @PostMapping("/api/send")
    public Object register(@RequestParam HashMap<String, Object> data){
        return transactionService.send(data);
    }

    @PostMapping("/api/send_input") //토큰 유효성 검사, 인터셉트 필요******
    public Object sendInput(@RequestBody HashMap<String, Object> data) {
        return transactionService.calculate(data);
    }
        //return encryption.getAES256encode(transaction.calculate(encryption.getAES256decode(data)));

        //id, token 넘겨받음. pub_address도 넘겨받나?
//        JSONObject jsonObject = new JSONObject(data);
//        System.out.println(jsonObject);
//
//        String to_address = (String) data.get("to_address");
//        System.out.println(to_address);
//        List<Object> calculated = transactionService.calculate(data);
//        Map<String, Object> responseMap = new HashMap<String, Object>();
//
//        if (!(memberRepository.existsByPubAddress(to_address))) {
//            responseMap.put("invalid input", "no destination");
//            JSONObject res = new JSONObject(responseMap);
//            return ResponseEntity.ok(res);
//        }
//        if (calculated != null) {
//            responseMap.put("to_address", to_address);
//            responseMap.put("remain_amount", calculated.get(0));
//            responseMap.put("calculated_gas", calculated.get(1));
//            responseMap.put("send_amount", calculated.get(2));
//            responseMap.put("coin_name", calculated.get(3));
//        }
//        if (!responseMap.isEmpty()) {
//            JSONObject res = new JSONObject(responseMap);
//            return ResponseEntity.ok(res);
//        }
//        else {
//            responseMap.put("invalid input", "not enough assets");
//            JSONObject res = new JSONObject(responseMap);
//            return ResponseEntity.ok(res);
//        }
//    }
}