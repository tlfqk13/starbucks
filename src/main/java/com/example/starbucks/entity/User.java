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
        String password = passwordEncoder.encode(userFormDto.getPassword());
        user.setPassword(password);
        user.setUserName(userFormDto.getUserName());
        user.setNickName(userFormDto.getNickName());
        user.setBirthDate(userFormDto.getBirthDate());
        user.setAddress(userFormDto.getAddress());
        user.setEmail(userFormDto.getEmail());
        user.setPhoneNumber(userFormDto.getPhoneNumber());

        return user;
    }


}
