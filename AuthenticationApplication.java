package com.ninehub.authentication;

import com.ninehub.authentication.entity.Role;
import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.entity.enums.RoleType;
import com.ninehub.authentication.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class AuthenticationApplication  {
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

	// Creation des user lors du lancement du projet
	public void run(String... args) throws Exception {
//
//		// Création des rôles s'ils n'existent pas
//		for (RoleType type : RoleType.values()) {
//			roleRepository.findByRoleType(type)
//					.orElseGet(() -> roleRepository.save(Role.builder().roleType(type).build()));
//		}
//
//		// Récupération des rôles persistés
//		Role adminRole = roleRepository.findByRoleType(RoleType.ADMIN).get();
//		Role moderatorRole = roleRepository.findByRoleType(RoleType.MODERATOR).get();

		User admin = User.builder()
				.isActif(true)
				.firstName("admin")
				.password(passwordEncoder.encode("admin"))
				.email("admin@gmail.com")
				.role(Role.builder()
						.roleType(RoleType.ADMIN)
						.build())
				.build();
		this.userRepository.findByEmail(admin.getEmail())
				.orElse(this.userRepository.save(admin));


		User moderator = User.builder()
				.isActif(true)
				.firstName("moderator")
				.password(passwordEncoder.encode("moderator"))
				.email("moderator@gmail.com")
				.role(Role.builder().roleType(RoleType.MODERATOR).build())
				.build();
		this.userRepository.findByEmail("moderator@gmail.com")
				.orElse(this.userRepository.save(moderator));
	}
}


