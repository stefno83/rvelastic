package rvelastic.client;


import rvelastic.configuration.AppConfiguration;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.OpType;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenSearchClientWrapper {

  OpenSearchClient openSearchClient;
  AppConfiguration appConfiguration;

  public OpenSearchClientWrapper(AppConfiguration appConfiguration) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    this.appConfiguration = appConfiguration;

    /*

    SdkHttpClient httpClient = ApacheHttpClient.builder().build();

    openSearchClient = new OpenSearchClient(
        new AwsSdk2Transport(
            httpClient,
            "search-universita-ecvfs5uedashidstyvqjmpv54y.eu-central-1.es.amazonaws.com",
            "es",
            Region.EU_CENTRAL_1, // signing service region
            AwsSdk2TransportOptions.builder().build()
        )
    );
     */

    log.info("Hello 1.1");

    SSLContext sslContext = SSLContexts.custom()
        .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
        .build();

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(appConfiguration.getOpensearch().getUser(), appConfiguration.getOpensearch().getPassword()));

    //Create a client.
    RestClientBuilder builder = RestClient.builder(new HttpHost(appConfiguration.getOpensearch().getAddress(), appConfiguration.getOpensearch().getPort(), "https"))
        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
          @Override
          public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
            httpClientBuilder.setSSLContext(sslContext);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setSSLHostnameVerifier((s, sslSession) -> true);
            return httpClientBuilder;
          }
        });

    openSearchClient = new OpenSearchClient(new RestClientTransport(builder.build(), new JacksonJsonpMapper()));

    try {

      InfoResponse info = openSearchClient.info();
      log.info("Connected to {} version {}", info.version().distribution(), info.version().number());

    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      throw new RuntimeException("Failed", e);
    }

  }

  public void putDataOnIndex(JSONObject data, String index) throws IOException {
    IndexRequest<Map<String, Object>> indexRequest = new IndexRequest.Builder<Map<String, Object>>()
        .index(index)
        .opType(OpType.Create)
        .document(data.toMap())
        .build();

    openSearchClient.index(indexRequest);
  }

}
