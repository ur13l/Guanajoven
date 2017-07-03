package mx.gob.jovenes.guanajuato.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codigus on 22/06/2017.
 */
public class TwitterResponse {
    public List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
