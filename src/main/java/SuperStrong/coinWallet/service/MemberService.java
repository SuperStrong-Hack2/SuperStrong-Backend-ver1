package SuperStrong.coinWallet.service;


import SuperStrong.coinWallet.entity.History;
import SuperStrong.coinWallet.entity.Member;
import SuperStrong.coinWallet.entity.Wallet;
import SuperStrong.coinWallet.jwttoken.JwtUtil;
import SuperStrong.coinWallet.repository.MemberRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Service
public class MemberService {
    private final String url = "jdbc:mysql://localhost:3306/mydb";
    private final String username = "myuser";
    private final String password = "mypassword";


    @Autowired
    private MemberRepository memberRepository;
    //    private WalletRepository walletRepository;
    public Object loginUser(JSONObject jsonObject) {
//        JSONObject jsoninput = new JSONObject(data);
//        System.out.println(jsoninput);
//        String id = String.valueOf(jsoninput.get("id"));
//        String pw = String.valueOf(jsoninput.get("pw"));
        System.out.println("3333333" + jsonObject);
        String id = (String) jsonObject.get("id");
        String pw = (String) jsonObject.get("pw");

        System.out.println("id: " + id + " pw: " + pw);
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent() && member.get().getPw().equals(pw)) {
            System.out.println(member.get().getMemberId());
            System.out.println("login success");
            String token = JwtUtil.generateToken(member.get().getMemberId(), member.get().getPubAddress());
            HashMap<String, Object> res_map = new HashMap<String, Object>();
            res_map.put("token", token);
            return new JSONObject(res_map);
        } else {
            HashMap<String, Object> res_map = new HashMap<String, Object>();
            res_map.put("token", "login failed");
            System.out.println("login fail");
            return new JSONObject(res_map);
        }
    }

    public Object assetInfo(HashMap<String, Object> data) {
        String id = (String) data.get("id");

        Optional<Member> member = memberRepository.findById(id);
        HashMap<String, Object> ret_asset = new HashMap<String, Object>();
        if (member.isPresent()) {
            Wallet memberwallet = member.get().getWallet();
            ret_asset.put("validation", "valid input");
            ret_asset.put("eth", memberwallet.getEthAmount());
            ret_asset.put("btc", memberwallet.getBtcAmount());
            ret_asset.put("doge", memberwallet.getDogeAmount());
            return new JSONObject(ret_asset);
        }
        else {
            HashMap<String, Object> res_map = new HashMap<String, Object>();
            res_map.put("validation", "invalid input");
            res_map.put("content", "no wallet info");
            return new JSONObject(res_map);
        }
    }

    public Object historyInfo(HashMap<String, Object> data) {
        JSONObject jsonObject = new JSONObject(data);
        String id = (String) data.get("id");

        Optional<Member> optionalMember = memberRepository.findById(id);
        List<Object> ret_history = new ArrayList<>();
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            List<History> histories = member.getHistories();
            ret_history.addAll(histories);

            List<Map<String, Object>> historyList = new ArrayList<>();
            for (Object obj : ret_history) {
                History history = (History) obj;
                if (history.getMember().getMemberId().equals(id)) {
                    HashMap<String, Object> each_history = new HashMap<String, Object>();
                    each_history.put("history_id", history.getHistoryId());
                    each_history.put("member_id", history.getMember().getMemberId());
                    each_history.put("status", history.getStatus());
                    each_history.put("interaction_id", history.getInteractionId());
                    each_history.put("coin_name", history.getCoinName());
                    each_history.put("amount", history.getAmount());
                    each_history.put("quote", history.getQuote());
                    each_history.put("gas", history.getGas());
                    historyList.add(each_history);
                }
            }
            if (!historyList.isEmpty()) {
                Map<String, Object> res_map = new HashMap<String, Object>();
                res_map.put("history", historyList);
                return new JSONObject(res_map);
            }
        }
        Map<String, Object> res_map = new HashMap<String, Object>();
        res_map.put("validation", "invalid input");
        res_map.put("content", "no history");
        return new JSONObject(res_map);
    }

    public String create_code() {

        Random random = new Random();        //랜덤 함수 선언
        int createNum = 0;              //1자리 난수
        String ranNum = "";             //1자리 난수 형변환 변수
        int letter    = 6;            //난수 자릿수:6
        String resultNum = "";          //결과 난수

        for (int i=0; i<letter; i++) {

            createNum = random.nextInt(9);        //0부터 9까지 올 수 있는 1자리 난수 생성
            ranNum =  Integer.toString(createNum);  //1자리 난수를 String으로 형변환
            resultNum += ranNum;            //생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
        }

        System.out.println(resultNum);
//        int result = Integer.parseInt(resultNum);
        return resultNum;
    }

    public String create_key(int num) {
        Random rnd =new Random();
        StringBuffer buf =new StringBuffer();
        for(int i=0;i<num;i++){
            // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
            if(rnd.nextBoolean()){
                buf.append((char)((int)(rnd.nextInt(26))+97));
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }
        String result = buf.toString();
        System.out.println(result);
        return result;
    }


    public int duplicate_checker(String query){
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt1 = conn.createStatement();

            ResultSet resultSet1 = stmt1.executeQuery(query);
            while (resultSet1.next()) {
                int result = Integer.parseInt( resultSet1.getString("success"));
                if(result==1){
//                    System.out.println("fail");
                    resultSet1.close();
                    stmt1.close();
                    conn.close();
                    return 0;
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    public Object signup(HashMap<String, Object> data) {
        System.out.println(data);
        // TODO     0:코드발송, 1:id존재, 2:email존재, 3:phone_num존재, 4:ssn존재, 5:이전에 코드 발송
        JSONObject jsonObject = new JSONObject(data);
        HashMap<String, String> res = new HashMap<>();

        try {
            String query1 = String.format("SELECT EXISTS (SELECT id FROM member where id='%s') as success", jsonObject.get("id"));
            if(duplicate_checker(query1) == 0) {
                res.put("res", "1");
                return new JSONObject(res);
            }

            String query2 = String.format("SELECT EXISTS (SELECT id FROM member where email='%s') as success", jsonObject.get("email"));
            if(duplicate_checker(query2) == 0) {
                res.put("res", "2");
                return new JSONObject(res);
            }

            String query3 = String.format("SELECT EXISTS (SELECT id FROM member where phone_num='%s') as success", jsonObject.get("phone_num"));
            if(duplicate_checker(query3) == 0) {
                res.put("res", "3");
                return new JSONObject(res);
            }

            String query4 = String.format("SELECT EXISTS (SELECT id FROM member where ssn='%s') as success", jsonObject.get("ssn"));
            if(duplicate_checker(query4) == 0) {
                res.put("res", "4");
                return new JSONObject(res);
            }

            String query5 = String.format("SELECT EXISTS (SELECT id FROM member_tmp where id='%s') as success", jsonObject.get("id"));
            if(duplicate_checker(query5) == 0) {
                res.put("res", "5");
                return new JSONObject(res);
            }

            String code = create_code();
            mailSender((String) jsonObject.get("email"), code);


            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt1 = conn.createStatement();

            String query6 = String.format("INSERT INTO member_tmp (id, pw, email, phone_num, ssn, code) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", jsonObject.get("id"), jsonObject.get("pw"), jsonObject.get("email"), jsonObject.get("phone_num"), jsonObject.get("ssn"), code);
            stmt1.executeUpdate(query6);
            stmt1.close();
            conn.close();

            res.put("res", "0");
            return new JSONObject(res);

        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        return null;
    }


    public Object signup_auth(HashMap<String, Object> data) {
        // TODO 가입 완료 후 member_tmp 테이블에 해당 데이터 삭제 필요
        JSONObject jsonObject = new JSONObject(data);
        HashMap<String, String> res = new HashMap<>();

        try {
            String query1 = String.format("SELECT EXISTS (SELECT id FROM member_tmp where code='%s') as success", jsonObject.get("code"));
            if (duplicate_checker(query1) == 0) {

                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt1 = conn.createStatement();

                String query5 = String.format("INSERT INTO member(id, pw, email, phone_num, ssn) SELECT member_tmp.id, member_tmp.pw, member_tmp.email, member_tmp.phone_num, member_tmp.ssn FROM member_tmp WHERE member_tmp.code = '%s'", jsonObject.get("code"));
                stmt1.executeUpdate(query5);

                String private_key = create_key(16);
                String pub_address = create_key(32);

                String query6 = String.format("SELECT id FROM member_tmp WHERE code = '%s'", jsonObject.get("code"));
                ResultSet resultSet = stmt1.executeQuery(query6);

                while (resultSet.next()) {
                    String id = resultSet.getString("id");

                    String query7 = String.format("UPDATE member SET private_key = '%s', pub_address = '%s' WHERE id = '%s';", private_key, pub_address, id);
                    stmt1.executeUpdate(query7);

                    String query8 = String.format("INSERT INTO member_wallet(wallet_id, member_id, btc_amount, eth_amount, doge_amount) VALUES ('%s', '%s', 5, 5, 5)", pub_address, id);
                    stmt1.executeUpdate(query8);

                    stmt1.close();
                    conn.close();

                    res.put("id", id);
                    res.put("private_key", private_key);
                    res.put("pub_address", pub_address);
                    res.put("res", "1");

                    JSONObject response = new JSONObject(res);
                    System.out.println(response);

                    return response;
                }
            }
            res.put("id", "");
            res.put("private_key", "");
            res.put("pub_address", "");
            res.put("res", "0");
            return new JSONObject(res);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @Autowired
    JavaMailSender emailSender;

    public void mailSender(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("carbigstar12@gmail.com");
        message.setTo(email);
        message.setSubject("<<Wallet 인증 코드>>");
        message.setText(String.format("       코드:  %s", code));
        emailSender.send(message);
    }

}
