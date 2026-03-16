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
        // 1. 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. DTO 반환
        return UserResponse.LoginDTO.builder()
                .user(user)
                .build();
    }

    public UserResponse.DetailDTO findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserResponse.DetailDTO.builder()
                .user(user)
                .build();
    }
}
