package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.configuration.vnpay.Config;
import com.example.BuildPC.dto.TransactionStatusDTO;
import com.example.BuildPC.model.*;
import com.example.BuildPC.repository.OrderDetailRepository;
import com.example.BuildPC.service.OrderDetailService;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import com.example.BuildPC.service.OrderService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @PostMapping("/create-payment")
    public RedirectView createPayment(HttpServletRequest req,
                                      @RequestParam("firstname") String firstName,
                                      @RequestParam("lastname") String lastName,
                                      @RequestParam("streetaddress") String streetAddress,
                                      @RequestParam("apartmentaddress") String apartmentAddress,
                                      @RequestParam("town") String town,
                                      @RequestParam("country") String country,
                                      @RequestParam("postcode") String postcode,
                                      @RequestParam("email") String email,
                                      @RequestParam("telephone") String telephone,
                                      @RequestParam("note") String note,
                                      @RequestParam("amount") BigDecimal amount,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) throws UnsupportedEncodingException {

        BigDecimal amountTimes100 = amount.multiply(new BigDecimal("100"));
        // Order creation logic
        Optional<User> currentUser = userService.findByEmail(userDetails.getEmail());
        if (!currentUser.isPresent()) {
            return new RedirectView("/login");
        }

        User user = currentUser.get();
        List<CartItem> itemsList = shoppingCartService.listCartItems(user);

        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String address = streetAddress + ", " + apartmentAddress + ", " + town + ", " + postcode + ", " + country;

        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        Order order = new Order(Integer.parseInt(vnp_TxnRef),date, note, address, user);
        orderService.saveOrder(order);

        for(CartItem item : itemsList){
            OrderDetail od = new OrderDetail(item.getQuantity(), (float) 0,order,item.getProduct());
            orderDetailRepository.save(od);
        }

        // Payment processing logic
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = req.getParameter("bankCode");





        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((amountTimes100.intValue())));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(paymentUrl);

        return redirectView;
    }

    @GetMapping("/payment_infor")
    public RedirectView transaction(@RequestParam(value = "vnp_Amount") String amount,
                                    @RequestParam(value = "vnp_BankCode") String bankcode,
                                    @RequestParam(value = "vnp_OrderInfo") String orderInfo,
                                    @RequestParam(value = "vnp_ResponseCode") String responseCode,
                                    @RequestParam(value = "vnp_TxnRef") String txnRef,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        RedirectView redirectView = new RedirectView();

        Optional<User> currentUser = userService.findByEmail(userDetails.getEmail());
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            LocalDate currentDate = LocalDate.now();
            Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Order order = orderService.getOrderById(Integer.parseInt(txnRef));

            if ("00".equals(responseCode)) {
                order.setStatus(Status.IN_PROGRESS);
                shoppingCartService.removeAll(user);
                redirectView.setUrl("/payment/PaymentSuccess");// Payment successful
            } else {
                order.setStatus(Status.CANCEL); // Payment failed
                redirectView.setUrl("/payment/PaymentFail");
            }

            orderService.saveOrder(order);

        } else {
            redirectView.setUrl("/login");
        }

        return redirectView;
    }
}