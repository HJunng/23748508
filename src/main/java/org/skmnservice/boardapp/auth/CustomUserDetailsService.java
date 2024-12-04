package org.skmnservice.boardapp.auth;

import lombok.RequiredArgsConstructor;
import org.skmnservice.boardapp.user.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword()) // DB에 저장된 암호화된 비밀번호 사용
                        .roles(user.getRole()) // ROLE_USER
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
