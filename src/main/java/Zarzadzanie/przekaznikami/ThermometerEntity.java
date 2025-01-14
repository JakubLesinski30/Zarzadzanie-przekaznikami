package Zarzadzanie.przekaznikami;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
@Table(name = "thermometers")
public class ThermometerEntity {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    private String name;
    private String description;
    private String location;

    // np. offset do kalibracji
    // 0.0 oznacza brak korekty,
    // +0.5 oznacza, że odczyt z API jest o 0.5 za niski i zwiększamy,
    // -0.5 odwrotnie
    private double offset;

    // Możesz też trzymać ostatnio zapisaną temperaturę w bazie:
    private Double lastMeasurement;

    // get/set ...
    
    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public Double getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(Double lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

    // reszta pól, get/set
}