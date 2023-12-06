package br.com.mspayments.msorder.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.mspayments.msorder.dto.PaymentDto;

@Component
public class PaymentListener {

	@RabbitListener(queues = "payments.details-order")
	public void receiveMessage(PaymentDto payment) {
		String message = """
				Dados do pagamento: %S
				Numero do pedido: %s
				Valor: %s
				Status %s
				""".formatted(payment.getId(), 
							  payment.getOrderId(), 
							  payment.getValue(), 
							  payment.getStatus());
		
        System.out.println("Recebi a mensagem " + message);
	}
}
