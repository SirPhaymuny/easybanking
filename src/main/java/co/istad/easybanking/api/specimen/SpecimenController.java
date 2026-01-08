package co.istad.easybanking.api.specimen;

import co.istad.easybanking.base.BaseSuccess;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/specimen")
@RequiredArgsConstructor
public class SpecimenController {

    private final CustomerSpecimenServices customerSpecimenServices;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/converter")
    BaseSuccess<?> imageConverter(@RequestPart MultipartFile file) throws IOException {
        String base64 = customerSpecimenServices.imageConverter(file);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully Created")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(base64)
                .build();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/uploadImage")
    BaseSuccess<?> uploadImage(@RequestBody SpecimenUploadDto file) throws IOException {
        customerSpecimenServices.uploadSingleFile(file);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully Created")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data("")
                .build();
    }
    @GetMapping("/getList")
    BaseSuccess<?> getFileList() {
        List<FileUploadDto> fileUploadDto = customerSpecimenServices.getList();
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

        FileUploadDto foundFile = customerSpecimenServices.findByName(name);
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
        customerSpecimenServices.deleteFileByName(name);
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
        customerSpecimenServices.deleteAllFile();
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully Deleted file")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
    }

}
