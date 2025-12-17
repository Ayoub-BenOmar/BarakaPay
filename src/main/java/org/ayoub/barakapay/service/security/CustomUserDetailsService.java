package org.ayoub.barakapay.service.security;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.excepion.EmailNotFoundException;
import org.ayoub.barakapay.model.entity.User;
import org.ayoub.barakapay.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws EmailNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EmailNotFoundException("User not found: " + username));
        return new CustomUserDetails(user);
    }


}
