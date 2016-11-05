package com.github.invizible.catdogtion.web.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.repository.RoleRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import static com.github.invizible.catdogtion.Constants.USER_ROLE;

@RestController
@RequestMapping(path = "/api/users")
public class UserResource {
  private ObjectMapper mapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired
  private RecaptchaValidator recaptchaValidator;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Validator validator;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @PostMapping
  public ResponseEntity<?> signUp(@RequestBody String json) throws URISyntaxException, IOException {
    ObjectNode node = mapper.readValue(json, ObjectNode.class);
    User user = mapper.treeToValue(node, User.class);
    Set<ConstraintViolation<User>> errors = validator.validate(user);

    if (!errors.isEmpty()) {
      ResponseEntity.badRequest().body("Validation failed!");
    }

    ValidationResult result = recaptchaValidator.validate(node.get("captcha").textValue());

    if (result.isSuccess()) {
      Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

      if (existingUser.isPresent()) {
        return ResponseEntity.badRequest().body("Username is taken!");
      }

      user.setPassword(passwordEncoder.encode(node.get("password").textValue()));
      user.setRoles(Sets.newHashSet(roleRepository.findRoleByName(USER_ROLE).get()));

      User savedUser = userRepository.save(user);

      return ResponseEntity.created(new URI("/api/users/" + savedUser.getId()))
        .body(savedUser);
    } else {
      return ResponseEntity.badRequest().body("Bad captcha!");
    }
  }

}