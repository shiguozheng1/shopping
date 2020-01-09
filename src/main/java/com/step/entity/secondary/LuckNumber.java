package com.step.entity.secondary;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * shigz
 * 2019/12/20
 **/
@Data
@Table(name = "t_number")
@Entity
public class LuckNumber implements Serializable {
    private static final long serialVersionUID = 1502397241540373666L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer key;

    /**
     * 是否启用
     */
    @NotNull
    @Column(nullable = false)
    private Boolean isEnabled;

}
