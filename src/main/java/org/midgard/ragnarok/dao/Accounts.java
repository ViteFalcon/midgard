package org.midgard.ragnarok.dao;

import lombok.extern.apachecommons.CommonsLog;
import org.cinchapi.concourse.Concourse;
import org.cinchapi.concourse.ConnectionPool;
import org.cinchapi.concourse.thrift.Operator;
import org.joda.time.DateTime;
import org.midgard.ragnarok.data.Account;
import org.midgard.ragnarok.data.AccountGender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@CommonsLog
@Repository
public class Accounts {
    private static final long META_TABLE_ID = 0L;
    private static final String USER_ID = "user_id";

    private final ConnectionPool connectionPool;

    @Autowired
    public Accounts(final ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public boolean createAccount(String userId, String password, AccountGender gender) throws Exception {
        try (Concourse request = connectionPool.request()) {
            if (isExistingAccount(request, userId)) {
                return false;
            }
            long id = request.create();
            Account account = Account
                    .builder()
                    .id(id)
                    .userid(userId)
                    .gender(gender)
                    .level(50)
                    .loginCount(0)
                    .state(0)
                    .createDate(DateTime.now())
                    .build();
            request.add(USER_ID, account.getUserid(), id);
            request.add("gender", account.getGender(), id);
            request.add("level", account.getLevel(), id);
            request.add("create_date", account.getCreateDate(), id);
            request.add("login_count", account.getLoginCount(), id);
        } catch (Exception e) {
            log.error("Failed to create account.", e);
            throw e;
        }
        return true;
    }

    private boolean isExistingAccount(Concourse request, String userId) {
        return !request.find(USER_ID, Operator.EQUALS, userId).isEmpty();
    }
}
