package murach.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import murach.business.User;

public class UserDB {

    public static long insert(User user) {
        EntityManager em =
            JpaUtil.getEntityManagerFactory().createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(user);

            tx.commit();
            return user.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}