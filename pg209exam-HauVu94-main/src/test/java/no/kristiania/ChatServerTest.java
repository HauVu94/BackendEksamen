package no.kristiania;

import no.kristiania.Database.InMemoryDataSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatServerTest {


    @Test
    void shouldServeFrontPage() throws Exception {
        ChatServer server;
        server = new ChatServer(0, InMemoryDataSource.createDataSource());

        server.start();

        var connection = (HttpURLConnection)new URL(server.getURL(),"/").openConnection();

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage(), " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>Vite + React</title>");

    }
}
