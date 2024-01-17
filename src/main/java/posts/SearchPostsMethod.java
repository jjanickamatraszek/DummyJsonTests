package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import lombok.NoArgsConstructor;

@Endpoint(url = "${config.env.api_url}/posts/search", methodType = HttpMethodType.GET)
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
@NoArgsConstructor
public class SearchPostsMethod extends AbstractApiMethodV2 {

    public SearchPostsMethod(String searchPhrase) {
        addUrlParameter("q", searchPhrase);
    }
}
