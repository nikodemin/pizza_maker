package com.t_systems.webstore.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@PropertySource("classpath:server.properties")
public class FilesService {

    @Value("${server.uploadDir}")
    private String UPLOAD_DIR;

    /**
     * save files to uploads
     * @param files CommonsMultipartFile array
     * @return list of paths to files
     * @throws IOException ex
     */
    public List<String> saveUploadedFiles(CommonsMultipartFile[] files) throws IOException {
        log.trace("Saving uploaded files");
        // Make sure directory exists!
        File uploadDir = new File(UPLOAD_DIR);
        uploadDir.mkdirs();
        List<String> paths = new ArrayList<>();

        for (CommonsMultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }
            String fileName = (new Date()).getTime() + "_" + file.getOriginalFilename();
            String uploadFilePath = UPLOAD_DIR + "/" + fileName;

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
            paths.add("/uploads/" + fileName);
        }
        return paths;
    }
}
