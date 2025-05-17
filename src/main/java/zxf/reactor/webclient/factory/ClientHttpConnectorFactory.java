package zxf.reactor.webclient.factory;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.http.client.reactive.JettyClientHttpConnector;

import java.net.URI;


public class ClientHttpConnectorFactory {
    public static JettyClientHttpConnector jettyClientHttpConnector(){
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        HttpClient httpClient = new HttpClient(sslContextFactory) {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return enhance(request);
            }

            private Request enhance(Request request) {
                StringBuilder logBuilder = new StringBuilder();
                request.onRequestBegin(theRequest -> {
                    logBuilder.append("=================================================Request begin=================================================\n");
                    logBuilder.append("URI             : " + theRequest.getURI() + "\n");
                    logBuilder.append("Methed          : " + theRequest.getMethod() + "\n");
                });
                request.onRequestHeaders(theRequest -> {
                    logBuilder.append("Headers         : " + theRequest.getHeaders() + "\n");
                });
                request.onRequestContent((theRequest, content) -> {
                    //logBuilder.append("Request Body    : " + content.;
                });
                request.onRequestSuccess(theRequest -> {
                    logBuilder.append("=================================================Request end=================================================");
                });
                request.onResponseBegin(theResponse -> {
                    // append response status to group
                });
                request.onResponseHeaders(theResponse -> {
                    for (HttpField header : theResponse.getHeaders()) {
                        // append response headers to group
                    }
                });
                request.onResponseContent((theResponse, content) -> {
                    // append content to group
                });
                request.onResponseSuccess(theResponse -> {
                    //log.debug(group.toString());
                });
                return request;
            }
        };
        return new JettyClientHttpConnector(httpClient);
    }
}
