package com.tiger.controller;

import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문하기
    @PostMapping("")
    public ResponseEntity<?> order(HttpServletRequest request,
                                   @PathVariable Long vehicleId,
                                   @RequestBody OrderRequestDto orderRequestDto){
        return orderService.order(request, vehicleId, orderRequestDto);
    }

    // 주문 리스트 가져오기(렌터)
    @GetMapping("/renter/{renterId}")
    public ResponseEntity<?> getOrderListRenter(HttpServletRequest request, @PathVariable Long renterId){
        return orderService.getOrderListRenter(request, renterId);
    }

    // 주문 취소 (렌터)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> refund(HttpServletRequest request, @PathVariable Long orderId){
        return orderService.refund(request, orderId);
    }
}
