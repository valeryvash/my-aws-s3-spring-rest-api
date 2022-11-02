package net.valeryvash.myawss3springrestapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * This class goal is to expose all app endpoints during app startup.
 * Also, it can be raised as "bad replacement" of ultimate idea "Endpoints" feature.
 */
@Component
@Slf4j
public class ExposeAllEndpoints implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("======EXPOSE ALL ENDPOINTS======");
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods()
                .forEach(
                        (requestMappingInfo, handlerMethod) -> {
                            log.info("mapping info: {} ", requestMappingInfo);
                            log.info("handler method: {} ", handlerMethod);
                        }
                );

        log.info("==============================");
    }
}
