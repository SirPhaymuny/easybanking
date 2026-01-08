package co.istad.easybanking.api.specimen;

import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.customer.CustomerRepository;
import co.istad.easybanking.api.customer.CustomerService;
import co.istad.easybanking.api.staff.Staff;
import co.istad.easybanking.api.staff.StaffRepository;
import lombok.RequiredArgsConstructor;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomerSpecimenServicesImpl implements CustomerSpecimenServices {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final SpecimenMapper specimenMapper;
    private final SpecimenRepository specimenRepository;
    @Value("${file-server-upload}")
    private String serverPath;
    @Value("${file-client}")
    private String clientPath;
    @Value("${file-client-uri}")
    private String uri;
    @Value("${url-force-download}")
    private String downloadUri;

    @SuppressWarnings("null")
    @Override
    public void uploadSingleFile(SpecimenUploadDto file) {
        try {
            Customer customer = customerRepository.findById(file.customerId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.CONFLICT, "Customer Id not found"));
            @SuppressWarnings("unused")
            Staff staff = staffRepository.findById(file.StaffId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.CONFLICT, "Staff Id not found"));
            CustomerSpecimen customerSpecimen = specimenMapper.fromCusToDto(file);
            String[] images = file.base64().split("\\|");
            for (String image : images) {
                String filename = customer.getFullName() + "-" + UUID.randomUUID().toString();
                decodeAndSave(image, serverPath, filename);
                customerSpecimen.setCreatedDate(LocalDate.now());
                customerSpecimen.setImageId(filename);
                specimenRepository.save(customerSpecimen);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Failed to create customer: " + Objects.requireNonNull(e.getRootCause()).getMessage());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }

    @Override
    public List<FileUploadDto> getList() {
        List<FileUploadDto> allFileInfo = new ArrayList<>();
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
        Path path = Paths.get(serverPath + name);
        FileUploadDto fileUpload;
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
                                Files.deleteIfExists(file);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public String imageConverter(MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }
    public void decodeAndSave(String base64, String path, String filename) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            Path outputDir = Paths.get(path);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }
            String extension = "jpg";
            filename = filename + "." + extension;
            Path outputFile = Paths.get(path, filename);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile.toFile()));
            outputStream.write(decodedBytes);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
        }
    }
}
