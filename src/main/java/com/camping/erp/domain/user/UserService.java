package com.camping.erp.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void join(UserRequest.JoinDTO request) {
        // 1. 중복 확인
        Optional<User> userOP = userRepository.findByUsername(request.getUsername());
        if (userOP.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 저장
        userRepository.save(request.toEntity(encodedPassword));
    }

    public UserResponse.LoginDTO login(UserRequest.LoginDTO request) {
        // TODO: 다음 단계에서 구현
        return null;
    }

    public UserResponse.DetailDTO findUser(Long id) {
        // TODO: 다음 단계에서 구현
        return null;
    }
}
