package api.pojo;

import api.enums.DocumentFormat;
import api.enums.DocumentType;
import api.enums.ProductGroup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Document {
    private DocumentFormat document_format;
    private String product_document;
    private ProductGroup product_group;
    private String signature;
    private DocumentType document_type;

    public Document(@NonNull DocumentFormat document_format,
                    @NonNull String product_document,
                    ProductGroup product_group,
                    @NonNull String signature,
                    @NonNull DocumentType document_type) {
        this.document_format = document_format;
        this.product_document = product_document;
        this.product_group = product_group;
        this.signature = signature;
        this.document_type = document_type;
    }
}
