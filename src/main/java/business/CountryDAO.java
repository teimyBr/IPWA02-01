package business;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named("countryorganizer")
public class CountryDAO{

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("likeHeroToZero");

    public List<Country> findAll() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select distinct a from Country a");
        List<Country> allCountries = q.getResultList();
        em.close();
        return allCountries;
    }

    public boolean isCountryAlreadyExisting(String country) {
        List<Country> countriesFound = new ArrayList<>();
        for (Country c : findAll()) {
            if (c.getName().equals(new Country(country).getName())) {
                return true;
            }
        }
        return false;
    }
}
