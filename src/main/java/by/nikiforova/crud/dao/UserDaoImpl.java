package by.nikiforova.crud.dao;



import by.nikiforova.crud.entity.User;
import by.nikiforova.crud.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {


    private static final Logger logger = LogManager.getLogger();


    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.debug("Transaction on saving user with id = {} successfully completed.", user);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving user: {}", user, e);
            throw new RuntimeException("Error saving user", e);
        }
    }
    @Override
    public Optional<User> findById(Integer id) {

        if (id == null) {
            logger.error("Attempted to find user with null id");
            throw new IllegalArgumentException("Id cannot be null");
        }

        Transaction transaction;

        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            transaction.commit();
            if (user == null) {
                transaction.rollback();
                logger.warn("User not found with id: {}", id);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            transaction.commit();
            logger.debug("Found {} users", users.size());
            return users;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error while finding all users", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("User updated successfully: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating user: {}", user, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
            logger.error("User deleted successfully: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user: {}", user, e);
            throw new RuntimeException(e);
        }
    }
}
