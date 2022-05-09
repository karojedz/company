package com.example.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityRegistered;
    private String streetRegistered;
    private String houseNrRegistered;
    private String flatNrRegistered;

    private String cityResidence;
    private String streetResidence;
    private String houseNrResidence;
    private String flatNrResidence;

    @OneToOne(mappedBy = "address")
    private Person person;
}
