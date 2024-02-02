package com.wanted.preonboarding.ticket.domain.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * DB Table = performance_seat_info
 * 공연 좌석 정보 엔티티
 * private int id;  // 좌석정보 ID (PK)
 * private UUID performanceId;  // 공연전시ID
 * private int round;   // 회차(FK)
 * private int gate;    // 입장 게이트
 * private char line;   // 좌석 열
 * private int seat;    // 좌석 행
 * private String isReserve;    // 예약 여부
 * private LocalDateTime createdAt; // 생성 시각
 * private LocalDateTime updateAt;  // 수정 시각
 */

@Entity
@Table(
    name = "performance_seat_info",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "performance_seat_info_unique",
            columnNames = {
                "performance_id",
                "round",
                "line",
                "seat"
            }
        )
    }
)
@Getter
@Builder
@DynamicInsert  // insert시 지정하지 않은 값에 대하여 db default값이 적용되도록 하기 위함
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceSeatInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 좌석정보 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "performance_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "round", referencedColumnName = "round", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    })  //(when an association has multiple '@JoinColumn's, they must each specify their 'referencedColumnName')
    private Performance performance;

    @Column(nullable = false)
    private int gate;    // 입장 게이트
    @Column(nullable = false)
    private char line;   // 좌석 열
    @Column(nullable = false)
    private int seat;    // 좌석 행
    @Column(name = "is_reserve",nullable = false, columnDefinition = "varchar default 'enable'")
    private String isReserve;    // 예약 여부
    @Column(nullable = true, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime createdAt;    // 생성 시각

    @Column(nullable = true, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime updatedAt;     // 수정 시각

    public void reserved (String disable){
        this.isReserve = disable;
    }

}
/*
not-null property references a null or transient value
@DynamicInsert 지정했고 columnDefinition 속성 지정했음에도 저러면 nullable = true 줘버리기

@ForeignKey(ConstraintMode.NO_CONSTRAINT) -> DB에서 FK 안 주는 대신 어플리케이션 레벨에서 연관관게를 맺음
좌석은 가로로 A~F, 세로로 1~15
 */
