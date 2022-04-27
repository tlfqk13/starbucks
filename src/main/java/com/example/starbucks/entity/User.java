package com.example.starbucks.entity;

import com.example.starbucks.constant.Role;
import com.example.starbucks.dto.UserFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="user")
@Getter
@Setter
@ToString
public class User extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    private String userName;

    private String birthDate;

    private String address;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Column(unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static User createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUserId(userFormDto.getUserId());
        user.setPassword(user.getPassword());
        user.setUserName(user.getUserName());
        user.setNickName(user.getNickName());
        user.setBirthDate(user.getBirthDate());
        user.setAddress(user.getAddress());
        user.setEmail(user.getEmail());
        user.setPhoneNumber(user.getPhoneNumber());

        return user;
    }


}
