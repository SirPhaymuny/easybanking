package co.istad.easybanking.api.fileupload;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.istad.easybanking.base.BaseSuccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fileUpload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadServices fileUpload;
    @Value("${file-server-upload}")
    private String serverPath;

    @PostMapping("/singleFile")
    BaseSuccess<?> uploadSingleFile(@RequestPart MultipartFile file) {
        FileUploadDto fileUploadDto = fileUpload.uploadSingleFile(file);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(fileUploadDto)
                .build();
    }

    @PostMapping("/multipleFile")
    BaseSuccess<?> uploadMultiFile(@RequestPart List<MultipartFile> files) {
        List<FileUploadDto> fileUploadDto = fileUpload.uploadMultiFile(files);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(fileUploadDto)
                .build();
    }

    @GetMapping("/files")
    BaseSuccess<?> getFileList() {
        List<FileUploadDto> fileUploadDto = fileUpload.getList();
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(fileUploadDto)
                .build();
    }

    @PostMapping("/{name}")
    BaseSuccess<?> findByName(@PathVariable String name) {

        FileUploadDto foundFile = fileUpload.findByName(name);
        System.out.println(foundFile);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully found file")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(foundFile)
                .build();
    }

    @DeleteMapping("/{name}")
    BaseSuccess<?> deleteByName(@PathVariable String name) {
        fileUpload.deleteFileByName(name);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully Deleted file")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
    }

    @DeleteMapping("/DeleteAll")
    BaseSuccess<?> deleteAll() {
        fileUpload.deleteAllFile();
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully Deleted file")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
    }

    @GetMapping("/download/{name}")
    ResponseEntity<ByteArrayResource> forceDownload(@PathVariable(name = "name") String name) {
        System.out.println("name");
        Path path = Paths.get(serverPath + name);
        try {
            byte[] fileData = Files.readAllBytes(path);

            //Create an InputStreamResource with the file data
            ByteArrayResource resource = new ByteArrayResource(fileData);

            //Build response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);

            //Set content type based on your file type
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Change to appropriate media type

            //Return ResponseEntity with InputStreamResource and headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
