package com.tiger.notification;

import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Notification extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member receiver;

    @Builder
    public Notification(Member receiver, Boolean isRead) {
        this.receiver = receiver;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }


}
