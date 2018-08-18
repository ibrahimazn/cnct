/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.springframework.web.multipart.MultipartFile;
import az.ancode.filemanager.connector.fileset.CsvFileResponse;
import az.ancode.filemanager.connector.fileset.FileListResponse;
import az.ancode.filemanager.connector.upload.FileUploadResponse;

/**
 * The Interface FilemanagerService.
 */
public interface FilemanagerService {

  /**
   * List of files.
   *
   * @param node the node
   * @param podUuid the pod uuid
   * @param pvc the pvc
   * @param path the path
   * @return the file list response
   * @throws Exception the exception
   */
  FileListResponse listOfFiles(String node, String podUuid, String pvc, String path) throws Exception;

  /**
   * Upload file.
   *
   * @param node the node
   * @param podUuid the pod uuid
   * @param pvc the pvc
   * @param path the path
   * @param file the file
   * @return the file upload response
   * @throws Exception the exception
   */
  FileUploadResponse uploadFile(String node, String podUuid, String pvc, String path, MultipartFile file)
      throws Exception;

  /**
   * Get csv file from FileManager.
   *
   * @param userId the userId.
   * @param fileName csv fileName.
   * @param start the starting range.
   * @param end the ending range.
   * @return the csv file.
   * @throws Exception if error occurs.
   */
  CsvFileResponse getCsvFile(Long userId, String fileName, String start, String end) throws Exception;

}
