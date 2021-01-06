package com.sakshisehgal.ssoserver;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(getUser().getId(), user.getUser().getId());
    }

public Collection<? extends GrantedAuthority> getAuthorities() {
	// TODO Auto-generated method stub
	return null;
}

public String getPassword() {
	// TODO Auto-generated method stub
	return user.getPasswordHash();
}

public String getUsername() {
	// TODO Auto-generated method stub
	return user.getUserName();
}



public boolean isEnabled() {
	// TODO Auto-generated method stub
	return true;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

@Override
public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean isAccountNonLocked() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return false;
}
}