package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class App {

    private static Session session;

    private static final String[] FIRST_NAMES = {
            "John", "Jane", "Alex", "Emily", "Michael", "Sarah", "David", "Laura", "Robert", "Anna",
            "Chris", "Jessica", "James", "Sophia", "Daniel", "Olivia", "Matthew", "Emma", "Joshua", "Ava"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson"
    };

    private static final String[] STREET_NAMES = {
            "Main St", "High St", "Maple Ave", "Oak St", "Pine St", "Cedar St", "Elm St", "Washington Ave", "Lake St", "Hill St"
    };

    private static final String[] CITY_NAMES = {
            "Springfield", "Rivertown", "Lakeview", "Hilltop", "Fairview", "Greenville", "Georgetown", "Kingston", "Brooklyn", "Farmington"
    };

    private static final String[] EMAIL_DOMAINS = {
            "@example.com", "@mail.com", "@test.com", "@domain.com", "@email.com"
    };

    private static SessionFactory getSessionFactory(String password) throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Image.class);
        configuration.addAnnotatedClass(Garage.class);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/myFirstDataBase?serverTimezone=Asia/Jerusalem");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generatePersons(List<Garage> garages) throws Exception {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
            Person person = new Person(firstName, lastName, "password" + i, email);

            // Randomly associate each person with 1 or more garages
            int numberOfGarages = random.nextInt(garages.size()) + 1;
            for (int j = 0; j < numberOfGarages; j++) {
                Garage garage = garages.get(random.nextInt(garages.size()));
                person.addGarage(garage);
            }
            session.save(person);
            session.flush(); // Ensure immediate execution of SQL INSERT
        }
    }

    private static List<Garage> generateGarages() throws Exception {
        List<Garage> garages = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String address = STREET_NAMES[random.nextInt(STREET_NAMES.length)] + ", " + CITY_NAMES[random.nextInt(CITY_NAMES.length)];
            String phoneNumber = "555-" + (random.nextInt(9000) + 1000);
            String ownerPhoneNumber = "555-" + (random.nextInt(9000) + 1000);
            Garage garage = new Garage(address, phoneNumber, ownerPhoneNumber);
            session.save(garage);
            session.flush(); // Ensure immediate execution of SQL INSERT
            garages.add(garage);
        }
        return garages;
    }

    private static void generateCarsAndImages() throws Exception {
        Random random = new Random();
        List<Person> persons = getAllPersons();
        List<Garage> garages = getAllGarages();

        for (int i = 0; i < 200; i++) {
            Car car = new Car("MOO-" + random.nextInt(10000), 100000, 2000 + random.nextInt(19));
            Person owner = persons.get(random.nextInt(persons.size()));
            car.setOwner(owner);
            owner.addCar(car);
            session.save(car);
            session.flush(); // Ensure immediate execution of SQL INSERT

            Image image = new Image("http://example.com/image" + i + ".jpg");
            car.setImage(image);
            session.save(image);
            session.flush(); // Ensure immediate execution of SQL INSERT

            // Randomly associate each car with 1 or more garages
            int numberOfGarages = random.nextInt(garages.size()) + 1; // Random number of garages
            for (int j = 0; j < numberOfGarages; j++) {
                Garage garage = garages.get(random.nextInt(garages.size()));
                car.addGarage(garage);
            }
            session.save(car); // Save car to update the association
            session.flush(); // Ensure the changes are persisted immediately
        }
    }

    private static List<Person> getAllPersons() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        query.from(Person.class);
        List<Person> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Garage> getAllGarages() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Garage> query = builder.createQuery(Garage.class);
        query.from(Garage.class);
        List<Garage> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Car> getAllCars() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data = session.createQuery(query).getResultList();
        return data;
    }

    private static void printAllGarages() throws Exception {
        List<Garage> garages = getAllGarages();
        for (Garage garage : garages) {
            System.out.println("Garage ID: " + garage.getId());
            System.out.println("Address: " + garage.getAddress());
            System.out.println("Phone Number: " + garage.getPhoneNumber());
            System.out.println("Owner Phone Number: " + garage.getOwnerPhoneNumber());
            System.out.print("Cars Allowed: ");
            for (Car car : garage.getCars()) {
                System.out.print(car.getLicensePlate() + " ");
            }
            System.out.println("\n");
        }
    }

    private static void printAllCars() throws Exception {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            System.out.println("Car ID: " + car.getId());
            System.out.println("License Plate: " + car.getLicensePlate());
            System.out.println("Price: " + car.getPrice());
            System.out.println("Year: " + car.getYear());
            System.out.println("Owner: " + car.getOwner().getFirstName() + " " + car.getOwner().getLastName());
            System.out.print("Garages Allowed: ");
            for (Garage garage : car.getGarages()) {
                System.out.print(garage.getAddress() + " ");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter database password: ");
        String password = scanner.nextLine();

        try {
            SessionFactory sessionFactory = getSessionFactory(password);
            session = sessionFactory.openSession();
            session.beginTransaction();

            List<Garage> garages = generateGarages();
            generatePersons(garages);
            generateCarsAndImages();
            printAllGarages();
            printAllCars();

            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
