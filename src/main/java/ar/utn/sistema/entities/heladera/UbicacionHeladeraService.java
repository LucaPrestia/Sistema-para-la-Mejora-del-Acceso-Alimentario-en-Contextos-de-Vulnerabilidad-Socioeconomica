package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.Coordenadas;

import ar.utn.sistema.entities.Direccion;
import org.springframework.beans.factory.annotation.Qualifier;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


import java.util.List;

public interface UbicacionHeladeraService {
  @Headers("x-api-key:d1f8fec79a5f4ee49849d52f4f710968")
  @GET("ubicacionesHeladera")
  Call<List<Direccion>> ubicacionesHeladera(@Query(value = "punto")Coordenadas punto, @Query(value="radio") double radio);

}