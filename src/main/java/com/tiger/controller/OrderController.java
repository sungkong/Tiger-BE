package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문하기
    @PostMapping("/{vehicleId}")
    public CommonResponseDto<?> order(@PathVariable Long vehicleId,
                                      @RequestBody OrderRequestDto orderRequestDto){
        return orderService.order(vehicleId, orderRequestDto);
    }

    // 주문 리스트 가져오기(렌터)
    @GetMapping("/renter")
    public CommonResponseDto<?> getOrderListRenter(@RequestParam String status,
                                                   @RequestParam int limit,
                                                   @RequestParam int offset) {
        return orderService.getOrderListRenter(status, limit, offset);
    }

    // 주문 취소 (렌터)
    @DeleteMapping("/{orderId}")
    public CommonResponseDto<?> refund( @PathVariable Long orderId){
        return orderService.refund(orderId);
    }

    /*******************오더 *******************/
    // 반납하기
    @PostMapping("/owner/return/{orderId}")
    public CommonResponseDto<?> returnVehicle(@PathVariable Long orderId){
        return orderService.returnVehicle(orderId);
    }

    // 오너 판매리스트(예약, 진행, 완료, 취소)
    @GetMapping("/owner")
    public CommonResponseDto<?> getOrderListOwner(@RequestParam String status,
                                                  @RequestParam int limit,
                                                  @RequestParam int offset) {
        return orderService.getOrderListOwner(status, limit, offset);
    }

    // 수익현황(일일 매출)
    @GetMapping("/owner/payout/day")
    public CommonResponseDto<?> getIncomeListDaY(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return orderService.getIncomeListDay(date);
    }

    // 수익현황(월 매출)
    @GetMapping("/owner/payout/month")
    public CommonResponseDto<?> getIncomeListMonth(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return orderService.getIncomeListMonth(date);
    }


}
