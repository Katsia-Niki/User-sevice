package by.nikiforova.crud.main;

import by.nikiforova.crud.controller.UserController;
import by.nikiforova.crud.dao.UserDao;
import by.nikiforova.crud.dao.UserDaoImpl;
import by.nikiforova.crud.service.UserService;
import by.nikiforova.crud.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
  private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        UserController userController = null;

        try {
            logger.info("Запуск user-service");

            UserDao userDao = new UserDaoImpl();

            UserService userService = new UserService(userDao);

            userController = new UserController(userService);

            userController.runApplication();

        } catch (Exception e) {
            logger.error("Критическая ошибка при запуске приложения", e);
        } finally {
            logger.info("Завершение работы user-service");

            if (userController != null) {
                userController.close();
            }
            HibernateUtil.shutdown();
        }
    }

}
