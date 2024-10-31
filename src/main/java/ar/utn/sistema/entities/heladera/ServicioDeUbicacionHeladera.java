package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.Coordenadas;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;

public class ServicioDeUbicacionHeladera {
  private static ServicioDeUbicacionHeladera instancia = null;
  private static int maximaCantidadRegistrosDefault = 500;
  private static final String urlApi = "https://api.mockapi.com/";
  private Retrofit retrofit;

  private ServicioDeUbicacionHeladera() {
    this.retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(urlApi)
        .build();
  }

  public static ServicioDeUbicacionHeladera instancia(){
    if(instancia== null){
      instancia = new ServicioDeUbicacionHeladera();
    }
    return instancia;
  }

  public List<Coordenadas> listadoPosicionesHeladera(Coordenadas punto, double radio) throws IOException {
    UbicacionHeladeraService heladeraService = this.retrofit.create(UbicacionHeladeraService.class);
    Call<List<Coordenadas>> requestPosicion = heladeraService.ubicacionesHeladera(punto,radio);
    Response<List<Coordenadas>> responsePosicion = requestPosicion.execute();
    return responsePosicion.body();
  }
}