package com.example.dividendfund.persist.entity;

import com.example.dividendfund.model.Company;
import lombok.*;

import javax.persistence.*;

@Entity(name="COMPANY")
@Getter
@NoArgsConstructor
@ToString
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;
    private String name;

    public CompanyEntity (Company company){
        ticker = company.getTicker();
        name = company.getName();
    }

}
