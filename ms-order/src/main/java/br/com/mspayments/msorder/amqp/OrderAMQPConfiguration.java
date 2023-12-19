package br.com.mspayments.msorder.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

public class OrderAMQPConfiguration {
	
  @Bean
  public Jackson2JsonMessageConverter messageConverter(){
      return  new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                       Jackson2JsonMessageConverter messageConverter){
      RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
      rabbitTemplate.setMessageConverter(messageConverter);
      return  rabbitTemplate;
  }

  @Bean
  public Queue queueDetailsOrder() {
      return QueueBuilder
              .nonDurable("payments.details-order")
              .deadLetterExchange("payments.dlx")
              .build();
  }
  
  @Bean
  public FanoutExchange fanoutExchage() {
	  return ExchangeBuilder.fanoutExchange("payments.ex").build();
  }
  
  @Bean
  public Binding bindPaymentoOrder(FanoutExchange fanoutExchage) {
	  return BindingBuilder.bind(queueDetailsOrder()).to(fanoutExchage);
  }
  
  @Bean
  public RabbitAdmin creatRabbitAdmin(ConnectionFactory conn) {
		return new RabbitAdmin(conn);
  }
	
  @Bean
  public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin){
      return event -> rabbitAdmin.initialize();
  }
  
  @Bean
  public FanoutExchange deadLetterExchage() {
	  return ExchangeBuilder.fanoutExchange("payments.dlx").build();
  }
  
  @Bean
  public Queue filaDqlDetailsOrder() {
      return QueueBuilder
              .nonDurable("payments.details-order-dlq")
              .build();
  }
  
  @Bean
  public Binding bindDlxPaymentoOrder(FanoutExchange fanoutExchage) {
	  return BindingBuilder.bind(filaDqlDetailsOrder()).to(deadLetterExchage());
  }
  
}
