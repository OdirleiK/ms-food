package br.com.mspayments.msorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

	private Long id;
    private Integer amount;
    private String description;
}
