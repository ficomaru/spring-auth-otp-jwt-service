package com.ninehub.authentication.service;

import com.ninehub.authentication.entity.*;
import com.ninehub.authentication.entity.enums.RoleType;
import com.ninehub.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$"
    );

    public void register(User user){
        if (!EMAIL_REGEX.matcher(user.getEmail()).matches()) {
            throw new RuntimeException("Invalid email format");
        }

        // Some steps to save a non-redundant user
        Optional<User> optionalUser = this.userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new RuntimeException("This email already exist");
        }

        String cryptedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(cryptedPassword);

        // Create a role to affect it to the user
        Role userRole = new Role();
        userRole.setRoleType(RoleType.USER);

        // Then we affect the role to the user
        user.setRole(userRole);

        user = this.userRepository.save(user);
        this.validationService.saveValidation(user);
    }

    public void activateAccount(Map<String, String> activation) {
        Validation validation = this.validationService.readWithTheCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiredAt())){
            throw new RuntimeException("Your code has expired");
        }
        User userActivated = this.userRepository.findById(validation.getUser().getId())
                .orElseThrow(()-> new RuntimeException("Unknown user"));

        userActivated.setActif(true);
        this.userRepository.save(userActivated);
    }

    @Override // Finf the user in the db by username( this method commes from UserDetailsService)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Any user match with this username"));
    }

    public void modifyPassword(Map<String, String> parameters) {
        User user = this.loadUserByUsername(parameters.get("email"));
        this.validationService.saveValidation(user);
    }

    public void newPassword(Map<String, String> parameters) {
        try {
            User user = this.loadUserByUsername(parameters.get("email"));
            Validation validation = this.validationService.readWithTheCode(parameters.get("code"));
            if (validation.getUser().getEmail().equals(user.getEmail())){
                String cryptedPassword = this.passwordEncoder.encode(parameters.get("password"));
                user.setPassword(cryptedPassword);
                this.userRepository.save(user);
            }
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public List<User> getAllUser() {
       final Iterable<User> userIterable =  userRepository.findAll();
       List<User> userList = new ArrayList<>();
       for (User user : userIterable){
           userList.add(user);
       }
       return userList;
    }
}
