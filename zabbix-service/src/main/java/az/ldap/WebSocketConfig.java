package az.ldap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import az.ldap.zabbix.service.LoginHistoryService;


/**
 * Set up our websocket configuration, which uses STOMP, and configure our
 * endpoints.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Value(value = "${allow.origin.access.control}")
	private String allowOriginAccessControl;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/zabbix/error.event/", "/zabbix/async.event/", "/zabbix/resource.event/",
				"/zabbix/action.event/", "/zabbix/test", "/zabbix/vmdashboard",  "/zabbix/dashboard");
		config.setApplicationDestinationPrefixes("/zabbix");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/zabbix/ws").setAllowedOrigins(allowOriginAccessControl).withSockJS();
	}

	@Bean
    public TokenSecurityChannelInterceptor presenceChannelInterceptor() {
		LoginHistoryService loginAuth = applicationContext.getBean(LoginHistoryService.class);
        return new TokenSecurityChannelInterceptor(loginAuth);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(presenceChannelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(8);
        registration.setInterceptors(presenceChannelInterceptor());
    }

    @Bean
    public WebSocketHandler subProtocolWebSocketHandler() {
        return new CustomSubProtocolWebSocketHandler(clientInboundChannel(), clientOutboundChannel());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    	registry.setMessageSizeLimit(1024*1024);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return super.configureMessageConverters(messageConverters);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
    }
}
