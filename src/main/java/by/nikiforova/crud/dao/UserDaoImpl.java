package by.nikiforova.crud.dao;


import by.nikiforova.crud.entity.User;
import by.nikiforova.crud.exception.DataAccessException;
import by.nikiforova.crud.exception.DatabaseException;
import by.nikiforova.crud.exception.UserNotFoundException;
import by.nikiforova.crud.exception.UserPersistenceException;
import by.nikiforova.crud.util.HibernateUtil;
import org.hibernate.HibernateException;
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
            logger.info("Transaction on saving user with id = {} successfully completed.", user);
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving user: {}", user, e);
            throw new UserPersistenceException("Error saving user", e);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Unexpected error while saving user: {}", user, e);
            throw new DataAccessException("Unexpected error while saving user", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {

        if (id == null) {
            logger.error("Attempted to find user with null id");
            throw new IllegalArgumentException("Id cannot be null");
        }

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) {
                transaction.rollback();
                logger.info("User not found with id: {}", id);
                throw new UserNotFoundException("User not found with id: " + id);
            }
            transaction.commit();
            return Optional.of(user);

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error while finding user by id: {}", id, e);
            throw new DatabaseException("Error while finding user by id", e);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Unexpected error while finding user by id: {}", id, e);
            throw new DataAccessException("Unexpected error while finding user", e);
        }
    }

    @Override
    public List<User> findAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            transaction.commit();
            logger.info("Found {} users", users.size());
            return users;

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error while finding all users", e);
            throw new DatabaseException("Error while finding all users", e);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Unexpected error while finding all users", e);
            throw new DataAccessException("Unexpected error while finding all users", e);
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

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating user: {}", user, e);
            throw new UserPersistenceException("Error updating user", e);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Unexpected error while updating user: {}", user, e);
            throw new DataAccessException("Unexpected error while updating user", e);
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
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting user: {}", user, e);
            throw new UserPersistenceException("Error deleting user", e);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Unexpected error while deleting user: {}", user, e);
            throw new DataAccessException("Unexpected error while deleting user", e);
        }
    }
}
