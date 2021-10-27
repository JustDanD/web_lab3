package com.justdan.weblab3.db;

import com.justdan.weblab3.result.Result;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class ResultDao {

    private Transaction transaction;
    private Session session;
    private boolean connected = true;

    public void connect() throws PersistenceException {
        session = Util.getSessionFactory().openSession();
    }

    public void close() {
        if(session != null)
            session.close();
    }

    public void rollback() {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    public void send(Result result) throws PersistenceException {
        transaction = session.beginTransaction();
        session.save(result);
        transaction.commit();
    }

    public ArrayList<Result> getAll() throws PersistenceException {
        List<Result> results = session.createQuery("from Result", Result.class).list();
        return (ArrayList<Result>) results;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}
