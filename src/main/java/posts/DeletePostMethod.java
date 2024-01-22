package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.PropertiesPath;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/posts/${id}", methodType = HttpMethodType.DELETE)
@ResponseTemplatePath(path = "api/posts/deletePost/rs.json")
@PropertiesPath(path = "api/posts/post.properties")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class DeletePostMethod extends AbstractApiMethodV2 {

    public DeletePostMethod() {
        replaceUrlPlaceholder("id", getProperties().getProperty("id"));
    }

    public DeletePostMethod(String id) {
        replaceUrlPlaceholder("id", id);
    }
}
