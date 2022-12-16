package org.ktoskazalnet.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyUserToCourse {
    private long id;
    private String courseUuid;
}
