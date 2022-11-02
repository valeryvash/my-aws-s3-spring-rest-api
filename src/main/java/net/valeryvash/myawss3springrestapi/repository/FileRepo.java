package net.valeryvash.myawss3springrestapi.repository;

import net.valeryvash.myawss3springrestapi.model.File;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileRepo extends CrudRepository<File, Long> {

}