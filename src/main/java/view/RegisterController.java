package view;

import business.Publisher;
import business.PublisherDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.List;

@Named("registerController")
@SessionScoped
public class RegisterController implements Serializable {

    private String name;
    private String passcode;
    private PublisherDAO publisherDAO = new PublisherDAO();

    private final static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("likeHeroToZero");

    public void postValidateName(ComponentSystemEvent ev) throws AbortProcessingException {
        UIInput temp = (UIInput) ev.getComponent();
        this.name = (String) temp.getValue();
    }

    public void validateRegister(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        List<Publisher> userList = publisherDAO.getPublishers();
        if (userList != null) {
            for (Publisher p : userList) {
                Publisher tmp = new Publisher(this.name, (String) value);
                if (p.equalsUsername(tmp.getName())) {
                    addMessage(FacesMessage.SEVERITY_WARN, "Der Benutzername " + this.name + " ist bereits vergeben!","");
                }
            }
            this.passcode = (String) value;
        }
    }

    public void showRegisterConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich Registriert! Du kannst Dich nun einloggen.", "");
    }

    public void register() {
        Publisher newPublisher = new Publisher(this.name, passcode);
        if (this.name.toLowerCase().contains("admin")) {
            newPublisher.setAdmin(true);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(newPublisher);
        transaction.commit();
        entityManager.close();

        showRegisterConfirm();

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "login.xhtml?faces-redirect=true&i=1");

    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
