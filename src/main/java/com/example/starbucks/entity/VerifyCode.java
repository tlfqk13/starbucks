package com.example.starbucks.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name="verifyCode")
@Getter
@Setter
@ToString
public class VerifyCode extends BaseEntity {

    @Id
    @Column(name="verifyCode_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String verifyCode;

    /*@OneToOne
    @JoinColumn(name="user_id")
    private User user;*/

    @Column(name="user_email")
    private String userEmail;

}
