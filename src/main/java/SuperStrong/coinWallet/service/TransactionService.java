package SuperStrong.coinWallet.service;

import SuperStrong.coinWallet.entity.Member;
import SuperStrong.coinWallet.entity.Wallet;
import SuperStrong.coinWallet.repository.MemberRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

@Service
public class TransactionService {
    private final String url = "jdbc:mysql://localhost:3306/wallet_test";
    private final String username = "root";
    private final String password = "dnjstjr11!";
    @Autowired
    private MemberRepository memberRepository;

    public Object calculate(HashMap<String, Object> data) {
        Double iRate = 0.05;
        HashMap<String, Object> ret_list = new HashMap<String, Object>();
        String id = (String) data.get("id");
        String coin_name = (String) data.get("coin_name");
        Double send_amount = (Double) Double.valueOf(data.get("send_amount").toString());
        Optional<Member> optionalMember = memberRepository.findById(id);

        String to_address = (String) data.get("to_address");
        if (!(memberRepository.existsByPubAddress(to_address))) {
            HashMap<String, Object> res_map = new HashMap<String, Object>();
            res_map.put("validation", "invalid input");
            res_map.put("content", "no destination");
            return new JSONObject(res_map);
        }

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            Wallet memberwallet = member.getWallet();
            Double calculated_gas = 0.0;
            if (coin_name.equals("eth") || coin_name.equals("ETH")) {
                Double remain_amount = memberwallet.getEthAmount();
                calculated_gas = send_amount * iRate;
                if (remain_amount - send_amount - calculated_gas < 0) {
                    //보유 자산 부족 에러
                    HashMap<String, Object> res_map = new HashMap<String, Object>();
                    res_map.put("validation", "invalid input");
                    res_map.put("content", "not enough assets");
                    return new JSONObject(res_map);
                }
                remain_amount = remain_amount - send_amount - calculated_gas;
                ret_list.put("remain_amount", remain_amount);
            }
            else if (coin_name.equals("btc") || coin_name.equals("BTC")) {
                Double remain_amount = memberwallet.getBtcAmount();
                calculated_gas = send_amount * iRate;
                if (remain_amount - send_amount - calculated_gas < 0) {
                    //보유 자산 부족 에러
                    HashMap<String, Object> res_map = new HashMap<String, Object>();
                    res_map.put("validation", "invalid input");
                    res_map.put("content", "not enough assets");
                    return new JSONObject(res_map);
                }
                remain_amount = remain_amount - send_amount - calculated_gas;
                ret_list.put("remain_amount", remain_amount);
            }
            else {
                Double remain_amount = memberwallet.getDogeAmount();
                calculated_gas = send_amount * iRate;
                if (remain_amount - send_amount - calculated_gas < 0) {
                    //보유 자산 부족 에러
                    HashMap<String, Object> res_map = new HashMap<String, Object>();
                    res_map.put("validation", "invalid input");
                    res_map.put("content", "not enough assets");
                    return new JSONObject(res_map);
                }
                remain_amount = remain_amount - send_amount - calculated_gas;
                ret_list.put("remain_amount", remain_amount);
            }
            ret_list.put("calculated_gas", calculated_gas);
            ret_list.put("send_amount", send_amount);
            ret_list.put("coin_name", coin_name);
            ret_list.put("to_address", to_address);
            ret_list.put("validation", "valid input");
        }

//        HashMap<String, Object> res_list = new HashMap<String, Object>()
//        res_list.put("to_address", to_address);
//        res_list.put("remain_amount", ret_list.get(0));
//        res_list.put("calculated_gas", ret_list.get(1));
//        res_list.put("send_amount", ret_list.get(2));
//        res_list.put("coin_name", ret_list.get(3));
        return new JSONObject(ret_list);
    }


    public Object send(HashMap<String, Object> data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            HashMap<String, String> res = new HashMap<>();

            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt1 = conn.createStatement();

            if (Objects.equals(jsonObject.get("coin"), "eth") || Objects.equals(jsonObject.get("coin"), "btc") || Objects.equals(jsonObject.get("coin"), "doge")) {
                double coin = Double.parseDouble((String) jsonObject.get("amount"));
                double gas = Double.parseDouble((String) jsonObject.get("gas"));
                System.out.println(coin);
                String query1 = String.format("UPDATE member_wallet SET %s_amount = %s_amount - %f WHERE member_id = '%s';", jsonObject.get("coin"), jsonObject.get("coin"), coin, jsonObject.get("id"));
                stmt1.executeUpdate(query1);

                String query2 = String.format("UPDATE server_wallet SET %s_amount = %s_amount + %f;", jsonObject.get("coin"), jsonObject.get("coin"), gas);
                stmt1.executeUpdate(query2);

                String query3 = String.format("UPDATE member_wallet SET %s_amount = %s_amount + %f WHERE wallet_id = '%s';", jsonObject.get("coin"), jsonObject.get("coin"), coin, jsonObject.get("pub_address"));
                stmt1.executeUpdate(query3);

                // TODO quote 변동 추가
                double quote = 1500000;
                String status = "send";

//                Optional<Member> member = memberRepository.findById((String) jsonObject.get("id"));
//                System.out.println(member.get().getMemberId());
//                System.out.println(member.get().getPw());
//
//                String query4 = String.format("INSERT INTO history (status, member_id, interaction_id, coin_name, amount, quote, gas) VALUES ('%s', '%s', '%s', 'doge', 10, 160000, 1.5);", status, jsonObject.get("id"), jsonObject.get("coin"), coin, jsonObject.get("pub_address"));
//                stmt1.executeUpdate(query4);


                stmt1.close();
                conn.close();

                res.put("res", "success");
                return new JSONObject(res);
            } else {
                stmt1.close();
                conn.close();

                res.put("res", "fail");
                return new JSONObject(res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}