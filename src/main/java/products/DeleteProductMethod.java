package products;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.PropertiesPath;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

@Endpoint(url = "${config.env.api_url}/product/${id}", methodType = HttpMethodType.DELETE)
@ResponseTemplatePath(path = "api/products/deleteProduct/rs.json")
@PropertiesPath(path = "api/products/product.properties")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class DeleteProductMethod extends AbstractApiMethodV2 {

    public DeleteProductMethod() {
        replaceUrlPlaceholder("id", getProperties().getProperty("id"));
    }

    public DeleteProductMethod(String id) {
        replaceUrlPlaceholder("id", id);
    }
}
