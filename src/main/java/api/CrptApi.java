package api;

import api.enums.DocumentFormat;
import api.enums.DocumentType;
import api.enums.ProductGroup;
import api.pojo.Description;
import api.pojo.DocumentProductIntroRF;
import api.pojo.Product;
import api.pojo.RequestToAuthorisation;
import api.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.sun.istack.internal.NonNull;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private static final Deque<Pair<String, DocumentProductIntroRF>> queueToSendDocProdIntroRF = new ConcurrentLinkedDeque<>();

    private final TimeUnit timeUnit;
    private final int requestLimit;
    @Getter
    private static int requestCount = 0;
    @Getter
    private static LocalDateTime requestStartTime;

    private final static Gson gson = new Gson();

    @Getter
    private static String token;
    @Getter
    private static LocalDateTime lifetimeToken;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public String requestToCreateProductIntroRFDocument(DocumentProductIntroRF document, String signature)
            throws IllegalArgumentException, IOException, LimitExceededException {

        try {
            setRequestCountAndTime();
        } catch (LimitExceededException ex) {
            queueToSendDocProdIntroRF.addFirst(new Pair<>(signature, document));
            throw new LimitExceededException(ex.getMessage());
        }

        if (!queueToSendDocProdIntroRF.isEmpty()) {
            queueToSendDocProdIntroRF.addLast(new Pair<>(signature, document));
            Pair<String, DocumentProductIntroRF> docAndSignFromQueue = queueToSendDocProdIntroRF.pollFirst();
            assert docAndSignFromQueue != null;
            document = docAndSignFromQueue.getValue();
            signature = docAndSignFromQueue.getKey();
        }

        try {
            authorisation(signature);
        } catch (IllegalArgumentException | IOException ex) {
            queueToSendDocProdIntroRF.addFirst(new Pair<>(signature, document));

            if (ex.getClass().equals(IllegalArgumentException.class)) {
                throw new IllegalArgumentException(((IllegalArgumentException) ex).getMessage());
            } else if (ex.getClass().equals(IOException.class)) {
                throw new IOException(((IOException) ex).getMessage());
            } else if (ex.getClass().equals(HttpResponseException.class)) {
                throw new HttpResponseException(
                        ((HttpResponseException) ex).getStatusCode(),
                        ((HttpResponseException) ex).getMessage()
                );
            }
        }

        HttpResponse createDocResp = Request.Post(Constants.URL_PRODUCT_INTRO_RF)
                .addHeader("Accept", Constants.CONTENT_TYPE_JSON)
                .addHeader("Content-type", Constants.CONTENT_TYPE_JSON)
                .addHeader("Authorization", "Bearer " + token)
                .bodyString(gson.toJson(document), ContentType.APPLICATION_JSON)
                .socketTimeout(3_000)
                .connectTimeout(3_000)
                .execute()
                .returnResponse();

        return EntityUtils.toString(createDocResp.getEntity(), StandardCharsets.UTF_8);
    }

    public synchronized void authorisation(String signature)
            throws IllegalArgumentException, HttpResponseException, IOException {

        if (token == null || lifetimeToken == null || lifetimeToken.isBefore(LocalDateTime.now())) {
            HttpResponse response = Request.Get(Constants.URL_REQUEST_AUTHORISATION)
                    .socketTimeout(3_000)
                    .connectTimeout(3_000)
                    .execute()
                    .returnResponse();

            int requestAuthStatusCode = response.getStatusLine().getStatusCode();
            String authResponseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (requestAuthStatusCode == HttpStatus.SC_OK) {
                RequestToAuthorisation auth = gson.fromJson(
                        authResponseBody,
                        RequestToAuthorisation.class
                );

                if (auth.getUuid() != null) {
                    auth.setData(signature);
                    String jsonToToken = gson.toJson(auth);
                    HttpResponse responseWithToken = Request.Post(Constants.URL_GET_AUTH_TOKEN)
                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                            .bodyString(jsonToToken, ContentType.APPLICATION_JSON)
                            .socketTimeout(3_000)
                            .connectTimeout(3_000)
                            .execute()
                            .returnResponse();

                    int responseWithTokenStatusCode = responseWithToken.getStatusLine().getStatusCode();
                    JsonObject jsonFromResponse = gson.fromJson(
                            EntityUtils.toString(responseWithToken.getEntity(), StandardCharsets.UTF_8),
                            JsonObject.class
                    );
                    if (responseWithTokenStatusCode == HttpStatus.SC_OK) {
                        token = jsonFromResponse.get("token").getAsString();
                        lifetimeToken = LocalDateTime.now().plusHours(10);
                    } else {
                        throw new HttpResponseException(responseWithTokenStatusCode, jsonFromResponse.get("error_message").getAsString());
                    }
                } else {
                    throw new IllegalArgumentException("Authorization response doesn't contain uuid.");
                }
            } else {
                throw new HttpResponseException(requestAuthStatusCode, authResponseBody);
            }
        }
    }

    private synchronized void setRequestCountAndTime() throws LimitExceededException {
        if (requestCount != 0) {
            boolean requestTimeIsOut = requestStartTime.plusNanos(timeUnit.toNanos(1L))
                    .isBefore(
                            LocalDateTime.now()
                    );

            if ((requestCount >= requestLimit) && (!requestTimeIsOut)) {
                throw new LimitExceededException("Limit on the number of requests has exceeded.");
            } else if (requestTimeIsOut) {
                requestCount = 0;
                requestStartTime = LocalDateTime.now();
                requestCount++;
            } else {
                requestCount++;
            }
        } else {
            requestStartTime = LocalDateTime.now();
            requestCount++;
        }
    }

//    class Constants {
//        final static String URL_CREATE_DOCUMENT = "https://ismp.crpt.ru/api/v3/lk/documents/create";
//        final static String URL_PRODUCT_INTRO_RF = "https://ismp.crpt.ru/api/v3/lk/documents/commissioning/contract/create";
//        final static String URL_REQUEST_AUTHORISATION = "https://ismp.crpt.ru/api/v3/auth/cert/key";
//        final static String URL_GET_AUTH_TOKEN = "https://ismp.crpt.ru/api/v3/auth/cert/";
//
//        final static String CONTENT_TYPE_JSON = "application/json";
//    }

//    @Getter
//    @Setter
//    public class RequestToAuthorisation {
//        private String uuid;
//        private String data;
//
//        public RequestToAuthorisation(String uuid, String data) {
//            this.uuid = uuid;
//            this.data = data;
//        }
//    }

//    @Getter
//    @Setter
//    public static class Document {
//        private DocumentFormat document_format;
//        private String product_document;
//        private ProductGroup product_group;
//        private String signature;
//        private DocumentType document_type;
//
//        public Document(@NonNull DocumentFormat document_format,
//                        @NonNull String product_document,
//                        ProductGroup product_group,
//                        @NonNull String signature,
//                        @NonNull DocumentType document_type) {
//            this.document_format = document_format;
//            this.product_document = product_document;
//            this.product_group = product_group;
//            this.signature = signature;
//            this.document_type = document_type;
//        }
//    }

//    @Getter
//    @Setter
//    public static class DocumentProductIntroRF extends Document {
//
//        private Description description;
//        private String doc_id;
//        private String doc_status;
//        private String doc_type;
//        private boolean importRequest;
//        private String owner_inn;
//        private String participant_inn;
//        private String producer_inn;
//        private LocalDate production_date;
//        private String production_type;
//        private Product products;
//        private LocalDate reg_date;
//        private String reg_number;
//
//        public DocumentProductIntroRF(@NonNull DocumentFormat document_format,
//                                      @NonNull String product_document,
//                                      ProductGroup product_group,
//                                      @NonNull String signature,
//                                      @NonNull DocumentType document_type,
//
//                                      Description description,
//                                      @NonNull String doc_id,
//                                      @NonNull String doc_status,
//                                      @NonNull String doc_type,
//                                      boolean importRequest,
//                                      @NonNull String owner_inn,
//                                      @NonNull String participant_inn,
//                                      @NonNull String producer_inn,
//                                      @NonNull LocalDate production_date,
//                                      @NonNull String production_type,
//                                      Product products,
//                                      @NonNull LocalDate reg_date,
//                                      String reg_number) {
//            super(document_format, product_document, product_group, signature, document_type);
//            this.description = description;
//            this.doc_id = doc_id;
//            this.doc_status = doc_status;
//            this.doc_type = doc_type;
//            this.importRequest = importRequest;
//            this.owner_inn = owner_inn;
//            this.participant_inn = participant_inn;
//            this.producer_inn = producer_inn;
//            this.production_date = production_date;
//            this.production_type = production_type;
//            this.products = products;
//            this.reg_date = reg_date;
//            this.reg_number = reg_number;
//        }
//    }

//    @Getter
//    @Setter
//    public static class Description {
//        private String participantInn;
//
//        public Description(@NonNull String participantInn) {
//            this.participantInn = participantInn;
//        }
//    }

//    @Getter
//    @Setter
//    public static class Product {
//        private String certificate_document;
//        private LocalDate certificate_document_date;
//        private String certificate_document_number;
//        private String owner_inn;
//        private String producer_inn;
//        private LocalDate production_date;
//        private String tnved_code;
//        private String uit_code;
//        private String uitu_code;
//
//        public Product(String certificate_document,
//                       LocalDate certificate_document_date,
//                       String certificate_document_number,
//                       @NonNull String owner_inn,
//                       @NonNull String producer_inn,
//                       @NonNull LocalDate production_date,
//                       @NonNull String tnved_code,
//                       String uit_code,
//                       String uitu_code) {
//            this.certificate_document = certificate_document;
//            this.certificate_document_date = certificate_document_date;
//            this.certificate_document_number = certificate_document_number;
//            this.owner_inn = owner_inn;
//            this.producer_inn = producer_inn;
//            this.production_date = production_date;
//            this.tnved_code = tnved_code;
//            this.uit_code = uit_code;
//            this.uitu_code = uitu_code;
//        }
//    }

//    public enum DocumentFormat {
//        MANUAL, XML, CSV
//    }
//
//    public enum ProductGroup {
//        CLOTHES("clothes"), SHOES("shoes"), TOBACCO("tobacco"),
//        PERFUMERY("perfumery"), TIRES("tires"), ELECTRONICS("electronics"), PHARMA("pharma"),
//        MILK("milk"), BICYCLE("bicycle"), WHEELCHAIRS("wheelchairs");
//
//        final String description;
//
//        ProductGroup(String description) {
//            this.description = description;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//    }
//
//    public enum DocumentType {
//        AGGREGATION_DOCUMENT, AGGREGATION_DOCUMENT_CSV, AGGREGATION_DOCUMENT_XML,
//        DISAGGREGATION_DOCUMENT, DISAGGREGATION_DOCUMENT_CSV, DISAGGREGATION_DOCUMENT_XML,
//        REAGGREGATION_DOCUMENT, REAGGREGATION_DOCUMENT_CSV, REAGGREGATION_DOCUMENT_XML,
//        LP_SHIP_GOODS, LP_SHIP_GOODS_CSV, LP_SHIP_GOODS_XML,
//        LP_INTRODUCE_GOODS, LP_INTRODUCE_GOODS_CSV, LP_INTRODUCE_GOODS_XML,
//        LP_ACCEPT_GOODS, LP_ACCEPT_GOODS_XML,
//        LK_REMARK, LK_REMARK_CSV, LK_REMARK_XML, LK_RECEIPT, LK_RECEIPT_XML, LK_RECEIPT_CSV,
//        LP_GOODS_IMPORT, LP_GOODS_IMPORT_CSV, LP_GOODS_IMPORT_XML, LP_CANCEL_SHIPMENT, LP_CANCEL_SHIPMENT_CSV, LP_CANCEL_SHIPMENT_XML,
//        LK_KM_CANCELLATION, LK_KM_CANCELLATION_CSV, LK_KM_CANCELLATION_XML,
//        LK_APPLIED_KM_CANCELLATION, LK_APPLIED_KM_CANCELLATION_CSV, LK_APPLIED_KM_CANCELLATION_XML,
//        LK_CONTRACT_COMMISSIONING, LK_CONTRACT_COMMISSIONING_CSV, LK_CONTRACT_COMMISSIONING_XML,
//        LK_INDI_COMMISSIONING, LK_INDI_COMMISSIONING_CSV, LK_INDI_COMMISSIONING_XML,
//        LP_SHIP_RECEIPT, LP_SHIP_RECEIPT_CSV, LP_SHIP_RECEIPT_XML,
//        OST_DESCRIPTION, OST_DESCRIPTION_CSV, OST_DESCRIPTION_XML,
//        CROSSBORDER, CROSSBORDER_CSV, CROSSBORDER_XML,
//        LP_INTRODUCE_OST, LP_INTRODUCE_OST_CSV, LP_INTRODUCE_OST_XML,
//        LP_RETURN, LP_RETURN_CSV, LP_RETURN_XML,
//        LP_SHIP_GOODS_CROSSBORDER, LP_SHIP_GOODS_CROSSBORDER_CSV, LP_SHIP_GOODS_CROSSBORDER_XML,
//        LP_CANCEL_SHIPMENT_CROSSBORDER
//    }
}
