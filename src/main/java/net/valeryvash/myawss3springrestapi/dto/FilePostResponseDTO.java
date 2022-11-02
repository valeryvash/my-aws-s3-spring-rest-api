package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valeryvash.myawss3springrestapi.model.File;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilePostResponseDTO implements Serializable {

    private Long id;

    private String fileName;

    public static FilePostResponseDTO fromFile(File file) {
        return new FilePostResponseDTO(file.getId(), file.getFileName());
    }

    public File fromDTO() {
        File result = new File();

        result.setId(this.id);
        result.setFileName(this.fileName);

        return result;
    }
}
