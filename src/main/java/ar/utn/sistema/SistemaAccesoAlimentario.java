package ar.utn.sistema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaAccesoAlimentario {

    private static Logger LOG = LoggerFactory.getLogger(SistemaAccesoAlimentario.class);
    public static void main(String[] args) {
        SpringApplication.run(SistemaAccesoAlimentario.class, args);
        LOG.info("aplicacion spring iniciada");
    }
}
