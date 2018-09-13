/**
 * 
 */
package com.imooc.security.app;

import com.imooc.security.core.properties.OAuth2ClientProperties;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author zhailiang
 *
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      /*  clients.inMemory()
                .withClient("test")
                .secret("test")
                .accessTokenValiditySeconds(20)
                .and()
                .withClient("imooc")
                .secret("imoocsecret")
                .accessTokenValiditySeconds(1200);*/
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients() ) {
                builder.withClient(client.getClientId()).secret(client.getClientSecret())
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds());

            }
        }


    }
}
