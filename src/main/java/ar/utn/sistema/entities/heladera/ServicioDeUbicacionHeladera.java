package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServicioDeUbicacionHeladera {
  private static ServicioDeUbicacionHeladera instancia = null;
  private static final int maximaCantidadRegistrosDefault = 500;
  private static final String urlApi = "https://api.mockapi.com/";
  private final Retrofit retrofit;

  private ServicioDeUbicacionHeladera() {
    this.retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(urlApi)
            .client(getUnsafeOkHttpClient())
            .build();
  }

  public static ServicioDeUbicacionHeladera instancia() {
    if (instancia == null) {
      instancia = new ServicioDeUbicacionHeladera();
    }
    return instancia;
  }

  public List<Direccion> listadoPosicionesHeladera(Coordenadas punto, double radio) throws IOException {
    UbicacionHeladeraService heladeraService = this.retrofit.create(UbicacionHeladeraService.class);
    Call<List<Direccion>> requestPosicion = heladeraService.ubicacionesHeladera(punto, radio);
    Response<List<Direccion>> responsePosicion = requestPosicion.execute();
    return responsePosicion.body();
  }

  private OkHttpClient getUnsafeOkHttpClient() {
    try {
      TrustManager[] trustAllCerts = new TrustManager[]{
              new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new java.security.cert.X509Certificate[]{};
                }
              }
      };

      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      return new OkHttpClient.Builder()
              .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
              .hostnameVerifier((hostname, session) -> true)
              .connectTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS)
              .build();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
