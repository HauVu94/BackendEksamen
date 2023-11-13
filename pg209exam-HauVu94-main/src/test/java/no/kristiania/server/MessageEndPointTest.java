package no.kristiania.server;

import no.kristiania.ChatServer;
import no.kristiania.Database.InMemoryDataSource;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageEndPointTest {
    ChatServer server;
    @Test
    void shouldAddAndListMessage() throws Exception {

        server = new ChatServer(0,InMemoryDataSource.createDataSource());
        server.start();

        //USER
        String userJson = "{\"username\":\"Gunnarson\",\"phonenumber\":\"12345678\",\"email\":\"ole@per.etter\"}";

        var postConnection = (HttpURLConnection)new URL(server.getURL(),"/api/users").openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection.getResponseCode()).as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        //MESSAGE
        String messageJson = "{\"senderId\":1,\"receiverId\":2,\"title\":\"This is a title\",\"body\":\"body of ess\"}";

        var postMessageConnection = (HttpURLConnection)new URL(server.getURL(),"/api/messages").openConnection();
        postMessageConnection.setRequestMethod("POST");
        postMessageConnection.setRequestProperty("Content-type", "application/json");
        postMessageConnection.setDoOutput(true);
        postMessageConnection.getOutputStream().write(messageJson.getBytes(StandardCharsets.UTF_8));

        assertThat(postMessageConnection.getResponseCode()).as(postMessageConnection.getResponseMessage() + " for " + postMessageConnection.getURL())
                .isEqualTo(204);

        var connection = (HttpURLConnection)new URL(server.getURL(),"/api/messages").openConnection();

        assertThat(connection.getResponseCode()).as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("{\"body\":\"body of ess\",\"messageId\":1,\"subject\":\"This is a title\"");
    }


}
