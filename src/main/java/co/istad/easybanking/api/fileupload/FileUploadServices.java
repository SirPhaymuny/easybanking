package co.istad.easybanking.api.fileupload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadServices {
    FileUploadDto uploadSingleFile(MultipartFile file);
    List<FileUploadDto> uploadMultiFile(List<MultipartFile> file);
    List<FileUploadDto> getList();
    FileUploadDto findByName(String name);
    void deleteFileByName(String name);
    void deleteAllFile();
}
