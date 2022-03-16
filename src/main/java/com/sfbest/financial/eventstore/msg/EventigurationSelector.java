package com.sfbest.financial.eventstore.msg;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class EventigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annotationType = EnableFmsEventStore.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected",
                annotationType.getSimpleName(), importingClassMetadata.getClassName()));

        String[] imports;
        if (attributes.containsKey("modular") && attributes.getBoolean("modular")) {
            imports = new String[] { CusomerEventAutoConfig.class.getName() };
        }
        else {
            imports = new String[] { DefaultEventAutoConfig.class.getName() };
        }

        return imports;
    }
}
