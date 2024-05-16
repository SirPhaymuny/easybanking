package co.istad.easybanking.api.specimen;

import lombok.Builder;

@Builder
public record FileUploadDto(
        String name,
        String uri,
        String urlToDownload,
        String extension,
        Long size,
        String fileType) {
}
