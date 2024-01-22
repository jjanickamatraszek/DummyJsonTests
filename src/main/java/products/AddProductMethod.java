package products;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.*;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/products/add", methodType = HttpMethodType.POST)
@PropertiesPath(path = "api/products/addProduct/product_with_wildcards.properties")
@RequestTemplatePath(path = "api/products/addProduct/rq.json")
@ResponseTemplatePath(path = "api/products/addProduct/rs.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class AddProductMethod extends AbstractApiMethodV2 {
}
