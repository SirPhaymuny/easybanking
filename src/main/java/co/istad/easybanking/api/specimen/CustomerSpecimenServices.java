package co.istad.easybanking.api.specimen;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerSpecimenServices {
    void uploadSingleFile(SpecimenUploadDto file);
    List<FileUploadDto> getList();
    FileUploadDto findByName(String name);
    void deleteFileByName(String name);
    void deleteAllFile();
    String imageConverter(MultipartFile file) throws IOException;
}
