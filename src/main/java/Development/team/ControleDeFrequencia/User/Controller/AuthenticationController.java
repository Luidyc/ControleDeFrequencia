package Development.team.ControleDeFrequencia.User.Controller;

import Development.team.ControleDeFrequencia.User.Entity.AuthenticationDto;
import Development.team.ControleDeFrequencia.User.Entity.LoginResponseDto;
import Development.team.ControleDeFrequencia.User.Entity.RegisterDto;
import Development.team.ControleDeFrequencia.User.Entity.UserEntity;
import Development.team.ControleDeFrequencia.User.Infra.Security.TokenService;
import Development.team.ControleDeFrequencia.User.Repository.UserRepository;
import Development.team.ControleDeFrequencia.User.Services.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto request) {
        //Verificando se tem usuário com esse login
        Optional<UserEntity> user = this.userRepository.findByLogin(request.login());
        // Se existir irei criar o user com a senha criptografada e tentar autenticar
        if (user.isPresent()) {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = this.tokenService.generateToken((UserEntity) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(user.get().getUsername(),token));
        }
        return ResponseEntity.badRequest().build();
    }

        @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@RequestBody @Valid RegisterDto request) {
        if(this.userRepository.findByLogin(request.login()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        UserEntity user = new UserEntity(request.login(),encryptedPassword,request.role());
        this.userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
