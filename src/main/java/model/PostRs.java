package model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostRs {
    private String id;
    private String title;
    private String body;
    private String userId;
    private List<String> tags;
    private int reactions;
}
