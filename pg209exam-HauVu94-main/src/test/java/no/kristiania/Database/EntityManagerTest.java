package no.kristiania.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.junit.jupiter.api.extension.*;

import javax.naming.NamingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(EntityManagerTest.Implementation.class)
public @interface EntityManagerTest {
    class Implementation implements ParameterResolver, BeforeEachCallback, AfterEachCallback{
        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException{
            return parameterContext.getParameter().getType() == EntityManager.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context){
            try{
                new Resource("jdbc/dataSource", new InMemoryDataSource().createDataSource());
            }catch(NamingException e){
                throw new ParameterResolutionException(e.toString());
            }
            var entityManager = Persistence.createEntityManagerFactory("users").createEntityManager();
            context.getStore(ExtensionContext.Namespace.GLOBAL).put("entityManager", entityManager);
            return entityManager;
        }

        @Override
        public void beforeEach(ExtensionContext context) throws Exception{
            var entityManager = context.getStore(ExtensionContext.Namespace.GLOBAL).get("entityManager", EntityManager.class);
            entityManager.getTransaction().begin();
        }

        @Override
        public void afterEach(ExtensionContext context) throws Exception{
            var entityManager = context.getStore(ExtensionContext.Namespace.GLOBAL).get("entityManager",EntityManager.class);
            entityManager.getTransaction().rollback();
        }
    }
}
