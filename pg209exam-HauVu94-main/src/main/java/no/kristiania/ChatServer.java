package no.kristiania;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Persistence;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Properties;

public class ChatServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ChatServer.class);
    private  final Server server;

    public ChatServer(int port, DataSource dataSource) throws IOException, NamingException {
        this.server = new Server(port);
        server.setHandler(new HandlerList(
                createApiContext(dataSource),
                createWebAppContext()
        ));

    }

    private WebAppContext createWebAppContext() throws IOException {
        var context = new WebAppContext();
        context.setContextPath("/");

        Resource resource = Resource.newClassPathResource("/webapp");
        File sourceDirectory  = getSourceDirectory(resource);
        if (sourceDirectory != null){
            context.setBaseResource(Resource.newResource(sourceDirectory));
            context.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");
        }else {
            context.setBaseResource(resource);
        }
        return context;
    }

    private File getSourceDirectory(Resource resource) throws IOException {
        if (resource.getFile() == null){
            return null;
        }
        var SourceDirectory = new File(resource.getFile().getAbsolutePath()
                .replace('\\', '/')
                .replace("target/classes", "src/main/resources"));
        return SourceDirectory.exists() ? SourceDirectory : null;
    }

    private ServletContextHandler createApiContext(DataSource dataSource) throws NamingException {
        var context = new ServletContextHandler(server, "/api");
        new org.eclipse.jetty.plus.jndi.Resource("jdbc/dataSource", dataSource);
        var entityManagerFactory = Persistence.createEntityManagerFactory("chatRoom");
        ChatConfig chatConfig = new ChatConfig(entityManagerFactory);
        context.addServlet(new ServletHolder(new ServletContainer(
                chatConfig
        )), "/*");
        context.addFilter(new FilterHolder(new EntityManagerFilter(chatConfig)), "/*", EnumSet.of(DispatcherType.REQUEST));
        return context;
    }

    public void start() throws Exception {
        server.start();
    }

    public URL getURL() throws MalformedURLException {
        return server.getURI().toURL();
    }

    public static void main(String[] args) throws Exception {
        var port = Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);

        var properties = new Properties();
        try (var fileReader = new FileReader("application.properties")) {
            properties.load(fileReader);
        }

        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
        dataSource.setUsername(properties.getProperty("jdbc.username"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();


        var server = new ChatServer(port, dataSource);
        server.start();
        logger.info("Started at {}", server.getURL());
    }
}
