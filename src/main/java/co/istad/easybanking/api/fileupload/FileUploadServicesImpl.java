package co.istad.easybanking.api.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileUploadServicesImpl implements FileUploadServices {

    @Value("${file-server-upload}")
    private String serverPath;
    @Value("${file-client}")
    private String clientPath;
    @Value("${file-client-uri}")
    private String uri;
    @Value("${url-force-download}")
    private String downloadUri;

    @Override
    public FileUploadDto uploadSingleFile(MultipartFile file) {
        String newFileName = uploadFile(file);
        return FileUploadDto.builder()
                .fileType(file.getContentType())
                .extension(file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1))
                .uri(uri + newFileName)
                .urlToDownload(downloadUri + newFileName)
                .size(file.getSize())
                .build();
    }

    @Override
    public List<FileUploadDto> uploadMultiFile(List<MultipartFile> files) {
        List<FileUploadDto> fileUploadDtos = new ArrayList<>();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                String filename = uploadFile(file);
                fileUploadDtos.add(FileUploadDto.builder()
                        .size(file.getSize())
                        .fileType(file.getContentType())
                        .extension(file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1))
                        .name(filename)
                        .uri(uri + filename)
                        .urlToDownload(downloadUri + filename)
                        .build());
            }
        }
        System.out.println(fileUploadDtos);
        return fileUploadDtos;
    }

    @Override
    public List<FileUploadDto> getList() {
        List<FileUploadDto> allFileInfo = new ArrayList<>();
        File files = new File(serverPath);
        Path path = Paths.get(serverPath);
        try {
            if (Files.exists(path)) {
                Stream<Path> pathStream = Files.list(Paths.get(serverPath));
                allFileInfo = pathStream.map(e -> {
                    try {
                        return FileUploadDto.builder()
                                .fileType(Files.probeContentType(e))
                                .size(Files.size(e))
                                .name(e.getFileName().toString())
                                .extension(Files.probeContentType(e)
                                        .substring(Files.probeContentType(e).lastIndexOf("/") + 1))
                                .uri(uri + e.getFileName().toString())
                                .urlToDownload(downloadUri + e.getFileName().toString())
                                .build();
                    } catch (IOException et) {
                        throw new RuntimeException(et);
                    }
                }).collect(Collectors.toList());
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return allFileInfo;
    }

    @Override
    public FileUploadDto findByName(String name) {
        FileUploadDto fileUpload = null;
        Path path = Paths.get(serverPath + name);
        try {
            if (!Files.exists(path)) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "File not founded");
            } else {
                fileUpload = FileUploadDto.builder()
                        .name(name)
                        .fileType(Files.probeContentType(path))
                        .extension(Files.probeContentType(path)
                                .substring((Files.probeContentType(path).lastIndexOf("/") + 1)))
                        .size(Files.size(path))
                        .uri(uri + name)
                        .build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileUpload;
    }

    @Override
    public void deleteFileByName(String name) {
        Path path = Paths.get(serverPath + name);
        try {
            if (!Files.exists(path)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file not found");
            } else {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllFile() {
        Path path = Paths.get(serverPath);
        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder not found");
        } else {
            try {
                if (Files.list(path).findAny().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file in this folder");
                }
                Files.list(path).forEach(
                        file -> {
                            try {
                                System.out.println(file);
                                Files.delete(file);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String uploadFile(MultipartFile file) {
        String newFileName = UUID.randomUUID().toString();
        int lastIndex = Objects.requireNonNull(file.getContentType()).lastIndexOf("/");
        String extension = file.getContentType().substring(lastIndex + 1);
        newFileName = newFileName + "." + extension;

        Path path = Paths.get(serverPath + newFileName);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newFileName;
    }
}
