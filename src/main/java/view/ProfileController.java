package view;

import business.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Named("profileController")
@SessionScoped
public class ProfileController implements Serializable {
    private String username;
    private Date year;
    private String country;
    private double value;

    private final static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("likeHeroToZero");

    public void addMeasurement(String username) {

        if (year.after(new Date(System.currentTimeMillis()))) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Du kannst nur Messwerte für die Vergangenheit hinzufügen!", "");
            return;
        }
        if (year.before(new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime())) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Du kannst keine Messwerte vor 1990 hinzufügen!", "");
            return;
        }

        Publisher publisher = findUserProfile(username);
        if (publisher == null) {
            return;
        }

        this.username = username;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Country newCountry = entityManager.find(Country.class, country);

        if (newCountry == null) {
            newCountry = new Country(country);
            entityManager.persist(newCountry);
        }

        Measurement m = new Measurement(year, value, newCountry, publisher);

        entityManager.persist(m);
        transaction.commit();
        entityManager.close();

        showAddedMeasurmentConfirm();
    }

    public void deleteMeasurement(int id) {
        Measurement deleteMeasurement = findMeasurementById(id);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.remove(entityManager.contains(deleteMeasurement) ? deleteMeasurement : entityManager.merge(deleteMeasurement));

        transaction.commit();
        entityManager.close();

        showDeleteMeasurmentConfirm();
    }

    public void approveMeasurementState(int id) {
        updateMeasurementState(id, State.APPROVED);
    }

    public void disapproveMeasurementState(int id) {
        updateMeasurementState(id, State.DISAPPROVED);
    }

    public void updateMeasurementState(int id, State newstate) {
        Measurement updateMeasurement = findMeasurementById(id);
        if (updateMeasurement != null) {
            updateMeasurement.setState(newstate);

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(updateMeasurement);
            transaction.commit();
            entityManager.close();

            if (newstate == State.APPROVED) {
                showApproveConfirm();
            } else if (newstate == State.DISAPPROVED) {
                showDisapproveConfirm();
            }

        }
    }

    public Publisher findUserProfile(String username) {
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

    public Measurement findMeasurementById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Query q = em.createQuery("select a from Measurement a");
        List<Measurement> allMeasurements = q.getResultList();
        em.close();
        for (Measurement m : allMeasurements) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public String stateColRowStyle(Measurement measurement) {
        if (measurement.getState() == State.APPROVED) return "app";
        if (measurement.getState() == State.PENDING) return "pen";
        if (measurement.getState() == State.DISAPPROVED) return "dis";
        return null;
    }

    public void showApproveConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Datensatz bestätigt!", "");
    }

    public void showDisapproveConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Datensatz verworfen!", "");
    }

    public void showAddedMeasurmentConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Datensatz hinzugefügt!", "");
    }

    public void showDeleteMeasurmentConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Datensatz gelöscht!", "");
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getUsername() {
        return username;
    }

    public Date getYear() {
        return year;
    }

    public int getYearFromDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public void setUsername(String username) {
        this.username = username;
    }

}