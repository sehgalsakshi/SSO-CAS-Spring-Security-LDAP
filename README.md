# Spring Boot Single Sign-on Demo Application

This repo contains a centralized authentication server and two client applications.
Client apps can be accessed only if user has been authenticated (once) at cas with oauth and LDAP authentication.

For authentication without LDAP, please refer to commented out code in SsoServerApplication_withoutLDAP.java

For authentication, spring security is used.

# In depth explanation of architecture

<b>Single sign-on (SSO) is an authentication method that enables users to securely authenticate with multiple applications and websites by using just one set of credentials.</b>

SSO works based upon a trust relationship set up between an application, known as the service provider, and an identity provider.

In our case, Identity provider i.e., application for managing authentication is SSO-Server.
Server Providers are the two client application, i.e, they provide service to authenticated clients.

#SSO Workflow:
1. A user browses App1/ App2.
2. Above application sends a token that contains some information about the user, like their email address, to the SSO-Server application as part of a request to authenticate the user.
3. SSO-Server first checks to see whether the user has already been authenticated, in which case it will grant the user access to the application and skip to step 5.
4. If the user hasn’t logged in, they will be prompted to do so by providing the credentials required by the SSO-Server. This would be used by Authentication Manager to authenticate. 
This could simply be a username and password or it might include some other form of authentication like a One-Time Password (OTP).
5. Once SSO-Server validates the credentials provided, it will send a token back to the client application confirming a successful authentication.
This token is passed through the user’s browser to the Client application.
The token that is received by the Service Provider is validated according to the trust relationship that was set up between the Service Provider and the Identity Provider during the initial configuration.
The user is granted access to the client application.

Now if user logged in App1 in step 1. User would also be logged in to App2.
Also a complex role based authentication can also be managed which would be authenticate users only for the client application whose roles they've been assigned.

<a href="https://www.onelogin.com/learn/how-single-sign-on-works">For more details on SSO, please read this </a>