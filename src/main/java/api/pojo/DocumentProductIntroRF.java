package api.pojo;

import api.CrptApi;
import api.enums.DocumentFormat;
import api.enums.DocumentType;
import api.enums.ProductGroup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DocumentProductIntroRF extends Document {
    private Description description;
    private String doc_id;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private LocalDate production_date;
    private String production_type;
    private Product products;
    private LocalDate reg_date;
    private String reg_number;

    public DocumentProductIntroRF(@NonNull DocumentFormat document_format,
                                  @NonNull String product_document,
                                  ProductGroup product_group,
                                  @NonNull String signature,
                                  @NonNull DocumentType document_type,

                                  Description description,
                                  @NonNull String doc_id,
                                  @NonNull String doc_status,
                                  @NonNull String doc_type,
                                  boolean importRequest,
                                  @NonNull String owner_inn,
                                  @NonNull String participant_inn,
                                  @NonNull String producer_inn,
                                  @NonNull LocalDate production_date,
                                  @NonNull String production_type,
                                  Product products,
                                  @NonNull LocalDate reg_date,
                                  String reg_number) {
        super(document_format, product_document, product_group, signature, document_type);
        this.description = description;
        this.doc_id = doc_id;
        this.doc_status = doc_status;
        this.doc_type = doc_type;
        this.importRequest = importRequest;
        this.owner_inn = owner_inn;
        this.participant_inn = participant_inn;
        this.producer_inn = producer_inn;
        this.production_date = production_date;
        this.production_type = production_type;
        this.products = products;
        this.reg_date = reg_date;
        this.reg_number = reg_number;
    }
}
