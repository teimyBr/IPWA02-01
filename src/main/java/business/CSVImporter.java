package business;

import jakarta.persistence.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CSVImporter {

    private final static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("likeHeroToZero");

    public static void main(String[] args) {
        String csvFile = "src/main/resources/data/emissions.csv";
        String line;
        String splitter = ";";

        Publisher publisher = findUserProfile("admin");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] headerline = br.readLine().split(splitter);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(splitter);

                for (int i = 1; i < data.length; i++) {
                    System.out.println("Country: " + data[0] + " Year: " + headerline[i] + " Value: " + data[i]);

                    String countryName = data[0];
                    String year = headerline[i];
                    String sValue = data[i];
                    double value = Double.parseDouble(sValue);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy", Locale.GERMAN);

                    String dateInString = "01-" + year;
                    Date date = formatter.parse(dateInString);

                    transaction.begin();

                    Country newCountry = entityManager.find(Country.class, countryName);
                    if (newCountry == null) {
                        newCountry = new Country(countryName);
                        entityManager.persist(newCountry);
                    }

                    Measurement newMeasurement = new Measurement(date, value, newCountry, publisher, State.APPROVED);
                    entityManager.persist(newMeasurement);
                    transaction.commit();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        entityManager.close();
    }

    public static Publisher findUserProfile(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Query q = em.createQuery("select a from Publisher a");
        List<Publisher> allPublishers = q.getResultList();
        em.close();

        for (Publisher p : allPublishers) {
            if (p.equalsUsername(username)) {
                return p;
            }
        }
        return null;
    }

}
