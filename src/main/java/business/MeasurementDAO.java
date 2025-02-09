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
@Named("messorganizer")
public class MeasurementDAO{

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("likeHeroToZero");

    public List<Measurement> findAll() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select a from Measurement a ORDER BY year desc");
        List<Measurement> allMeasurements = q.getResultList();
        em.close();
        return allMeasurements;
    }

    public List<Measurement> findAllApproved() {
        List<Measurement> findApproved = new ArrayList<>();
        for (Measurement m : findAll()) {
            if (m.getState() == State.APPROVED) {
                findApproved.add(m);
            }
        }
        return findApproved;
    }

    public List<Measurement> findAllPending() {
        List<Measurement> findPending = new ArrayList<>();
        for (Measurement m : findAll()) {
            if (m.getState() == State.PENDING) {
                findPending.add(m);
            }
        }
        return findPending;
    }

    public List<Measurement> findByCountry(String country) {
        List<Measurement> measurementsByCountry = new ArrayList<Measurement>();
        for (Measurement m : findAll()) {
            if (m.getCountry().equals(country)) {
                measurementsByCountry.add(m);
            }
        }
        return measurementsByCountry;
    }

    public List<Measurement> findByPublisher(String publisher) {
        List<Measurement> measurementsByPublisher = new ArrayList<Measurement>();
        for (Measurement m : findAll()) {
            if (m.getPublisher().equalsUsername(publisher)) {
                measurementsByPublisher.add(m);
            }
        }
        return measurementsByPublisher;
    }
}



