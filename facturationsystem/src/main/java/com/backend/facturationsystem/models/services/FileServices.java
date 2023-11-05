package com.backend.facturationsystem.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface FileServices {
    public String  copy(MultipartFile image) throws IOException;
    public Resource getImageResource(String clientImageName) throws MalformedURLException;
    public void deleteImageResource(String clientImageName);
    public Path getPath(String clientImageName);
}
