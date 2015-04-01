package ru.hh.api.utils;

import org.testng.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Created by n.miroshnichenko <nikbox@ya.ru> on 01.04.15 9:00.
 */
public final class Files {
  public final String readFile(String filePath) {
    URI fileLocation = null;
    try {
      fileLocation = this.getClass().getClassLoader()
          .getResource(filePath)
          .toURI();
    } catch (URISyntaxException e) {
      Assert.fail("Syntax error in the file path", e);
    }

    String content = null;
    try {
      content = new String(java.nio.file.Files.readAllBytes(
          Paths.get(fileLocation)));
    } catch (IOException e) {
      Assert.fail("Could not read the file", e);
    }
    return content;
  }
}
