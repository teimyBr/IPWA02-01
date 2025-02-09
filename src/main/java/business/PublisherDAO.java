package business;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.List;

@RequestScoped
@Named("publisherorganizer")
public class PublisherDAO{

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("likeHeroToZero");

    public List<Publisher> getPublishers() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select a from Publisher a");
        List<Publisher> publishers = q.getResultList();
        return publishers;
    }
}
