package ru.netology.data;
import com.github.javafaker.Faker;

import java.util.Random;

public class DataGenerator {

    Faker faker = new Faker();

    private final String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private Random rand = new Random();

    public String getRandomMonth() {
        return months[rand.nextInt(months.length)];
    }

    private final String[] years = new String[]{"24", "25", "26", "27", "28"};
    private Random random = new Random();

    public String getRandomYear() {
        return years[random.nextInt(years.length)];
    }

    public String generateCardOwnerName() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    private final String[] cvv = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private Random randomCVV = new Random();

    public String getCVV() {
        return cvv[randomCVV.nextInt(cvv.length)] + cvv[randomCVV.nextInt(cvv.length)] + cvv[randomCVV.nextInt(cvv.length)];
    }
}