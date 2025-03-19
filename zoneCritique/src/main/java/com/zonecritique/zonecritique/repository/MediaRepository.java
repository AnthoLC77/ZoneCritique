package com.zonecritique.zonecritique.repository;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.TypeDeMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    // Filtrer par type
    List<Media>findByType(TypeDeMedia typeDeMedia);
    List<Media> id(Long id);
    Optional<Media> findByTitreAndType(String titre, TypeDeMedia type);

    // Les prochaines sorties
    @Query("SELECT m FROM Media m WHERE m.type = :type AND m.date > CURRENT_DATE ORDER BY m.date ASC LIMIT 10")
    List<Media> findTop10UpcomingByType(TypeDeMedia type);

    // Les medias du moments
    @Query("SELECT m FROM Media m WHERE m.type = :type AND m.date <= CURRENT_DATE ORDER BY m.date DESC LIMIT 10")
    List<Media> findByTypeOrderByDateDesc(TypeDeMedia type);

    List<Media> findByTypeOrderByNoteGeneralDesc(TypeDeMedia type);
}
