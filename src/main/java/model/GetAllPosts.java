package model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetAllPosts {
    private List<Post> posts;
    private int total;
    private int skip;
    private int limit;
}
