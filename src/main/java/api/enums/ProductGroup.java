package api.enums;

public enum ProductGroup {
    CLOTHES("clothes"), SHOES("shoes"), TOBACCO("tobacco"),
    PERFUMERY("perfumery"), TIRES("tires"), ELECTRONICS("electronics"), PHARMA("pharma"),
    MILK("milk"), BICYCLE("bicycle"), WHEELCHAIRS("wheelchairs");

    final String description;

    ProductGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
