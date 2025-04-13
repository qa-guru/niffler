package guru.qa.niffler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class PropertiesLogger implements ApplicationListener<ApplicationPreparedEvent> {

  private static final Logger LOG = LoggerFactory.getLogger(PropertiesLogger.class);

  private ConfigurableEnvironment environment;
  private boolean isFirstRun = true;

  @Override
  public void onApplicationEvent(ApplicationPreparedEvent event) {
    if (isFirstRun) {
      environment = event.getApplicationContext().getEnvironment();
      printProperties();
    }
    isFirstRun = false;
  }

  public void printProperties() {
    for (EnumerablePropertySource<?> propertySource : findPropertiesPropertySources()) {
      LOG.info("******* {} *******", propertySource.getName());
      String[] propertyNames = propertySource.getPropertyNames();
      Arrays.sort(propertyNames);
      for (String propertyName : propertyNames) {
        String resolvedProperty = environment.getProperty(propertyName);
        String sourceProperty = Objects.requireNonNull(propertySource.getProperty(propertyName)).toString();
        if (Objects.equals(resolvedProperty, sourceProperty)) {
          LOG.info("{}={}", propertyName, resolvedProperty);
        } else {
          LOG.info("{}={} OVERRIDDEN to {}", propertyName, sourceProperty, resolvedProperty);
        }
      }
    }
  }

  private List<EnumerablePropertySource<?>> findPropertiesPropertySources() {
    List<EnumerablePropertySource<?>> propertiesPropertySources = new LinkedList<>();
    for (PropertySource<?> propertySource : environment.getPropertySources()) {
      if (propertySource instanceof EnumerablePropertySource) {
        propertiesPropertySources.add((EnumerablePropertySource<?>) propertySource);
      }
    }
    return propertiesPropertySources;
  }
}