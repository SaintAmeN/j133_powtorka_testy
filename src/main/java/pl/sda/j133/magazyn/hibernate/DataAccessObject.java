package pl.sda.j133.magazyn.hibernate;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Paweł Recław, AmeN
 * @project warsztat
 * @created 03.12.2022
 * <p>
 * Data Access Object - instancja klasy która umożliwia manipulowanie danymi w bazie.
 * Posiada metody CRUD dla wybranego modelu.
 */
public class DataAccessObject<T> {

    // C (RUD)
    public void insert(T obiektDoWstawieniaDoBazy) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(obiektDoWstawieniaDoBazy);

            transaction.commit();
        } catch (Exception e) {
            System.err.println("Błąd: " + e);
        }
    }

    // (C) R (UD)
    public List<T> findAll(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            TypedQuery<T> zapytanie = session.createQuery("FROM " + tClass.getName(), tClass);

            list.addAll(zapytanie.getResultList());
        } catch (Exception e) {
            System.err.println("Błąd: " + e);
        }
        return list;
    }

    // (C) R (UD)
    public Optional<T> find(Class<T> tClass, Long id) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            T encja = session.get(tClass, id);

            return Optional.ofNullable(encja);
        } catch (Exception ioe) {
            System.err.println("Błąd bazy: " + ioe);
        }
        return Optional.empty();
    }

    // (CRU) D
    public boolean delete(Class<T> tClass, Long id) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // sprawdz czy istnieje - pobierz z bazy i sprawdz czy nie jest null
            T encja = session.get(tClass, id);
            if (encja == null) {
                return false; // nie ma encji z takim id
            }

            session.remove(encja);
            transaction.commit();
            return true; // znalezlismy encje i ją usunelismy, zrobilismy commit.
        } catch (Exception ioe) {
            System.err.println("Błąd bazy: " + ioe);
        }

        return false; // wystąpił błąd, nie usunelismy rekordu
    }

    public void update(Class<T> tClass, Long id, T encjaAktualizujaca) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // kolejne linie weryfikują to że rekord istnieje i że będziemy mogli go aktualizować w jednej transakcji
            T encja = session.get(tClass, id);
            if (encja == null) {
                System.err.println("Nie znaleziono rekordu!");
                return;
            }

            session.merge(encjaAktualizujaca);

            transaction.commit();
        } catch (Exception e) {
            System.err.println("Błąd: " + e);
        }
    }

    public boolean exists(Class<T> tClass, Long id) {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            T encja = session.get(tClass, id);
            if (encja != null) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Błąd: " + e);
        }
        return false;
    }
}
