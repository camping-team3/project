package com.camping.erp.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void join(UserRequest.JoinDTO request) {
        // 직접 구현하세요.
    }

    public UserResponse.LoginDTO login(UserRequest.LoginDTO request) {
        // 직접 구현하세요.
        return null;
    }

    public UserResponse.DetailDTO findUser(Long id) {
        // 직접 구현하세요.
        return null;
    }
}
