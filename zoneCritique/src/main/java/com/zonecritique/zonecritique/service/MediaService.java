package com.zonecritique.zonecritique.service;

import com.zonecritique.zonecritique.entity.Media;
import com.zonecritique.zonecritique.entity.TypeDeMedia;
import com.zonecritique.zonecritique.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private static final String UPLOAD_DIR = "uploads/";
    private static final Logger LOGGER = Logger.getLogger(MediaService.class.getName());

    public String uploadImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        // Vérifier si le dossier de stockage existe, sinon le créer
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Générer un nom de fichier unique pour éviter les conflits
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir.getAbsolutePath(), fileName);

        // Écrire le fichier dans le répertoire
        Files.write(filepath, file.getBytes());

        // Retourner l'URL de l'image
        return "/uploads/" + fileName; // Assurer que c'est bien l'URL attendue
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public List<Media> getMediaByType(TypeDeMedia typeDeMedia) {
        return mediaRepository.findByType(typeDeMedia);
    }

    public List<Media>getMediasDuMoment(TypeDeMedia type) {
        return mediaRepository.findByTypeOrderByDateDesc(type);
    }

    public List<Media>getMediasByTypeProchainement(TypeDeMedia type) {
        return mediaRepository.findTop10UpcomingByType(type);
    }

    public List<Media>getMediaByMeilleurNoteGeneral(TypeDeMedia type) {
        return mediaRepository.findByTypeOrderByNoteGeneralDesc(type);
    }

    public Optional<Media> getMediaById(Long id) {
        Optional<Media> media = mediaRepository.findById(id);
        if (media.isPresent()) {
            return media;
        } else {
          throw new RuntimeException("media non trouvé avec id " + id);
        }
    }

    public Media createMedia(Media media) {
        if(media.getTitre() == null || media.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du média est obligatoire");
        }

        if (media.getType() == null) {
            throw new IllegalArgumentException("Le type du média est obligatoire");
        }

        Optional<Media> mediaOptional = mediaRepository.findByTitreAndType(media.getTitre(), media.getType());
        if (mediaOptional.isPresent()) {
            throw new IllegalArgumentException("Un média avec ce titre et ce type existe déjà");
        }

        return mediaRepository.save(media);
    }

//    public Media updateMedia(Long id, Media media, MultipartFile file) throws IOException {
//        Optional<Media> optionalMedia = mediaRepository.findById(id);
//        if (optionalMedia.isPresent()) {
//            Media existingMedia = optionalMedia.get();
//
//            if (file != null && !file.isEmpty()) {
//                if (existingMedia.getImage() != null) {
//                    deleteFile(existingMedia.getImage());
//                }
//
//                String imagePath = saveFile(file);
//                existingMedia.setImage(imagePath);
//            }
//            existingMedia.setTitre(media.getTitre());
//            existingMedia.setDate(media.getDate());
//            existingMedia.setResume(media.getResume());
//            existingMedia.setType(media.getType());
//
//            return mediaRepository.save(existingMedia);
//
//        } else {
//            throw new NoSuchElementException("Media non trouvé avec l'ID " + id);
//        }
//    }

//    public Media updateMedia(Long id, String titre, LocalDate date, String resume, TypeDeMedia type, MultipartFile file) throws IOException {
//        Optional<Media> optionalMedia = mediaRepository.findById(id);
//
//        if (optionalMedia.isPresent()) {
//            Media existingMedia = optionalMedia.get();
//
//            LOGGER.info("Mise à jour du média avec l'ID : " + id);
//            LOGGER.info("Nouveau titre : " + titre);
//            LOGGER.info("Nouvelle date : " + date);
//            LOGGER.info("Nouveau résumé : " + resume);
//            LOGGER.info("Nouveau type : " + type);
//            LOGGER.info("Ancienne image : " + existingMedia.getImage());// Récupération de l'existant
//
//            // Mise à jour des champs
//            existingMedia.setTitre(titre);
//            existingMedia.setDate(date);
//            existingMedia.setResume(resume);
//            existingMedia.setType(type);
//
//            // Gestion de l'image : seulement si un nouveau fichier est envoyé
//            if (file != null && !file.isEmpty()) {
//                LOGGER.info("Nouvelle image détectée, mise à jour de l'image...");
//                if (existingMedia.getImage() != null) {
//                    deleteFile(existingMedia.getImage()); // Supprime l'ancienne image
//                }
//                String imagePath = saveFile(file); // Sauvegarde de la nouvelle image
//                existingMedia.setImage(imagePath);
//            } else {
//                LOGGER.info("Aucune nouvelle image fournie, conservation de l'image existante.");
//            }
//
//            return mediaRepository.save(existingMedia);
//        } else {
//            throw new NoSuchElementException("Media non trouvé avec l'ID " + id);
//        }
//    }

    public Media updateMedia(Long id, String titre, LocalDate date, String resume, TypeDeMedia type, MultipartFile file, String existingImage) throws IOException {
        Optional<Media> optionalMedia = mediaRepository.findById(id);

        if (optionalMedia.isPresent()) {
            Media existingMedia = optionalMedia.get();

            existingMedia.setTitre(titre);
            existingMedia.setDate(date);
            existingMedia.setResume(resume);
            existingMedia.setType(type);

            // Gestion de l'image
            if (file != null && !file.isEmpty()) {
                LOGGER.info("Nouvelle image détectée, mise à jour...");
                if (existingMedia.getImage() != null) {
                    deleteFile(existingMedia.getImage());
                }
                String imagePath = saveFile(file);
                existingMedia.setImage(imagePath);
            } else {
                LOGGER.info("Aucune nouvelle image fournie, conservation de l'image existante.");
                existingMedia.setImage(existingImage); // Conserve l'image actuelle
            }

            return mediaRepository.save(existingMedia);
        } else {
            throw new NoSuchElementException("Media non trouvé avec l'ID " + id);
        }
    }


    public void deleteMedia(Long id) throws IOException {
        Optional<Media> optionalMedia = mediaRepository.findById(id);
        if (optionalMedia.isPresent()) {
            Media media = optionalMedia.get();
            if (media.getImage() != null) {
                deleteFile(media.getImage());
            }
            mediaRepository.delete(media);
        } else {
            throw new NoSuchElementException("Media not found with id " + id);
        }
    }



private String saveFile(MultipartFile file) throws IOException {
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists()) {
        uploadDir.mkdir();
    }

    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    Files.write(filePath, file.getBytes());

    // Retourner une URL relative pour l'image
    return "/uploads/" + fileName;  // Correspond à la route que tu as définie
}

    // 📌 Supprimer un fichier image
//    private void deleteFile(String imagePath) throws IOException {
//        Path path = Paths.get(imagePath);
//        Files.deleteIfExists(path);  // Supprimer le fichier s'il existe
//    }

    private void deleteFile(String imagePath) throws IOException {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        // Construire le chemin absolu correct
        Path path = Paths.get(UPLOAD_DIR).resolve(imagePath.replace("/uploads/", ""));
        File file = path.toFile();

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Fichier supprimé : " + path);
            } else {
                System.out.println("Échec de la suppression du fichier : " + path);
            }
        } else {
            System.out.println("Fichier non trouvé : " + path);
        }
    }


}
