package com.zonecritique.zonecritique.controller;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.TypeDeMedia;
import com.zonecritique.zonecritique.repository.MediaRepository;
import com.zonecritique.zonecritique.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public ResponseEntity<List<Media>> getAllMedia() {
        List<Media> allMedias = mediaService.getAllMedia();
        if (allMedias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(allMedias);
    }
    
    @GetMapping("type/{type}")
    public ResponseEntity<List<Media>> getMediaByType(@PathVariable TypeDeMedia type) {
        return ResponseEntity.ok(mediaService.getMediaByType(type));
    }

    @GetMapping("du-moment/{type}")
    public ResponseEntity<List<Media>> getMediaByTypeDuMoment(@PathVariable TypeDeMedia type) {
        return ResponseEntity.ok(mediaService.getMediasDuMoment(type));
    }

    @GetMapping("prochainement/{type}")
    public ResponseEntity<List<Media>> getMediaByTypeProchainement(@PathVariable TypeDeMedia type) {
        return ResponseEntity.ok(mediaService.getMediasByTypeProchainement(type));
    }

    @GetMapping("meilleur-note/{type}")
    public ResponseEntity<List<Media>> getMediaByTypeMeilleurNote(@PathVariable TypeDeMedia type) {
        return ResponseEntity.ok(mediaService.getMediaByMeilleurNoteGeneral(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        try {
            Media media = mediaService.getMediaById(id).orElseThrow(() -> new RuntimeException("Media not found"));
            return new ResponseEntity<>(media, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titre") String titre,
            @RequestParam("date") LocalDate date,
            @RequestParam("resume") String resume,
            @RequestParam("type") TypeDeMedia type) {

        try {
            // Upload de l'image et récupération du chemin de l'image
            String imagePath = mediaService.uploadImage(file);

            // Création du média avec les informations
            Media media = new Media();
            media.setTitre(titre);
            media.setDate(date);
            media.setResume(resume);
            media.setType(type);
            media.setImage(imagePath); // L'image est bien ajoutée

            // Sauvegarde du média
            Media savedMedia = mediaService.createMedia(media);

            return new ResponseEntity<>(savedMedia, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(
            @PathVariable Long id,
            @RequestParam("titre") String titre,
            @RequestParam("date") LocalDate date,
            @RequestParam("resume") String resume,
            @RequestParam("type") TypeDeMedia type,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "image", required = false) String existingImage) {
        try {
            Media updatedMedia = mediaService.updateMedia(id, titre, date, resume, type, file, existingImage);
            return new ResponseEntity<>(updatedMedia, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long id) {
        Optional<Media> mediaOptional = mediaRepository.findById(id);

        if (mediaOptional.isPresent()) {
            Media media = mediaOptional.get();

            // Construire le chemin absolu du fichier
            String filePath = "C:/Users/gad_7/IdeaProjects/zoneCritique/" + media.getImage();

            // Supprimer le fichier si présent
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Fichier supprimé : " + filePath);
                } else {
                    System.out.println("Échec de la suppression du fichier !");
                }
            } else {
                System.out.println("Fichier non trouvé : " + filePath);
            }

            // Supprimer l'entrée en base de données
            mediaRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
            Path filepath = Paths.get(uploadDir.getAbsolutePath(), fileName);

            Files.write(filepath, file.getBytes());

            return ResponseEntity.ok(filepath.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'upload de l'image");
        }
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // ou un autre type d'image selon le format
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
