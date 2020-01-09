package com.step.entity.secondary;

import lombok.Data;

import javax.persistence.*;

/**
 * shigz
 * 2019/9/10
 **/
@Entity
@Table(name="t_maycur_token")
@Data
public class MaycurToken {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name="maycur_token")
    private String tokenId;

    @Column(name="refresh_time")
    private Long refreshTime;

    @Column(name = "maycur_code",length = 1000)
    private String entCode;
}
