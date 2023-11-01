package coffeemeet.server.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

  private static final String MULTIPART_FILE_TRANSFER_ERROR = "MULTIPART FILE을 FILE로 변환 중 오류가 발생했습니다.";
  private static final String FILE_DELETE_ERROR = "FILE 삭제 중 오류가 발생했습니다.";

  public static File convertMultipartFileToFile(MultipartFile multipartFile) {
    try {
      File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
      multipartFile.transferTo(file);
      return file;
    } catch (IOException e) {
      throw new FileIOException(MULTIPART_FILE_TRANSFER_ERROR, e);
    }
  }

  public static void delete(File file) {
    try {
      Files.delete(file.toPath());
    } catch (IOException e) {
      throw new FileIOException(FILE_DELETE_ERROR, e);
    }
  }

  public static class FileIOException extends RuntimeException {

    public FileIOException(String message, Throwable e) {
      super(message, e);
    }

  }

}
