package org.midgard.ragnarok.configs;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
public class DatabaseConfiguration {
    @Bean
    public HttpClient httpClient() throws MalformedURLException {
        return new StdHttpClient.Builder().url("http://localhost:5984").build();
    }

    @Bean
    public CouchDbInstance dbInstance() throws MalformedURLException {
        return new StdCouchDbInstance(httpClient());
    }

    @Bean
    public CouchDbConnector dbConnector() throws MalformedURLException {
        CouchDbConnector db = new StdCouchDbConnector("midgard", dbInstance());
        db.createDatabaseIfNotExists();
        return db;
    }
}
