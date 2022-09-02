package com.tiger.service;

import com.tiger.domain.order.Orders;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.PayMethod;
import com.tiger.repository.MemberRepository;
import com.tiger.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
//@RunWith(SpringRunner.class)  //Junit 테스트 선언
//@DataJpaTest // DataJpaTest 선언
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 목록 생성하기")
    void createOrder(){


        //given
        HttpServletRequest request = null;
        Long vehicleId = 2l;

        String imp_uid = "0002"; // 가맹점에서 생성/관리하는 고유 주문번호
        String pay_method = PayMethod.CARD.toString(); // 결제 수단
        int paid_amount = 10000; // 결제 금액
        LocalDate start_date = LocalDate.parse("2022-09-04"); // 시작 날짜
        LocalDate end_date = LocalDate.parse("2022-09-11"); // 종료 날짜
        OrderRequestDto orderRequestDto = new OrderRequestDto(
                imp_uid, pay_method, paid_amount, start_date, end_date);
        //when
        ResponseEntity<?>result = orderService.order(request, vehicleId, orderRequestDto);
        //then
        assertThat(result.getStatusCode().toString()).isEqualTo("200");
    }

    @Test
    @DisplayName("등록된 상품리스트[렌터}")
    void getOrderList(){

        //givn
        HttpServletRequest request = null;
        Long renterId = 1l;

        //when
        List<Orders> ordersList = orderRepository.findAllByMemberId(renterId).orElse(null);
        //then
        assertThat(ordersList.size()).isEqualTo(1);
    }




}