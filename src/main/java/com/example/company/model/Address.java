package com.example.company.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode
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
