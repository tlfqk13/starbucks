package com.example.starbucks.service;

import com.example.starbucks.entity.User;
import com.example.starbucks.entity.VerifyCode;
import com.example.starbucks.repository.UserRepository;
import com.example.starbucks.repository.VerifyCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final VerifyCodeRepository verifyCodeRepository;

    public User saveUser(User user) {
        validateDuplicateUser(user);
        return userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        User findUserId = userRepository.findByUserId(user.getUserId());
        User findUserEmail = userRepository.findByEmail(user.getEmail());
        User findUserNickName = userRepository.findByNickName(user.getNickName());

        if (findUserId != null && findUserEmail != null && findUserNickName != null) {
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }

    public VerifyCode saveVerifyCode(VerifyCode verifyCode) {
        return verifyCodeRepository.save(verifyCode);
    }

    public Boolean validateVerifyCode(String token) {

        VerifyCode findVerifyCode = verifyCodeRepository.findByVerifyCode(token);
        VerifyCode findUserEmail = verifyCodeRepository.findByUserEmail(token);

        if(findVerifyCode != null || findUserEmail !=null){
            return true;
        }else{
            return false;
        }

    }

   /* @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);

        if(user == null){
            throw new UsernameNotFoundException(userId);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }*/
}
