package br.com.mspayments.msorder.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

	@RabbitListener(queues = "payment.created")
	public void receiveMessage(Message message) {
		System.out.println("message receive:" + message.toString());
	}
}
