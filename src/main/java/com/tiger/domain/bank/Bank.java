package com.tiger.domain.bank;

import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long money; // 잔고

    //입금
    public void deposit(Long price){
        this.money += price;
    }

    //출금
    //@Transactional
    public void withdraw(Long price){
        if(this.money - price < 0){
            throw new CustomException(StatusCode.EXCESS_AMOUNT_BANK);
        }
        this.money -= price;
    }

}
