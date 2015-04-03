package com.baybora.bookstore.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.baybora.bookstore.domain.Account;
import com.baybora.bookstore.repository.AccountRepository;

/**
 * JPA based {@link AccountRepository} implementation.
 *  
 * @author Marten Deinum
 * @author Koen Serneels
 *
 */
@Repository("accountRepository")
public class JpaAccountRepository implements AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account findByUsername(String username) {
        String hql = "select c from Account c where c.username=:username";
        TypedQuery<Account> query = this.entityManager.createQuery(hql, Account.class).setParameter("username",
                username);
        List<Account> accounts = query.getResultList();

        return accounts.size() == 1 ? accounts.get(0) : null;
    }

    @Override
    public Account findById(long id) {
        return this.entityManager.find(Account.class, id);
    }

    @Override
    public Account save(Account account) {
        if (account.getId() != null) {
            return this.entityManager.merge(account);
        } else {
            this.entityManager.persist(account);
            return account;
        }
    }

}
