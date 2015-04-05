package ru.hh.api.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * General file util
 */
public final class FileUtils {
  /**
   * @param filePath Path to a given file to read
   * @return Contents of a given file
   * @throws IOException
   * @throws URISyntaxException
   */
  public final String readFile(String filePath) throws RuntimeException {

    URI fileLocation = null;
    try {
      fileLocation = this.getClass().getClassLoader()
          .getResource(filePath)
          .toURI();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e.getMessage());
    }

    try {
      return new String(java.nio.file.Files.readAllBytes(
          Paths.get(fileLocation)));
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
