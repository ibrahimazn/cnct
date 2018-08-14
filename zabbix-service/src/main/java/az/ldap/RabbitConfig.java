package az.ldap;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import az.ldap.sync.messages.ActionListener;
import az.ldap.sync.messages.AsynchronousJobListener;
import az.ldap.sync.messages.ZabbixListener;
import az.ldap.sync.service.LdapSyncService;
import az.ldap.sync.util.CloudStackUserService;
import az.ldap.zabbix.service.HostService;

/**
 * RabbitMQ configuration to publish/consume messages from CS server via
 * RabbitMQ server with specified Exchange name. All CS server events are
 * tracked and update the status of resources in APP DB, update usage of
 * resource in APP DB, sync APP DB while action directly handled at CS server,
 * CS server Alert.
 */
@Configuration
public class RabbitConfig {
	
	/** RabbitMQ host name. */
	@Value(value = "${spring.rabbit.host}")
	private String hostName;

	/** RabbitMQ virtual host name. */
	@Value(value = "${spring.rabbit.vhost}")
	private String vhost;

	/** RabbitMQ login user name. */
	@Value(value = "${spring.rabbit.username}")
	private String username;

	/** RabbitMQ login password. */
	@Value(value = "${spring.rabbit.password}")
	private String password;

	/** CS server action routing key pattern. */
	@Value(value = "${spring.rabbit.server.action.pattern}")
	private String csActionPattern;

	/** CS server asynchronous routing key pattern. */
	@Value(value = "${spring.rabbit.server.asynchJob.pattern}")
	private String csAsynchJobPattern;
	
	/** CS server zabbix routing key pattern. */
    @Value(value = "${spring.rabbit.server.zabbix.pattern}")
    private String zabbixPattern;

	/** CS server exchange name. */
	@Value(value = "${spring.rabbit.exchange.name}")
	private String exchangeName;
	
	/** CS server action queue name. */
	@Value(value = "${spring.rabbit.server.action.queue}")
	private String csActionQueueName;

	/** CS server asyncJob queue name. */
	@Value(value = "${spring.rabbit.server.asynchJob.queue}")
	private String csAsynchJobQueueName;
	
	/** CS server zabbix queue name. */
    @Value(value = "${spring.rabbit.server.zabbix.queue}")
    private String zabbixQueueName;

	/** Admin username. */
	@Value("${backend.username}")
	private String backendAdminUsername;

	/** Admin role. */
	@Value("${backend.role}")
	private String backendAdminRole;

	/** Application context reference. */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Queue for action event messages.
	 *
	 * @return queue name
	 */
	@Bean
	Queue queue() {
		return new Queue(csActionQueueName, true);
	}

	/**
	 * Queue for asynchronous job messages.
	 *
	 * @return queue name
	 */
	@Bean
	Queue queue1() {
		return new Queue(csAsynchJobQueueName, true);
	}
	
	/**
     * Queue for zabbix messages.
     *
     * @return queue name
     */
    @Bean
    Queue zabbixQueue() {
        return new Queue(zabbixQueueName, true);
    }

	/**
	 * Create Topic Exchange from given exchange name specified in CS server
	 * event-bus configuration.
	 *
	 * @return exchange name
	 */
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchangeName);
	}

	/**
	 * Binding Queue with exchange to get action related event message from CS
	 * server.
	 *
	 * @param queue name of the queue.
	 * @param exchange name of the exchange.
	 * @return binding object
	 */
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(csActionPattern);
	}

	/**
	 * Binding Queue with exchange to get asynchronous job related event message
	 * from CS server.
	 *
	 * @param queue1 name of the queue1.
	 * @param exchange name of the exchange.
	 * @return binding object
	 */
	@Bean
	Binding binding1(Queue queue1, TopicExchange exchange) {
		return BindingBuilder.bind(queue1).to(exchange).with(csAsynchJobPattern);
	}
	
	/**
     * Binding Queue with exchange to get zabbix message from CS server event.
     *
     * @param zabbixQueue name of the queue4.
     * @param exchange name of the exchange.
     * @return binding object
     */
    @Bean
    Binding binding2(Queue zabbixQueue, TopicExchange exchange) {
        return BindingBuilder.bind(zabbixQueue).to(exchange).with(zabbixPattern);
    }

	/**
	 * Convenience "factory" to facilitate opening a link Connection to an AMQP
	 * broker.
	 *
	 * @return connection factory
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostName);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(vhost);
		return connectionFactory;
	}

	/**
	 * Message listener adapter that delegates the handling of action event
	 * messages to target listener methods via reflection, with flexible type
	 * conversion.
	 *
	 * @return message action event listener.
	 */
	@Bean
	MessageListenerAdapter actionListenerAdapter() {
		LdapSyncService ldapSyncService = applicationContext.getBean(LdapSyncService.class);
		return new MessageListenerAdapter(new ActionListener(ldapSyncService));
	}

	/**
	 * Message listener adapter that delegates the handling of asynchronous job
	 * messages to target listener methods via reflection, with flexible type
	 * conversion.
	 *
	 * @return message asynchronous job listener.
	 */
	@Bean
	MessageListenerAdapter asynchJobListenerAdapter() {
		CloudStackUserService cloudStackUserService = applicationContext.getBean(CloudStackUserService.class);
		LdapSyncService ldapSyncService = applicationContext.getBean(LdapSyncService.class);
		return new MessageListenerAdapter(new AsynchronousJobListener(cloudStackUserService,
				ldapSyncService));
	}
	
	/**
	 * Message listener adapter that delegates the handling of asynchronous job
	 * messages to target listener methods via reflection, with flexible type
	 * conversion.
	 *
	 * @return message zabbix listener.
	 */
	@Bean
	MessageListenerAdapter zabbixListenerAdapter() {
		HostService hostService = applicationContext.getBean(HostService.class);
		return new MessageListenerAdapter(new ZabbixListener(hostService));
	}

	/**
	 * Add message listener to message listener container with specified queue
	 * to listen/consume action event message.
	 *
	 * @param queue queue for action event.
	 * @return message listener container for action event messages.
	 */
	@Bean
	SimpleMessageListenerContainer actionContainer(Queue queue) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueues(queue);
		container.setMessageListener(actionListenerAdapter());
		return container;
	}

	/**
	 * Add message listener to message listener container with specified queue
	 * to listen/consume asynchronous job message.
	 *
	 * @param queue1 queue for asynchronous event.
	 * @return message listener container for asynchronous message.
	 */
	@Bean
	SimpleMessageListenerContainer asynchJobContainer(Queue queue1) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueues(queue1);
		container.setMessageListener(asynchJobListenerAdapter());
		return container;
	}
	
	/**
	 * Add message listener to message listener container with specified queue
	 * to listen/consume zabbix message.
	 *
	 * @param zabbixQueue queue for asynchronous event.
	 * @return message listener container for asynchronous message.
	 */
	@Bean
	SimpleMessageListenerContainer zabbixContainer(Queue zabbixQueue) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueues(zabbixQueue);
		container.setMessageListener(zabbixListenerAdapter());
		return container;
	}
}
