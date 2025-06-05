package com.lms.api;

import com.lms.config.ConfigVNPay;
import com.lms.dto.PaymentResDTO;
import com.lms.dto.PaymentStatusResponse;
import com.lms.util.ApiPaths;
import com.lms.util.VNPayUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(ApiPaths.PaymentCtrl.CTRL)
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PaymentController {


    private static final Map<String, String> RESPONSE_CODE_MESSAGES = Map.ofEntries(
            Map.entry("00", "Giao dịch thành công"),
            Map.entry("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ"),
            Map.entry("09", "Thẻ/Tài khoản chưa đăng ký InternetBanking"),
            Map.entry("10", "Xác thực thông tin sai quá 3 lần"),
            Map.entry("11", "Hết thời gian thanh toán"),
            Map.entry("12", "Thẻ/Tài khoản bị khóa"),
            Map.entry("13", "Sai OTP"),
            Map.entry("24", "Khách hàng hủy giao dịch"),
            Map.entry("51", "Không đủ số dư"),
            Map.entry("65", "Vượt hạn mức giao dịch trong ngày"),
            Map.entry("75", "Ngân hàng bảo trì"),
            Map.entry("79", "Sai mật khẩu thanh toán quá số lần"),
            Map.entry("99", "Lỗi khác")
    );

    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam("amount") Long amount, @RequestParam("vnp_ReturnUrl") String vnp_ReturnUrl, @RequestParam("quantity") int quantity, HttpServletRequest req) throws UnsupportedEncodingException {



        String vnp_TxnRef = ConfigVNPay.getRandomNumber(8);
        String vnp_IpAddr = ConfigVNPay.getIpAddress(req);

        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", ConfigVNPay.vnp_Version);
        vnp_Params.put("vnp_Command", ConfigVNPay.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);


        String vnp_Version = ConfigVNPay.vnp_Version;
        String vnp_Command = ConfigVNPay.vnp_Command;
        String vnp_OrderInfo = req.getParameter("vnp_OrderInfo");
        String orderType = req.getParameter("ordertype");
//        String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "");;
//        String vnp_IpAddr = VNPayUtil.getIpAddress(req);
//        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;
//        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Quantity", String.valueOf(quantity));
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = req.getParameter("bankcode");
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", String.valueOf(quantity));
        if (vnp_OrderInfo == null || vnp_OrderInfo.isEmpty()) {
            vnp_OrderInfo = "Thanh toan don hang: " + vnp_TxnRef;
        }
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        if (orderType == null || orderType.isEmpty()) {
            orderType = ConfigVNPay.vnp_orderType;
        }
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        //Billing
        vnp_Params.put("vnp_Bill_Mobile", req.getParameter("txt_billing_mobile"));
        vnp_Params.put("vnp_Bill_Email", req.getParameter("txt_billing_email"));
        String fullName = (req.getParameter("txt_billing_fullname"));
        if (fullName != null && !fullName.isEmpty()) {
            fullName = fullName.trim();
            int idx = fullName.indexOf(' ');
            String firstName = fullName.substring(0, idx);
            String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
            vnp_Params.put("vnp_Bill_FirstName", firstName);
            vnp_Params.put("vnp_Bill_LastName", lastName);
        }

        vnp_Params.put("vnp_Bill_Address", req.getParameter("txt_inv_addr1"));
        vnp_Params.put("vnp_Bill_City", req.getParameter("txt_bill_city"));
        vnp_Params.put("vnp_Bill_Country", req.getParameter("txt_bill_country"));
        if (req.getParameter("txt_bill_state") != null && !req.getParameter("txt_bill_state").isEmpty()) {
            vnp_Params.put("vnp_Bill_State", req.getParameter("txt_bill_state"));
        }

        // Invoice
        vnp_Params.put("vnp_Inv_Phone", req.getParameter("txt_inv_mobile"));
        vnp_Params.put("vnp_Inv_Email", req.getParameter("txt_inv_email"));
        vnp_Params.put("vnp_Inv_Customer", req.getParameter("txt_inv_customer"));
        vnp_Params.put("vnp_Inv_Address", req.getParameter("txt_inv_addr1"));
        vnp_Params.put("vnp_Inv_Company", req.getParameter("txt_inv_company"));
        vnp_Params.put("vnp_Inv_Taxcode", req.getParameter("txt_inv_taxcode"));
        vnp_Params.put("vnp_Inv_Type", req.getParameter("cbo_inv_type"));
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVNPay.vnp_PayUrl + "?" + queryUrl;
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("Ok");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }

    @GetMapping("/verify-payment")
    public ResponseEntity<?> transaction(@RequestParam("vnp_ResponseCode")String receivedHash, HttpServletRequest req) throws Exception {
        Map<String, String> parameters = VNPayUtil.getParametersFromRequest(req);
//        parameters.remove("vnp_SecureHash");
//        String calculatedHash = VNPayUtil.hmacSHA512(ConfigVNPay.secretKey, VNPayUtil.buildHashData(parameters));
//
//        if (!receivedHash.equalsIgnoreCase(calculatedHash)) {
//            throw new Exception("exception.payment.invalid.or.tampered.data");
//        }
        String vnpResponseCode = parameters.get("vnp_ResponseCode");
        String message = vnpResponseCode != null
                ? RESPONSE_CODE_MESSAGES.getOrDefault(receivedHash, "Không xác định mã lỗi")
                : "Mã phản hồi không hợp lệ";
        return new ResponseEntity<>(new PaymentStatusResponse(
                message,
                "00".equals(vnpResponseCode)
        ), HttpStatus.OK);
    }

}
