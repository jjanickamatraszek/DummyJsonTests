package model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetAllPostsRs {
    private List<PostRs> posts;
    private int total;
    private int skip;
    private int limit;
}
