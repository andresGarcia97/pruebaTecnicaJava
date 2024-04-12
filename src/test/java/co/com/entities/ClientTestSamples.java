package co.com.entities;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import co.com.entities.ClientEntity;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClientEntity getClientSample1() {
        return new ClientEntity().id(1L).identification("identification1").name("name1").lastName("lastName1").email("email1");
    }

    public static ClientEntity getClientSample2() {
        return new ClientEntity().id(2L).identification("identification2").name("name2").lastName("lastName2").email("email2");
    }

    public static ClientEntity getClientRandomSampleGenerator() {
        return new ClientEntity()
            .id(longCount.incrementAndGet())
            .identification(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
