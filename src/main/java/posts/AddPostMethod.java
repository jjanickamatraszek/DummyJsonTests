package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.*;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/posts/add", methodType = HttpMethodType.POST)
@PropertiesPath(path = "api/posts/post.properties")
@RequestTemplatePath(path = "api/posts/addPost/rq.json")
@ResponseTemplatePath(path = "api/posts/addPost/rs.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class AddPostMethod extends AbstractApiMethodV2 {

    public AddPostMethod(String userId) {
        getProperties().setProperty("userId", userId);
    }
}
