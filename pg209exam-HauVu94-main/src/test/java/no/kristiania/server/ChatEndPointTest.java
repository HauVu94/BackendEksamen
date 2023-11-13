package no.kristiania.server;

import no.kristiania.ChatServer;
import no.kristiania.Database.Entities.User;
import no.kristiania.Database.InMemoryDataSource;
import no.kristiania.dto.NewChatDto;
import org.junit.jupiter.api.Test;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatEndPointTest {
    private ChatServer server;
    @Test
    void shouldAddAndListAllChat() throws Exception {

        server = new ChatServer(0, InMemoryDataSource.createDataSource());
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

        var getConnection =  (HttpURLConnection)new URL(server.getURL(),"/api/users").openConnection();
        assertThat(getConnection.getResponseCode()).isEqualTo(200);
        assertThat(getConnection.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("phonenumber");

        //USER2
        String userJson2 = "{\"username\":\"Amund\",\"phonenumber\":\"123999678\",\"email\":\"Amund@per.etter\"}";

        var postConnection2 = (HttpURLConnection)new URL(server.getURL(),"/api/users").openConnection();
        postConnection2.setRequestMethod("POST");
        postConnection2.setRequestProperty("Content-type", "application/json");
        postConnection2.setDoOutput(true);
        postConnection2.getOutputStream().write(userJson2.getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection2.getResponseCode()).as(postConnection2.getResponseMessage() + " for " + postConnection2.getURL())
                .isEqualTo(204);

        var getConnection2 =  (HttpURLConnection)new URL(server.getURL(),"/api/users").openConnection();
        assertThat(getConnection2.getResponseCode()).isEqualTo(200);
        assertThat(getConnection2.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("phonenumber");


        //CHAT
        String chatJson = "{\"creatorId\":2,\"subject\":\"Chat sub\",\"userId\":[{\"email\":\"ole@per.etter\",\"phonenumber\":\"12345678\",\"userId\":1,\"username\":\"Gunnarson\"}]}";
        var postChatConnection = (HttpURLConnection)new URL(server.getURL(),"/api/chats").openConnection();
        postChatConnection.setRequestMethod("POST");
        postChatConnection.setRequestProperty("Content-type", "application/json");
        postChatConnection.setDoOutput(true);
        postChatConnection.getOutputStream().write(chatJson.getBytes(StandardCharsets.UTF_8));

        assertThat(postChatConnection.getResponseCode()).as(postChatConnection.getResponseMessage() + " for " + postChatConnection.getURL())
                .isEqualTo(204);

        var connection = (HttpURLConnection)new URL(server.getURL(),"/api/chats").openConnection();

        assertThat(connection.getResponseCode()).as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("{\"chatId\":1,\"creator\":{\"email\":\"Amund@per.etter\",\"phonenumber\":\"123999678\",\"userId\":2,\"username\":\"Amund\"},\"subject\":\"Chat sub\"}");

    }
}
