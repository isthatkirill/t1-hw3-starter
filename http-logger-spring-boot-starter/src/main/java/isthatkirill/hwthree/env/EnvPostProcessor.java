package isthatkirill.hwthree.env;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Kirill Emelyanov
 */

public class EnvPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader propertySourceLoader;

    public EnvPostProcessor() {
        propertySourceLoader = new YamlPropertySourceLoader();
    }

    @Override
    @SneakyThrows
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ClassPathResource resource = new ClassPathResource("default.yaml");
        PropertySource<?> propertySource = propertySourceLoader.load("http", resource).get(0);
        environment.getPropertySources().addLast(propertySource);
    }
}