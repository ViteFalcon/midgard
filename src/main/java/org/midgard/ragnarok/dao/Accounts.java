package org.midgard.ragnarok.dao;

import lombok.extern.apachecommons.CommonsLog;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.midgard.ragnarok.data.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@CommonsLog
@Repository
public class Accounts extends CouchDbRepositorySupport<Account> {
    @Autowired
    public Accounts(final CouchDbConnector db) {
        super(Account.class, db);
    }
}
