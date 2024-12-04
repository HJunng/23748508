package org.skmnservice.boardapp.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.user.User;
import org.skmnservice.boardapp.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        // 데이터베이스에서 사용자 정보 조회
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // 입력된 비밀번호와 데이터베이스의 암호화된 비밀번호 비교
        if (passwordEncoder.matches(password, user.getPassword())) {
            return true; // 인증 성공
        }
        return false; // 인증 실패
    }

    // 회원가입 처리
    @Transactional
    public void registerUser(
            String username,
            String password,
            String name
    ) {
        // 사용자 중복 체크
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // 사용자 생성 및 저장
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
        userRepository.save(user);
    }
}
