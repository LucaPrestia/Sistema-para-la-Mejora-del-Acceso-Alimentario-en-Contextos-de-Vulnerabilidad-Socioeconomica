package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.Coordenadas;

import org.springframework.beans.factory.annotation.Qualifier;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


import java.util.List;

public interface UbicacionHeladeraService {
  @GET("ubicacionesHeladera")
  Call<List<Coordenadas>> ubicacionesHeladera(@Query(value = "punto")Coordenadas punto, @Query(value="radio") float radio);

}