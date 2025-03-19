package com.zonecritique.zonecritique.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medias", uniqueConstraints = @UniqueConstraint(columnNames = {"titre", "type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titre;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 1000, nullable = false)
    private String resume;

    @Enumerated(EnumType.STRING)
    private TypeDeMedia type;

    private Double noteGeneral;

    private Double NoteAmis;
}
