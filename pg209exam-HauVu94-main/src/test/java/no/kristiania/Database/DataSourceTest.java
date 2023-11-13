package no.kristiania.Database;

import org.junit.jupiter.api.extension.*;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(DataSourceTest.Provider.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DataSourceTest {
    class Provider implements ParameterResolver{

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException{
            return parameterContext.getParameter().getType() == DataSource.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException{
            return InMemoryDataSource.createDataSource();
        }
    }
}
