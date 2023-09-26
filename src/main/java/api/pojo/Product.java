package api.pojo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Product {
    private String certificate_document;
    private LocalDate certificate_document_date;
    private String certificate_document_number;
    private String owner_inn;
    private String producer_inn;
    private LocalDate production_date;
    private String tnved_code;
    private String uit_code;
    private String uitu_code;

    public Product(String certificate_document,
                   LocalDate certificate_document_date,
                   String certificate_document_number,
                   @NonNull String owner_inn,
                   @NonNull String producer_inn,
                   @NonNull LocalDate production_date,
                   @NonNull String tnved_code,
                   String uit_code,
                   String uitu_code) {
        this.certificate_document = certificate_document;
        this.certificate_document_date = certificate_document_date;
        this.certificate_document_number = certificate_document_number;
        this.owner_inn = owner_inn;
        this.producer_inn = producer_inn;
        this.production_date = production_date;
        this.tnved_code = tnved_code;
        this.uit_code = uit_code;
        this.uitu_code = uitu_code;
    }
}
