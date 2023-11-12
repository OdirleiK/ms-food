package br.com.mspayments.payments.service;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.mspayments.payments.dto.PaymentDTO;
import br.com.mspayments.payments.model.Payment;
import br.com.mspayments.payments.model.Status;
import br.com.mspayments.payments.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Page<PaymentDTO> getAll(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, PaymentDTO.class));
    }

    public PaymentDTO findById(Long id) {
        Payment Payment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(Payment, PaymentDTO.class);
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CRIADO);
        repository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setId(id);
        payment = repository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

}
