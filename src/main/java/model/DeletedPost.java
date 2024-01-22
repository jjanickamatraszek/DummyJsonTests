package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeletedPost extends Post {

    @JsonProperty(value = "isDeleted")
    private boolean isDeleted;
    private String deletedOn;
}
