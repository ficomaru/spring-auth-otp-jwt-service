package com.ninehub.authentication.service;

import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.entity.Validation;
import com.ninehub.authentication.repository.ValidationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ValidationService {
    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void saveValidation(User user){
        Validation validation = new Validation();
        // Set user for validation
        validation.setUser(user);

        Instant createdAt = Instant.now();
        validation.setCreatedAt(createdAt);

        Instant expiredAt = createdAt.plus(10, ChronoUnit.MINUTES);
        validation.setExpiredAt(expiredAt);

        // Now we create the code
        Random random = new Random();
        int ramdomInteger = random.nextInt(999999);
        String code = String.format("%06d", ramdomInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.sendNotification(validation);
    }

    public Validation readWithTheCode(String code){
        Validation validation = validationRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid code"));

        if (validation.getExpiredAt().isBefore(Instant.now())) {
            throw new RuntimeException("Code expired");
        }

        return validation;
    }

    @Scheduled(cron = "* */15 * * * *")
    public void removeUselessCode(){
        log.info("Useless code deleted at {}", Instant.now());
        this.validationRepository.deleteAllByExpiredAtBefore(Instant.now());
    }

}
