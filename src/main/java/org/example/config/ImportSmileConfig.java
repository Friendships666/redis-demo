package org.example.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportSmileConfig implements BeanDefinitionRegistryPostProcessor {
    private final String CLASS_PATH = "META-INF/smile.properties";
    private Map<String, Object> handlerMappings = null;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Properties mappings = null;
        try {
            mappings = PropertiesLoaderUtils.loadAllProperties(CLASS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> hMappings = new ConcurrentHashMap<>(mappings.size());
        CollectionUtils.mergePropertiesIntoMap(mappings, hMappings);
        this.handlerMappings = hMappings;
    }

    public Map<String, Object> getSmileHandlerMappings(){
        return handlerMappings;
    }
}
