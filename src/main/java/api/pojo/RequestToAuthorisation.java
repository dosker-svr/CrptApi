package api.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestToAuthorisation {
    private String uuid;
    private String data;

    public RequestToAuthorisation(String uuid, String data) {
        this.uuid = uuid;
        this.data = data;
    }
}
