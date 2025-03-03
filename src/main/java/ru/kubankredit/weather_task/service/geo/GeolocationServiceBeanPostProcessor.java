package ru.kubankredit.weather_task.service.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.kubankredit.weather_task.model.PointPos;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GeolocationServiceBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceBeanPostProcessor.class);

    Map<Object, Object> pointPosMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GeoService) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                if (!pointPosMap.containsKey(args[0])) {
                    Object invoke = method.invoke(bean, args);
                    pointPosMap.put(args[0], invoke);
                    return invoke;
                } else {
                    PointPos point = (PointPos)pointPosMap.get(args[0]);
                    LOGGER.info("Геолокация города {} получена = {}", args[0], point);
                    return point;
                }
            });
        }
        return bean;
    }
}
