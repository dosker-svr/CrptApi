package api.pojo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Description {
    private String participantInn;

    public Description(@NonNull String participantInn) {
        this.participantInn = participantInn;
    }
}
