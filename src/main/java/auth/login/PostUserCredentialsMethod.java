package auth.login;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.RequestTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/auth/login", methodType = HttpMethodType.POST)
@RequestTemplatePath(path = "api/login/_post/rq.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class PostUserCredentialsMethod extends AbstractApiMethodV2 {

}
