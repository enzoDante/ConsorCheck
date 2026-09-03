package com.credito.ConsorCheck.security;

import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private final Usuario usuario;
    public UserDetailsImpl(Usuario usuario){
        this.usuario = usuario;
    }

    public Long getId(){ return usuario.getId(); }

    public Role getRole() { return usuario.getRole(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }
    @Override
    public String getPassword(){ return usuario.getSenha(); }
    @Override
    public String getUsername(){ return usuario.getEmail(); }

    @Override
    public boolean isEnabled(){ return usuario.isAtivo(); }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
}
