package posts;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import lombok.NoArgsConstructor;

import java.util.List;

@Endpoint(url = "${config.env.api_url}/posts", methodType = HttpMethodType.GET)
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
@NoArgsConstructor
public class GetAllPostsMethod extends AbstractApiMethodV2 {

    public GetAllPostsMethod(int limit) {
        addUrlParameter("limit", String.valueOf(limit));
    }

    public GetAllPostsMethod(List<String> selectedEntries) {
        addUrlParameter("select", String.join(",", selectedEntries));
    }

    public GetAllPostsMethod(int limit, int skip) {
        this(limit);
        addUrlParameter("skip", String.valueOf(skip));
    }
}
