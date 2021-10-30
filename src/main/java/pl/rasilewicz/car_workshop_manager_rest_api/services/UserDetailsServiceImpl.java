package pl.rasilewicz.car_workshop_manager_rest_api.services;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class UserDetailsServiceImpl implements UserDetails {
    private User user;

    public UserDetailsServiceImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
            return user.getEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
            return user.getEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
            return user.getEnabled();
    }

    @Override
    public boolean isEnabled() {
            return user.getEnabled();
    }

    public User getUserDetails() {
        return user;
    }


}
