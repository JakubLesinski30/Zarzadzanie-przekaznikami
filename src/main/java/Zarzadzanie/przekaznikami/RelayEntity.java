package Zarzadzanie.przekaznikami;

import jakarta.persistence.*;

@Entity
@Table(name = "relays")
public class RelayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "relay_number", nullable = false, unique = true)
    private Integer relayNumber;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    // 0 = OFF, 1 = ON
    @Column(name = "state")
    private Integer state;

    @Column(name = "location", length = 100)
    private String location;

    // Gettery i settery
    public Long getId() {
        return id;
    }
    
    public Integer getRelayNumber() {
        return relayNumber;
    }

    public void setRelayNumber(Integer relayNumber) {
        this.relayNumber = relayNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }
    
    public void setState(Integer state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
