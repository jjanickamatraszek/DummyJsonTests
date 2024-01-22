package model.enums;

import lombok.Getter;

@Getter
public enum ProductCategory {
    SMARTPHONES("smartphones"),
    LAPTOPS("laptops"),
    FRAGRANCES("fragrances"),
    SKINCARE("skincare"),
    GROCERIES("groceries"),
    HOME_DECORATION("home-decoration"),
    FURNITURE("furniture"),
    TOPS("tops"),
    WOMENS_DRESSES("womens-dresses"),
    WOMENS_SHOES("womens-shoes"),
    WOMENS_BAGS("womens-bags"),
    WOMENS_JEWELLERY("womens-jewellery"),
    WOMENS_WATCHES("womens-watches"),
    MENS_SHOES("mens-shoes"),
    MENS_SHIRTS("mens-shirts"),
    MENS_WATCHES("mens-watches"),
    SUNGLASSES("sunglasses"),
    AUTOMOTIVE("automotive"),
    MOTORCYCLE("motorcycle"),
    LIGHTING("lighting");

    private String name;

    ProductCategory(String name) {
        this.name=name;
    }
}
