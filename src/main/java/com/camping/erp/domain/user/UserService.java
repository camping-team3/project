package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
import com.camping.erp.domain.user.enums.UserStatus;
import com.camping.erp.global.handler.ex.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void join(UserRequest.JoinDTO request) {
        // 1. 비밀번호 확인 일치 검증 추가
        if (request.getPassword() == null || !request.getPassword().equals(request.getPasswordConfirm())) {
            throw new Exception400("비밀번호가 일치하지 않습니다.");
        }

        // 2. 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception400("이미 존재하는 아이디입니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. 저장
        userRepository.save(request.toEntity(encodedPassword));
    }

    public UserResponse.LoginDTO login(UserRequest.LoginDTO request) {
        // 1. 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new Exception400("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 비교 (평문 비교 OR BCrypt 비교)
        boolean isMatch = request.getPassword().equals(user.getPassword())
                || passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isMatch) {
            throw new Exception400("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. DTO 반환
        return UserResponse.LoginDTO.builder()
                .user(user)
                .build();
    }

    public UserResponse.DetailDTO findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("사용자를 찾을 수 없습니다."));
        return UserResponse.DetailDTO.builder()
                .user(user)
                .build();
    }

    // 회원 정보 수정
    @Transactional
    public User update(Long id, UserRequest.UpdateDTO request) {
        // 1. 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("사용자를 찾을 수 없습니다."));

        // 2. 기존 비밀번호 확인 (평문 비교 OR BCrypt 비교)
        boolean isMatch = request.getCurrentPassword().equals(user.getPassword())
                || passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());

        if (!isMatch) {
            throw new Exception400("현재 비밀번호가 일치하지 않습니다.");
        }

        // 3. 일반 정보 업데이트 (이름, 이메일, 전화번호)
        user.updateInfo(request.getName(), request.getEmail(), request.getPhone());

        // 4. 새 비밀번호 변경 처리 (입력된 경우에만)
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
                throw new Exception400("새 비밀번호가 일치하지 않습니다.");
            }
            String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
            user.updatePassword(encodedNewPassword);
        }

        return user;
    }

    // 전체 회원 목록 조회 (페이징)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 회원 권한 변경
    @Transactional
    public void updateRole(Long id, UserRole role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("사용자를 찾을 수 없습니다."));
        user.updateRole(role);
    }

    // 회원 상태 변경
    @Transactional
    public void updateStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("사용자를 찾을 수 없습니다."));
        user.updateStatus(status);
    }
}
