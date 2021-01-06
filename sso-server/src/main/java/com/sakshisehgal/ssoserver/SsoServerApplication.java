package com.sakshisehgal.ssoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@SpringBootApplication
@EnableResourceServer
public class SsoServerApplication {
	public static final String LDAP_PROVIDER_URL = "ldap.provider.url";

    public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
    }
    
    @Configuration
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requestMatchers()
                    .antMatchers("/login", "/oauth/authorize")
                    .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().permitAll();
        }

        @Override
	    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	    	authenticationManagerBuilder.authenticationProvider(ldapAuthenticationProvider());
	    }
        
        @Bean
	    public LdapAuthenticator ldapAuthenticator() throws Exception {
	        BindAuthenticator authenticator = new CustomAuthenticationProvider(ldapContextSource());
	        authenticator.setUserDnPatterns(new String[] {"CN={0}"});
	        return authenticator;
	    }
        
        @Bean
	    public UserDetailsContextMapper userDetailsContextMapper() {
	        return new CustomUserDetailsContextMapper();
	    }
        
        @Bean
	    public LdapContextSource ldapContextSource() throws Exception {
	        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(LDAP_PROVIDER_URL);
	        return contextSource;
	    }
        
        @Bean
	    public LdapAuthenticationProvider ldapAuthenticationProvider() throws Exception {
	        LdapAuthenticationProvider lAP = new LdapAuthenticationProvider(ldapAuthenticator());
	        lAP.setUserDetailsContextMapper(userDetailsContextMapper());
	        return lAP;
	    }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client1")
                    .secret("secret_client")
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes("user_info")
                    .autoApprove(true)
                    .and().withClient("client2")
                    .secret("secret_client")
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes("user_info")
                    .autoApprove(true);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }
    }
}
