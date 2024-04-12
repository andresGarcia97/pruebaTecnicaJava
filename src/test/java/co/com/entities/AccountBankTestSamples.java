package co.com.entities;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import co.com.entities.AccountBankEntity;

public class AccountBankTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountBankEntity getAccountBankSample1() {
        return new AccountBankEntity().id(1L).number(1L);
    }

    public static AccountBankEntity getAccountBankSample2() {
        return new AccountBankEntity().id(2L).number(2L);
    }

    public static AccountBankEntity getAccountBankRandomSampleGenerator() {
        return new AccountBankEntity().id(longCount.incrementAndGet()).number(longCount.incrementAndGet());
    }
}
