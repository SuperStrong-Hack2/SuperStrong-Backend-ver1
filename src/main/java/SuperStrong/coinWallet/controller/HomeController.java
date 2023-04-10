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
//        List<Double> mem_wallet = memberService.assetInfo(data);    //지갑 내부 정보를 리스트로 받아옴
//        Map<String, Object> walletMap = new HashMap<String, Object>();
//        if (mem_wallet != null && mem_wallet.size() >= 3) {
//            walletMap.put("eth", mem_wallet.get(0));
//            walletMap.put("btc", mem_wallet.get(1));
//            walletMap.put("doge", mem_wallet.get(2));
//
//            System.out.println(walletMap);
//            JSONObject res = new JSONObject(walletMap);
//
//            return ResponseEntity.ok(res);
//        }
//        else {
//            walletMap.put("walletinfo", "no wallet info");
//            JSONObject res = new JSONObject(walletMap);
//            System.out.println(res);
//            return ResponseEntity.ok(res);
//        }
    }

    @PostMapping("/api/main_history")
    public Object main_history(@RequestBody HashMap<String, Object> data) {
//        System.out.println(data);
        return memberService.historyInfo(data);
//        String id = (String) data.get("id");
//        List<Object> mem_history = memberService.historyInfo(data);
//        Map<String, Object> responseMap = new HashMap<String, Object>();
//
//        if (mem_history != null) {
//            List<Map<String, Object>> historyList = new ArrayList<>();
//            for (Object obj : mem_history) {
//                History history = (History) obj;
//                if (history.getMember().getMemberId().equals(id)) {
//                    Map<String, Object> historyMap = new HashMap<>();
//                    historyMap.put("history_id", history.getHistoryId());
//                    historyMap.put("member_id", history.getMember().getMemberId());
//                    historyMap.put("status", history.getStatus());
//                    historyMap.put("interaction_id", history.getInteractionId());
//                    historyMap.put("coin_name", history.getCoinName());
//                    historyMap.put("amount", history.getAmount());
//                    historyMap.put("quote", history.getQuote());
//                    historyMap.put("gas", history.getGas());
//                    historyList.add(historyMap);
//                }
//            }
//            if (!historyList.isEmpty()) {
//                responseMap.put("history", historyList);
//                JSONObject res = new JSONObject(responseMap);
//                return ResponseEntity.ok(res);
//            }
//        }
//        responseMap.put("history", "no history data");
//        JSONObject res = new JSONObject(responseMap);
//        System.out.println(res);
//        return ResponseEntity.ok(res);
    }
}