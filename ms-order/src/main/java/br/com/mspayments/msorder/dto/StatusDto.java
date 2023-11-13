package br.com.mspayments.msorder.dto;

import br.com.mspayments.msorder.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {

	private Status status;
}
