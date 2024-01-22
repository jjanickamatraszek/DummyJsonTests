package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.*;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/posts/${id}", methodType = HttpMethodType.PUT)
@RequestTemplatePath(path = "api/posts/updatePost/rq.json")
@ResponseTemplatePath(path = "api/posts/updatePost/rs.json")
@PropertiesPath(path = "api/posts/post.properties")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class UpdatePostMethod extends AbstractApiMethodV2 {

    public UpdatePostMethod() {
        replaceUrlPlaceholder("id", getProperties().getProperty("id"));
    }
}
