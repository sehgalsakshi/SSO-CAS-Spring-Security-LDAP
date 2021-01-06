package com.sakshisehgal.ssoserver;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;


public class CustomUserDetailsContextMapper extends LdapUserDetailsMapper {

  @Override
  public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
      username = StringUtils.split(username, "\\")[0];
      //fetch details from db/ directory
      User user = new User();
      user.setId(1l);
      user.setUserName("Sakshi");
      user.setPasswordHash("");
        return new UserPrincipal(user);
  }
}
