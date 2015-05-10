package org.midgard.ragnarok.data;

import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

@Data
@Builder
public class Account {
    private final long id;
    private final String userid;
    private final AccountGender gender;
    private final String email;
    private final int level;
    private final long state;
    private final DateTime unbanTime;
    private final DateTime expirationTime;
    private final long loginCount;
    private final DateTime lastLogin;
    private final String lastIp;
    private final DateTime createDate;
}
