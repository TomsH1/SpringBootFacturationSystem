package com.backend.facturationsystem.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServicesImp implements FileServices{

    private final Logger logger = LoggerFactory.getLogger(FileServicesImp.class);
    private final static String UPLOAD_DIRECTORY = "uploads";
    @Override
    public String copy(MultipartFile image) throws IOException {
        // crear un formato único de imagen
        String nameImage = UUID.randomUUID()+"_"+ Objects.requireNonNull(image.getOriginalFilename())
                .replace(" ", "");
        Path uploadsPath = getPath(nameImage);

        // copiar la imagen en el directorio uploads
        Files.copy(image.getInputStream(), uploadsPath);
        return nameImage;
    }

    @Override
    public Resource getImageResource(String clientImageName) throws MalformedURLException {
        //Obtener la ruta de imagen del cliente
        Path uploadsPath = getPath(clientImageName);
        logger.info(uploadsPath.toString());
        //convertir en recurso de URL
        Resource resource = new UrlResource(uploadsPath.toUri());
        //Sí la imagen no existe devuelve una imagen por defecto
        if(!resource.exists() && !resource.isReadable()) {
            Path defaultImagePath = Paths.get("src/main/resources/static/img/")
                    .resolve("defualt-user-img.jpeg").toAbsolutePath();
            resource = new UrlResource(defaultImagePath.toUri());
        }
        return resource;
    }

    @Override
    // Eliminar la imagen del cliente del directorio
    public void deleteImageResource(String clientImageName) {
        if(clientImageName != null){
            Path uploadsPath = getPath(clientImageName);
            File clientImageFile = uploadsPath.toFile();
            if(clientImageFile.exists() && clientImageFile.canRead()){ //* existe una imagen en la URI
                clientImageFile.delete();
            }
        }
    }

    @Override
    //Obtener la ruta completa del directorio uploads con el nombre de imagen
    public Path getPath(String clientImageName) {
        return Paths.get(UPLOAD_DIRECTORY).resolve(clientImageName).toAbsolutePath();
    }
}
