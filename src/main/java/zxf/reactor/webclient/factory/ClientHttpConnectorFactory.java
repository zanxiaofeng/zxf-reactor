package zxf.reactor.webclient.factory;


import org.eclipse.jetty.client.HttpClient;
import org.springframework.http.client.reactive.JettyClientHttpConnector;

public class ClientHttpConnectorFactory {
    public static JettyClientHttpConnector jettyClientHttpConnector() {
        HttpClient httpClient = new HttpClient();
        return new JettyClientHttpConnector();
    }
}
