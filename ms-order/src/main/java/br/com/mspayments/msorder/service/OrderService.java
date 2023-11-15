package br.com.mspayments.msorder.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mspayments.msorder.dto.OrderDto;
import br.com.mspayments.msorder.dto.StatusDto;
import br.com.mspayments.msorder.model.Order;
import br.com.mspayments.msorder.model.Status;
import br.com.mspayments.msorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	@Autowired
    private OrderRepository repository;

    @Autowired
    private final ModelMapper modelMapper;

    public List<OrderDto> getAll() {
    	return repository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }
    
    public OrderDto findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(order, OrderDto.class);
    }
    
    public OrderDto creatOrder(OrderDto dto) {
    	Order order = modelMapper.map(dto, Order.class);
        order.setDateHour(LocalDateTime.now());
        order.setStatus(Status.COMPLETED);
        order.getItens().forEach(item -> item.setOrder(order));
        Order salvo = repository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }
    
    public OrderDto updateStatus(Long id, StatusDto dto) {
    	Order order = repository.porIdComItens(id);
        if (order == null) {
            throw new EntityNotFoundException();
        }
        order.setStatus(dto.getStatus());
        repository.atualizaStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDto.class);
    }
    
    public void approvePaymentOrder(Long id) {
    	Order order = repository.porIdComItens(id);
        if (order == null) {
            throw new EntityNotFoundException();
        }
        order.setStatus(Status.PAID);
        repository.atualizaStatus(Status.PAID, order);
    }
    
}
