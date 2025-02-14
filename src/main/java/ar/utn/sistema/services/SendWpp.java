package ar.utn.sistema.services;

import ar.utn.sistema.dto.DTOWhatsapp;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.heladera.UbicacionHeladeraService;
import okhttp3.OkHttpClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SendWpp {

    private static SendWpp instancia = null;
    private static final int maximaCantidadRegistrosDefault = 500;
    private static final String urlApi = "https://api.ultramsg.com/instance107251/";
    private final Retrofit retrofit;

    private SendWpp() {
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(urlApi)
                .client(getUnsafeOkHttpClient())
                .build();
    }

    public static SendWpp instancia() {
        if (instancia == null) {
            instancia = new SendWpp();
        }
        return instancia;
    }

    public void enviarWpp(String numero,String mensaje)  {
        try {

            MandarWhatsappService mandarWhatsappService = this.retrofit.create(MandarWhatsappService.class);
            DTOWhatsapp dtoWhatsapp = new DTOWhatsapp();
            dtoWhatsapp.setBody(mensaje);
            dtoWhatsapp.setTo(numero);
            dtoWhatsapp.setToken("72w5gkul42m3u68p");
            Call<Boolean> requestPosicion = mandarWhatsappService.mandarWhatsapp(dtoWhatsapp);
            Response<Boolean> responsePosicion = requestPosicion.execute();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
