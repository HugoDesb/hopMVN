package Validation;

import Object.CandidatTerm;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {
    /*
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("hugo.desbiolles@etu.univ-amu.fr", "AconerIMumPk");
        provider.setCredentials(AuthScope.ANY, credentials);


    DefaultHttpClient client = new DefaultHttpClient();
        client.setCredentialsProvider(provider);

    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("user1", "user1Pass");
    provider.setCredentials(AuthScope.ANY, credentials);


    HttpClient client = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider)
            .build();

    HttpResponse response = client.execute(
            new HttpGet(URL_SECURED_BY_BASIC_AUTHENTICATION));
    int statusCode = response.getStatusLine()
            .getStatusCode();

    assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    */
}
