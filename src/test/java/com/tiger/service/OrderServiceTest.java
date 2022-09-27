package com.tiger.service;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.PayMethod;
import com.tiger.exception.CustomException;
import com.tiger.repository.OpenDateRepository;
import com.tiger.repository.OrderRepository;
import com.tiger.utils.CheckUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

import static com.tiger.exception.StatusCode.*;
import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
//@RunWith(SpringRunner.class)  //Junit 테스트 선언
//@DataJpaTest // DataJpaTest 선언
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CheckUtil checkUtil;
    @Autowired
    private OpenDateRepository openDateRepository;

    @Test
    @DisplayName("주문하기")
    void createOrder(){

        //given
        HttpServletRequest request = null;
        Long vehicleId = 2l;

        String imp_uid = "0000"; // 가맹점에서 생성/관리하는 고유 주문번호
        String pay_method = PayMethod.CARD.toString(); // 결제 수단
        int paid_amount = 4000; // 결제 금액
        LocalDate start_date = LocalDate.parse("2022-08-26"); // 시작 날짜
        LocalDate end_date = LocalDate.parse("2022-08-27"); // 종료 날짜
        OrderRequestDto orderRequestDto = new OrderRequestDto(
                imp_uid, pay_method, paid_amount, start_date, end_date);
        //when
        CommonResponseDto<?> result = orderService.order(request, vehicleId, orderRequestDto);
        // 예약 당일 이전 날짜들 예약 막아놓기
        LocalDate now = LocalDate.now();
        if(now.compareTo(orderRequestDto.getStartDate()) < 0){
            System.out.println("now.compareTo(orderRequestDto.getStartDate() = " + now.compareTo(orderRequestDto.getStartDate()));
            throw new CustomException(EXPIRED_DATE_FORBIDDEN);
        }

        //then

        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
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

    @Test
    @DisplayName("판매리스트[오너] 성공")
    void getOrderListOwner(){

        // given
        Long memberId = 1l;
        // when
        Status status = null;
        try {
            status = Status.valueOf("RESERVED");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        List<Orders> orderList = orderRepository.getOrderList(memberId, "RESERVED", 3, 0).orElse(null);
        //then
        assertThat(orderList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("예약 당일이 된 상품들 STATUS 변경하기")
    void findAllByStartDateEqualsNow(){

        List<Orders> list = orderRepository.findAllByStartDateEquals(LocalDate.now()).get();

        for(int i=0; i<list.size(); i++){
            Orders order = list.get(i);
            order.setStatus(Status.USE);
        }
        for(int j=0; j<list.size(); j++){
            Orders order = list.get(j);
            assertThat(order.getStatus()).isEqualTo(Status.USE);
        }
    }




}