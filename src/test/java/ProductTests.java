import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.agent.core.registrar.TestCase;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.R;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.enums.ProductCategory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import products.AddProductMethod;
import products.DeleteProductMethod;
import products.GetAllProductsCategoriesMethod;
import products.GetAllProductsMethod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductTests extends BaseTests {

    @DataProvider(name = "getIncorrectProductsIds")
    public Object[][] getIncorrectProductsIds() {
        return new Object[][]{
                {"10000000", "JOANNA-74"},
                {"0", "JOANNA-75"},
                {"-1", "JOANNA-76"},
                {"aa", "JOANNA-77"}
        };
    }

    @Test
    @TestCaseKey("JOANNA-71")
    public void getAllProductsTest() {
        int expectedLimit = Integer.parseInt(R.TESTDATA.get("products.defaultLimit"));
        int expectedSkip = Integer.parseInt(R.TESTDATA.get("products.defaultSkip"));

        GetAllProductsMethod getAllProducts = new GetAllProductsMethod();
        Response allProductsRs = getAllProducts.callAPIExpectSuccess();
        getAllProducts.validateResponseAgainstSchema("api/products/getAllProducts/rs.schema");
        JsonPath jsonPath = allProductsRs.jsonPath();
        int actualProductsCount = jsonPath.getList("products.id").size();
        int actualLimit = jsonPath.getInt("limit");
        int actualSkip = jsonPath.getInt("skip");
        Assert.assertTrue(actualProductsCount <= expectedLimit,
                "Amount of products returned is greater than expected limit");
        Assert.assertEquals(actualLimit, expectedLimit,
                "Value for limit in response is different than default one");
        Assert.assertEquals(actualSkip, expectedSkip,
                "Value for skip in response is different than default one");
    }

    @Test
    @TestCaseKey("JOANNA-72")
    public void addNewProductTest() {
        AddProductMethod addProduct = new AddProductMethod();
        addProduct.callAPIExpectSuccess();
        addProduct.validateResponse(comparatorContext);
        addProduct.validateResponseAgainstSchema("api/products/addProduct/rs.schema");
    }

    @Test
    @TestCaseKey("JOANNA-73")
    public void deleteProductTest() {
        DeleteProductMethod deleteProduct = new DeleteProductMethod();
        deleteProduct.callAPIExpectSuccess();
        deleteProduct.validateResponse(comparatorContext);
        deleteProduct.validateResponseAgainstSchema("api/products/deleteProduct/rs.schema");
    }

    @Test(dataProvider = "getIncorrectProductsIds")
    public void returnErrorForDeleteProductWithInvalidIdTest(String incorrectProductId, String caseKey) {
        TestCase.setTestCaseKey(caseKey);
        DeleteProductMethod deleteProduct = new DeleteProductMethod(incorrectProductId);
        String expectedErrorMessage = deleteProduct.getProperties().getProperty("error.productNotFound.message").formatted(incorrectProductId);
        deleteProduct.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
        Response response = deleteProduct.callAPI();
        String actualErrorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage,
                "Error message is different than expected");
    }

    @Test
    @TestCaseKey("JOANNA-78")
    public void getAllProductsCategoriesTest() {
        List<String> expectedCategories = Arrays.stream(ProductCategory.values()).map(ProductCategory::getName).toList();

        GetAllProductsCategoriesMethod categoriesMethod = new GetAllProductsCategoriesMethod();
        Response response = categoriesMethod.callAPI();
        Set<String> actualCategories = new HashSet<>(response.jsonPath().getList("$"));
        actualCategories.removeAll(expectedCategories);
        Assert.assertTrue(expectedCategories.containsAll(actualCategories),
                "Some categories returned %s are not expected".formatted(actualCategories.removeAll(expectedCategories)));
    }
}
