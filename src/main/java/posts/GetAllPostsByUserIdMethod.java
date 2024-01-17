package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.PropertiesPath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import lombok.NoArgsConstructor;

@Endpoint(url = "${config.env.api_url}/posts/user/${id}", methodType = HttpMethodType.GET)
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
@PropertiesPath(path = "api/posts/post.properties")
@NoArgsConstructor
public class GetAllPostsByUserIdMethod extends AbstractApiMethodV2 {

    public GetAllPostsByUserIdMethod(String userId) {
        replaceUrlPlaceholder("id", userId);
    }
}
