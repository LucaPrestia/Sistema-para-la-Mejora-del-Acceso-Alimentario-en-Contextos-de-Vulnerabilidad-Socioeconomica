package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter@NoArgsConstructor
public class DTOWhatsapp {
 private String token;
 private String to;
 private String body;
}
