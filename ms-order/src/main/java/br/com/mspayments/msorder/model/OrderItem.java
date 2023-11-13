package br.com.mspayments.msorder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Integer amount;

    private String description;

    @ManyToOne(optional=false)
    private Order order;

}
