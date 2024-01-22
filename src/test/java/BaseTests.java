import com.zebrunner.carina.api.apitools.validation.JsonComparatorContext;
import com.zebrunner.carina.core.IAbstractTest;
import model.enums.ProductCategory;

import java.time.LocalDate;
import java.util.Arrays;

public class BaseTests implements IAbstractTest {

    protected JsonComparatorContext comparatorContext = JsonComparatorContext.context()
            .<String>withPredicate("todayDatePredicate", date -> date.startsWith(LocalDate.now().toString()))
            .<Double>withPredicate("isPositiveAndLTE10", rating -> rating <= 10.00 && rating >= 0)
            .<String>withPredicate("isProductCategory", category -> Arrays.asList(ProductCategory.values()).stream().filter(p -> p.getName().equals(category)).count() == 1);
}
