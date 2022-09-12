package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
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
    @PostMapping("/{vehicleId}")
    public CommonResponseDto<?> order(HttpServletRequest request,
                                      @PathVariable Long vehicleId,
                                      @RequestBody OrderRequestDto orderRequestDto){
        return orderService.order(request, vehicleId, orderRequestDto);
    }

    // 주문 리스트 가져오기(렌터)
    @GetMapping("/renter")
    public CommonResponseDto<?> getOrderListRenter(HttpServletRequest request,
                                                   @RequestParam String status,
                                                   @RequestParam int limit,
                                                   @RequestParam int offset) {
        return orderService.getOrderListRenter(request, status, limit, offset);
    }

    // 주문 취소 (렌터)
    @DeleteMapping("/{orderId}")
    public CommonResponseDto<?> refund(HttpServletRequest request, @PathVariable Long orderId){
        return orderService.refund(request, orderId);
    }



    /*******************오더 *******************/
    // 오너 판매리스트(예약, 진행, 완료, 취소)
    @GetMapping("/owner")
    public CommonResponseDto<?> getOrderListOwner(HttpServletRequest request,
                                                  @RequestParam String status,
                                                  @RequestParam int limit,
                                                  @RequestParam int offset) {
        return orderService.getOrderListOwner(request, status, limit, offset);
    }

    // 수익현황(일일 매출)
    @GetMapping("/owner/payout/day")
    public CommonResponseDto<?> getIncomeListDaY(HttpServletRequest request) {
        return orderService.getIncomeListDay(request);
    }

    // 수익현황(월 매출)
    @GetMapping("/owner/payout/month")
    public CommonResponseDto<?> getIncomeListMonth(HttpServletRequest request) {
        return orderService.getIncomeListMonth(request);
    }

}
