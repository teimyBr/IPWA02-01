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

import java.io.Serializable;
import java.util.List;

@Named("logInController")
@SessionScoped
public class LoginController implements Serializable {
    private String name;
    private String passcode;
    private boolean loginStatus = false;
    private boolean admin = false;
    private PublisherDAO publisherDAO = new PublisherDAO();

    public void postValidateName(ComponentSystemEvent ev) throws AbortProcessingException {
        UIInput temp = (UIInput) ev.getComponent();
        this.name = (String) temp.getValue();
    }

    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        List<Publisher> userList = publisherDAO.getPublishers();
        if (userList != null) {
            for (Publisher p : userList) {
                Publisher tmp = new Publisher(this.name, (String) value);
                if (p.equals(tmp)) {
                    this.admin = p.isAdmin();
                    return;
                }
            }
        }
        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzername und Passwort stimmen nicht überein!", ""));
    }

    public void login() {
        loginStatus = true;
        showLoginConfirm();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "profile.xhtml?faces-redirect=true&i=1");
    }

    public void logout() {
        loginStatus = false;
        this.admin = false;
        this.name = "";
        this.passcode = "";
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml?faces-redirect=true&i=0");
    }

    public void showLoginConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich eingeloggt!", "");
    }

    public void showLogOutConfirm() {
        addMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich ausgeloggt!", "");
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

    public void setPasscode(String password) {
        this.passcode = password;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public boolean isAdmin() {
        return admin;
    }
}
