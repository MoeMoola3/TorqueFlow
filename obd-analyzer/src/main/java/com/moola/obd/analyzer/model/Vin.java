package com.moola.obd.analyzer.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Data
public class Vin {

    @Id
    private String vin;

    @JsonIgnore
    @OneToMany(mappedBy = "vin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ObdData> obdDataRecords;
}
