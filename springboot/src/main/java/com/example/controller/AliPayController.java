package com.example.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.common.config.AliPayConfig;
import com.example.common.enums.OrderStatusEnum;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Alipay payment integration controller
 * Handles payment requests and asynchronous payment notifications
 */

@RestController
@RequestMapping("/alipay")
public class AliPayController {
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    // Alipay sandbox gateway URL
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    // Signature algorithm
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private OrdersService ordersService;

    /**
     * Initiate Alipay payment
     * @param orderNo Order number
     * @param httpResponse HTTP response to redirect to Alipay payment page
     */
    @GetMapping("/pay")
    public void pay(String orderNo, HttpServletResponse httpResponse) throws Exception {
        // Query order information
        Orders orders = ordersService.selectByOrderNo(orderNo);
        if (orders == null) {
            return;
        }
        // 1. Create Alipay client
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. Create payment request and set parameters
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("total_amount", orders.getTotal());
        bizContent.set("subject", orders.getGoodsName()); // Payment subject
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // Product code (fixed)
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:8080/front/orders"); // Redirect URL after payment completion
        // Execute request and return payment form
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
            log.info("Payment initiated for order: {}", orderNo);
        } catch (AlipayApiException e) {
            log.error("Alipay payment request failed for order: {}", orderNo, e);
            throw e;
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * Handle Alipay asynchronous payment notification
     * @param request HTTP request containing payment result parameters
     */
    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            log.info("========= Alipay async notification received =========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8");
            // Verify Alipay signature
            if (checkSignature){
                log.info("Payment verification successful:");
                log.info("Transaction subject: {}", params.get("subject"));
                log.info("Transaction status: {}", params.get("trade_status"));
                log.info("Alipay trade number: {}", params.get("trade_no"));
                log.info("Merchant order number: {}", params.get("out_trade_no"));
                log.info("Transaction amount: {}", params.get("total_amount"));
                log.info("Buyer Alipay ID: {}", params.get("buyer_id"));
                log.info("Payment time: {}", params.get("gmt_payment"));
                log.info("Buyer payment amount: {}", params.get("buyer_pay_amount"));


                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");
                // Update order status to "awaiting shipment" and set payment information
                Orders orders = ordersService.selectByOrderNo(tradeNo);
                orders.setStatus(OrderStatusEnum.NOTSEND.value);
                orders.setPayTime(gmtPayment);
                orders.setPayNo(alipayTradeNo);
                ordersService.updateById(orders);
                log.info("Order {} updated successfully after payment", tradeNo);
            } else {
                log.error("Alipay signature verification failed");
            }
        }
    }

}