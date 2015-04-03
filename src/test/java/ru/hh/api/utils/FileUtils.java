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
  public final String readFile(String filePath) throws
      IOException, URISyntaxException {

    URI fileLocation = this.getClass().getClassLoader()
        .getResource(filePath)
        .toURI();

    return new String(java.nio.file.Files.readAllBytes(
        Paths.get(fileLocation)));
  }
}
