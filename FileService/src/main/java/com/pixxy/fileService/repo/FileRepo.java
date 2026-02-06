package com.pixxy.fileService.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixxy.fileService.model.FileMetaData;

@Repository
public interface FileRepo extends JpaRepository<FileMetaData, Long>{

	List<FileMetaData> findAllByOwnerId(Long id);


}
