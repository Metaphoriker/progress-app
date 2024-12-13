package de.metaphoriker.progress.model.util;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoMapperTest {

    @Getter
    public static class Source {
        private String field1;
        private int field2;

        public Source(String field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    @Getter
    public static class Destination {
        private String field1;
        private int field2;
    }

    @Test
    public void testMap() {
        Source source = new Source("test", 123);
        Destination destination = DtoMapper.map(source, Destination.class);

        assertEquals(source.getField1(), destination.getField1());
        assertEquals(source.getField2(), destination.getField2());
    }
}
