package app.service;
import app.model.Job;
import app.web.dto.EditAccountRequest;
import lombok.extern.slf4j.Slf4j;
import app.model.User;
import app.model.UserRole;
import app.repository.UserRepository;
import app.security.UserData;
import app.web.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

//    @Transactional
//    @CacheEvict(value = "users", allEntries = true)
    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("User with [%s] username already exist.".formatted(registerRequest.getUsername()));
        }

        if (registerRequest.getRole().equals("USER")) {

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(UserRole.USER)
                    .active(true)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();

            user = userRepository.save(user);
            log.info("New user profile was registered in the system for user [%s].".formatted(registerRequest.getUsername()));
            return user;
        } else {

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(UserRole.RECRUITER)
                    .active(true)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();

            user = userRepository.save(user);
            log.info("New user profile was registered in the system for user [%s].".formatted(registerRequest.getUsername()));
            return user;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));

        return new UserData(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.getAvatar(), user.isActive());
    }

    public void editAccount(EditAccountRequest dto, UUID userId) {

        Optional<User> user = userRepository.findById(userId);
        User curUser = user.get();

        if (dto.getLocation() != null && dto.getLocation() != "" ) {
            curUser.setLocation(dto.getLocation());
        }
        if (dto.getEmail() != null && dto.getEmail() != "") {
            curUser.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null && dto.getPhone() != "") {
            curUser.setPhone(dto.getPhone());
        }

        if (dto.getUsername() != null && dto.getUsername() != "") {
            curUser.setUsername(dto.getUsername());
        }

        if (dto.getCurrentPassword() != null && dto.getCurrentPassword() != "") {

            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            curUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
//            curUser.setPassword(dto.getConfirmPassword());
        }

        userRepository.save(curUser);
    }

    public ModelAndView buildUserHomeView(UserData userData, List<Job> offers) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-home");
        modelAndView.addObject("user", userData);
        modelAndView.addObject("latestJobs", offers);
        Map<String, Object> defaultParams = new HashMap<>();
        defaultParams.put("keyword", null);
        defaultParams.put("location", null);
        defaultParams.put("jobType", null);
        modelAndView.addObject("searchParams", defaultParams);
        modelAndView.addObject("searchActive", false);
        modelAndView.addObject("searchResultCount", offers.size());
        return modelAndView;
    }
}
