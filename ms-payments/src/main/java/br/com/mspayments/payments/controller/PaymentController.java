package br.com.mspayments.payments.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.mspayments.payments.dto.PaymentDTO;
import br.com.mspayments.payments.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
    private PaymentService service;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

    @GetMapping
    public Page<PaymentDTO> list(@PageableDefault(size = 10) Pageable paginacao) {
        return service.getAll(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> detail(@PathVariable @NotNull Long id) {
        PaymentDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> register(@RequestBody @Valid PaymentDTO dto, UriComponentsBuilder uriBuilder) {
        PaymentDTO payment = service.createPayment(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(payment.getId()).toUri();

        
        rabbitTemplate.convertAndSend("payment.created", payment);
        
        return ResponseEntity.created(endereco).body(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDTO dto) {
        PaymentDTO atualizado = service.updatePayment(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDTO> remove(@PathVariable @NotNull Long id) {
        service.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "confirmedWithoutIntegration")
    public void confirmPayment(@PathVariable @NotNull Long id){
        service.confirmPayment(id);
    }
    
    public void confirmedWithoutIntegration(Long id, Exception e){
        service.updateStatus(id);
    }
}
