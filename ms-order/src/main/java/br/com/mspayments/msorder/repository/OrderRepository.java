package br.com.mspayments.msorder.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.mspayments.msorder.model.Order;
import br.com.mspayments.msorder.model.Status;

public interface OrderRepository extends JpaRepository<Order, Long>{

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = :status where o = :order")
    void atualizaStatus(Status status, Order order);

    @Query(value = "SELECT o from Order o LEFT JOIN FETCH o.itens where o.id = :id")
    Order porIdComItens(Long id);
}
