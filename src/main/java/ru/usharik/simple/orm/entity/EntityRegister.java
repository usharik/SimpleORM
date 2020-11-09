package ru.usharik.simple.orm.entity;


import java.util.HashMap;
import java.util.Map;

public class EntityRegister {

    public Map<Class<?>, EntityDescriptor> entityDescriptorMap = new HashMap<>();

    public void register(Class<?> entityClass) {
        entityDescriptorMap.put(entityClass, new EntityDescriptor(entityClass));
    }

    public EntityDescriptor getEntityDescriptor(Class<?> entityClass) {
        return entityDescriptorMap.get(entityClass);
    }
}
