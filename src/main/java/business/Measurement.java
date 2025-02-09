package business;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Measurement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date year;
    private double value;
    @ManyToOne
    private Country country;
    @ManyToOne
    private Publisher publisher;
    @Enumerated(EnumType.STRING)
    private State state;

    public Measurement(Date year, double value, Country country, Publisher publisher) {
        this.year = year;
        this.value = value;
        this.country = country;
        this.publisher = publisher;
        this.state = State.PENDING;
    }

    public Measurement(Date year, double value, Country country, Publisher publisher, State state) {
        this.year = year;
        this.value = value;
        this.country = country;
        this.publisher = publisher;
        this.state = state;
    }

    public Measurement() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + country.getName() + ", " + year + ", " + value + ", " + publisher.getName() + ")";
    }
}
