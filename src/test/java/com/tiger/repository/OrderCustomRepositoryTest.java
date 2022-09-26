package com.tiger.repository;

import com.tiger.domain.order.dto.IncomeVehicleResponseDto;
import com.tiger.utils.CheckUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
//@RunWith(SpringRunner.class)  //Junit 테스트 선언
//@DataJpaTest // DataJpaTest 선언
@SpringBootTest
class OrderCustomRepositoryTest {

    @Autowired
    private OrderCustomRepository orderCustomRepository;
    @Autowired
    private CheckUtil checkUtil;
    @Autowired
    private OpenDateRepository openDateRepository;

    @Test
    @DisplayName("일일매출 바그래프")
    void getIncomeListBar(){
        //given
        Long ownerId = 5l;
        //when
        List<IncomeVehicleResponseDto> incomeListDayBar = orderCustomRepository.getIncomeListDayBar(ownerId, LocalDate.now());
        //then
        assertThat(incomeListDayBar.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("월매출 바그래프")
    void getIncomeListMonthBar(){
        //given
        Long ownerId = 5l;
        //when
        List<IncomeVehicleResponseDto> incomeListMonthBar = orderCustomRepository.getIncomeListMonthBar(ownerId, LocalDate.now());
        //then
        assertThat(incomeListMonthBar.size()).isEqualTo(5);
    }

}