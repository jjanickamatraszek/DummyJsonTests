package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.PropertiesPath;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/posts/${id}", methodType = HttpMethodType.GET)
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
@PropertiesPath(path = "api/posts/post.properties")
@ResponseTemplatePath(path = "api/posts/getPost/rs.json")
public class GetPostMethod extends AbstractApiMethodV2 {

    public GetPostMethod() {
        replaceUrlPlaceholder("id", getProperties().getProperty("id"));
    }
}
