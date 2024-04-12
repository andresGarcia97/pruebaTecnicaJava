package co.com.entities;

import java.util.UUID;

import co.com.entities.TransactionEntity;

public class TransactionTestSamples {

    public static TransactionEntity getTransactionSample1() {
        return new TransactionEntity().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static TransactionEntity getTransactionSample2() {
        return new TransactionEntity().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static TransactionEntity getTransactionRandomSampleGenerator() {
        return new TransactionEntity().id(UUID.randomUUID());
    }
}
