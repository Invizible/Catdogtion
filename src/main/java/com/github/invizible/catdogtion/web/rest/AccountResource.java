package com.github.invizible.catdogtion.web.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.repository.UserRepository;
import com.github.invizible.catdogtion.service.AccountService;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/account")
public class AccountResource {
  private static ObjectMapper MAPPER = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired
  private RecaptchaValidator recaptchaValidator;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Validator validator;

  @Autowired
  private RepositoryEntityLinks entityLinks;

  @Autowired
  private AccountService accountService;

  @PostMapping("/registration")
  public ResponseEntity<?> signUp(@RequestBody String json) throws URISyntaxException, IOException {
    ObjectNode node = MAPPER.readValue(json, ObjectNode.class);
    User user = MAPPER.treeToValue(node, User.class);

    Set<ConstraintViolation<User>> errors = validator.validate(user);
    if (!errors.isEmpty()) {
      ResponseEntity.badRequest().body("Validation failed!");
    }

    ValidationResult result = recaptchaValidator.validate(node.get("captcha").textValue());
    if (result.isFailure()) {
      return ResponseEntity.badRequest().body("Bad captcha!");
    }

    Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser.isPresent()) {
      return ResponseEntity.badRequest().body("Username is taken!");
    }

    User savedUser = accountService.createUser(user, node.get("password").textValue());

    return ResponseEntity.created(entityLinks.linkForSingleResource(User.class, savedUser.getId()).toUri())
      .body(savedUser);
  }

}