package com.techelevator.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.dao.UserDao;
import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;
import com.techelevator.model.User;
import com.techelevator.model.UserAlreadyExistsException;
import com.techelevator.security.jwt.JWTFilter;
import com.techelevator.security.jwt.TokenProvider;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        User user = userDao.findByUsername(loginDto.getUsername());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new LoginResponse(jwt, user), httpHeaders, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDTO newUser) {
        try {
            User user = userDao.findByUsername(newUser.getUsername());
            log.debug("User: [{}] already exist", newUser.getUsername());
            throw new UserAlreadyExistsException();
        } catch (UsernameNotFoundException e) {
            // added first and last to userDao.create params
            log.debug("User: [{}] created", newUser.getUsername());
            userDao.create(newUser.getUsername(), newUser.getPassword(), newUser.getRole(), newUser.getFirstName(),
                    newUser.getLastName());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public List<String> getAllPlayerNames(Principal principal) {
        // List<User> users = userDao.findAll();
        // List<String> players = new ArrayList<>();
        //
        // for(User user : users)
        // {
        // if (!user.getUsername().equalsIgnoreCase(principal.getName()) &&
        // !user.getUsername().equalsIgnoreCase("admin"))
        // {
        // players.add(user.getUsername());
        // }
        // }
        //
        // return players;

        return userDao.findAll().stream().map(User::getUsername)
                .filter(name -> !name.equalsIgnoreCase(principal.getName())
                        && !name.equalsIgnoreCase("admin"))
                .collect(Collectors.toList());
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class LoginResponse {

        private String token;
        private User user;

        LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        @JsonProperty("token")
        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }

        @JsonProperty("user")
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
