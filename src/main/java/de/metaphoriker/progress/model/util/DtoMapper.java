package de.metaphoriker.progress.model.util;

import java.lang.reflect.Field;

public class DtoMapper {
    public static <TSource, TDestination> TDestination map(TSource source, Class<TDestination> destinationClass) {
        try {
            TDestination destination = destinationClass.getDeclaredConstructor().newInstance();

            for (Field sourceField : source.getClass().getDeclaredFields()) {
                try {
                    Field destinationField = destinationClass.getDeclaredField(sourceField.getName());
                    if (destinationField.getType() == sourceField.getType()) {
                        sourceField.setAccessible(true);
                        destinationField.setAccessible(true);
                        destinationField.set(destination, sourceField.get(source));
                    }
                } catch (NoSuchFieldException ignored) {
                }
            }

            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Mapping", e);
        }
    }
}
