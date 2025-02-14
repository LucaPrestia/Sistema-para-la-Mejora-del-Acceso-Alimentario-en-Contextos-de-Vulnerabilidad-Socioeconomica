package ar.utn.sistema.services;

import ar.utn.sistema.dto.DTOWhatsapp;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import retrofit2.Call;
import retrofit2.http.*;

import javax.validation.constraints.Positive;
import java.util.List;

public interface MandarWhatsappService {
  @POST("messages/chat")
  Call<Boolean> mandarWhatsapp(@Body DTOWhatsapp whatsapp);

}