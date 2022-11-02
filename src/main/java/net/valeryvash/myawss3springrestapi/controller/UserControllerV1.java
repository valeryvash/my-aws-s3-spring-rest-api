package net.valeryvash.myawss3springrestapi.controller;

import net.valeryvash.myawss3springrestapi.dto.*;
import net.valeryvash.myawss3springrestapi.model.User;
import net.valeryvash.myawss3springrestapi.dto.UserRolesDto;
import net.valeryvash.myawss3springrestapi.security.jwt.JwtTokenProvider;
import net.valeryvash.myawss3springrestapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/")
public class UserControllerV1 {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserControllerV1(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("auth/signup")
    public ResponseEntity<UserSignUpResponseDTO> signUpUser(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {

        String userName = userSignUpRequestDTO.getUserName();
        String email = userSignUpRequestDTO.getEmail();

        boolean userExist = userService.existsByUserNameIgnoreCase(userName);
        boolean emailExist = userService.existsByEmailIgnoreCase(email);

        if (userExist || emailExist) {
            String responseString = "";

            if (userExist) {
                responseString += String.format("Username: %s already exist ", userName);
            }
            if (emailExist) {
                responseString += String.format("Email: %s already exist ", email);
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    responseString);
        }

        User result = userSignUpRequestDTO.fromDTO();

        result = userService.signUp(result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserSignUpResponseDTO.fromUser(result));
    }

    @PostMapping("auth/signin")
    public ResponseEntity<UserSignInResponseDTO> signInUser(@RequestBody UserSignInRequestDTO userSignInRequestDTO) {
        String userName = userSignInRequestDTO.getUserName();
        String password = userSignInRequestDTO.getPassword();

        if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User credentials shall not be empty"
            );
        }

        User user = userService.findByUserName(userName);

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with username '%s' not found", userName)
            );
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid username or password",
                    e
            );
        }

        String token = jwtTokenProvider.createToken(user.getUserName(), user.getRoles());

        UserSignInResponseDTO responseDTO = new UserSignInResponseDTO(
                user.getUserName(),
                token
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", "Bearer_" + responseDTO.getToken())
                .body(responseDTO);
    }

    @PutMapping("users")
    public ResponseEntity<UserSignUpResponseDTO> changeUserRegisteredData(@RequestBody UserSignUpRequestDTO requestDTO) {
        String loggedUserName = getLoggedUser_UserName();

        String userName = requestDTO.getUserName();

        if (userName == null || userName.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User name shall not be empty"
            );
        }

        User userFromPersistenceContext = userService.findByUserName(loggedUserName);

        if (userFromPersistenceContext == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User with such name not found"
            );
        }

        User fromRequestDto = requestDTO.fromDTO();

        userFromPersistenceContext.setUserName(fromRequestDto.getUserName());
        userFromPersistenceContext.setPassword(requestDTO.getPassword());

        userFromPersistenceContext.setFirstName(fromRequestDto.getFirstName());
        userFromPersistenceContext.setLastName(fromRequestDto.getLastName());
        userFromPersistenceContext.setEmail(fromRequestDto.getEmail());

        userFromPersistenceContext = userService.update(userFromPersistenceContext);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserSignUpResponseDTO.fromUser(userFromPersistenceContext));
    }

    @DeleteMapping("users")
    public ResponseEntity<UserSignUpResponseDTO> deleteLoggedUser() {
        String userName = getLoggedUser_UserName();

        User userBeforeDelete = userService.deleteByUserName(userName);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(UserSignUpResponseDTO.fromUser(userBeforeDelete));
    }


    private String getLoggedUser_UserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    @PutMapping("moder/")
    public ResponseEntity<UserSignUpResponseDTO> updateUserData(@RequestBody UserUpdateInfoRequestDTO requestDTO) {
        String userName = requestDTO.getUserName();

        if (userName == null || userName.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not null or empty data required"
            );
        }

        User persistedUser = userService.findByUserName(requestDTO.getOldUserName());

        if (persistedUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with user name %s not found", requestDTO.getOldUserName())
            );
        }

        persistedUser.setUserName(requestDTO.getUserName());
        persistedUser.setEmail(requestDTO.getEmail());
        persistedUser.setFirstName(requestDTO.getFirstName());
        persistedUser.setLastName(requestDTO.getLastName());
        persistedUser.setEmail(requestDTO.getEmail());

        persistedUser = userService.update(persistedUser);

        UserSignUpResponseDTO responseDTO = UserSignUpResponseDTO.fromUser(persistedUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }

    @PutMapping("admin/add-moder-role/{userName}")
    public ResponseEntity<UserRolesDto> addModeratorRoleByUserName(@PathVariable("userName") String userName) {
        User persistedUser = userService.addModeratorRoleByUserName(userName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserRolesDto.fromUser(persistedUser));
    }

    @GetMapping("admin/user-roles/{userName}")
    public ResponseEntity<UserRolesDto> getUserRolesByUserName(@PathVariable("userName") String userName) {
        User persistedUser = userService.findByUserName(userName);

        if (persistedUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with user name %s not found", userName)
            );
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserRolesDto.fromUser(persistedUser));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public void UserNotFoundException(UsernameNotFoundException e) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }


}
