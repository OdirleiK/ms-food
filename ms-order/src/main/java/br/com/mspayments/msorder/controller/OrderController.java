package br.com.mspayments.msorder.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.mspayments.msorder.dto.OrderDto;
import br.com.mspayments.msorder.dto.StatusDto;
import br.com.mspayments.msorder.service.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
    private OrderService service;
	
	@GetMapping()
    public List<OrderDto> listarTodos() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> listById(@PathVariable @NotNull Long id) {
        OrderDto dto = service.findById(id);
        return  ResponseEntity.ok(dto);
    }

    @GetMapping("/porta")
    public String getPort(@Value("${local.server.port}") String porta){
        return String.format("Requisição respondida pela instância executando na porta %s", porta);
    }

    @PostMapping()
    public ResponseEntity<OrderDto> placeOrder(@RequestBody @Valid OrderDto dto, UriComponentsBuilder uriBuilder) {
        OrderDto createdOrder = service.creatOrder(dto);
        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(createdOrder.getId()).toUri();
        return ResponseEntity.created(endereco).body(createdOrder);

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto status){
       OrderDto dto = service.updateStatus(id, status);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
        service.approvePaymentOrder(id);
        return ResponseEntity.ok().build();

    }

    
}
