package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.AuthRequestDTO;
import com.credito.ConsorCheck.dto.AuthResponseDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.UsuarioMapper;
import com.credito.ConsorCheck.model.RefreshToken;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.RefreshTokenRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.JwtService;

import com.credito.ConsorCheck.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // verificar!!!

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenRepository refreshTokenRepository, UsuarioMapper usuarioMapper){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO dto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getSenha())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String accessToken = jwtService.gerarToken(userDetails);

        Usuario user = usuarioRepository.findByEmail(dto.getUsername())
                .orElseThrow(() -> new SQLException("Usuário inválidos"));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataVencimento(LocalDateTime.now().plusSeconds(refreshExpiration /1000));
        refreshTokenRepository.save(refreshToken);

        UsuarioResponseDTO respose = usuarioMapper.toDTO(user);
        return new AuthResponseDTO(respose, accessToken, refreshToken.getToken(), "Bearer");

        /*
            O próprio java spring faz essa validação abaixo com o authenticate e o @Bean do passwordEncoder
            if(!passwordEncoder.matches(dto.getSenha(), user.getSenha()))
                throw new InvalidDataException("Senha incorreta", List.of());
        */
    }

    @Transactional
    public void logout(String refreshTokenValue){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new SQLException("Token não encontrado"));
        /*refreshToken.setValido(false);
        refreshTokenRepository.save(refreshToken);*/
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public AuthResponseDTO refreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SQLException("Token não encontrado"));

        if(!refreshToken.isValido())
            throw new BusinessException("Refresh token revogado");

        if(refreshToken.getDataVencimento().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessException("Refresh token expirado, faça login novamente");
        }

        Usuario user = refreshToken.getUsuario();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String novoAccessToken = jwtService.gerarToken(userDetails);

        refreshTokenRepository.delete(refreshToken);

        RefreshToken novoRefreshoToken = new RefreshToken();
        novoRefreshoToken.setUsuario(user);
        novoRefreshoToken.setToken(UUID.randomUUID().toString());
        novoRefreshoToken.setDataVencimento(LocalDateTime.now().plusSeconds(refreshExpiration/1000));
        refreshTokenRepository.save(novoRefreshoToken);

        return new AuthResponseDTO(usuarioMapper.toDTO(user),novoAccessToken, novoRefreshoToken.getToken(), "Bearer");
    }
}
