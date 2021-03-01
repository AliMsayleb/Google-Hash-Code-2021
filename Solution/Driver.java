package driver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;


class Intersection {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    Integer id;
    HashMap<String, Street> streetsLeadingToTheIntersection = new HashMap<>();
    Integer totalCarsPassing = 0;
    Integer totalInitialCarCounts = 0;
}

class Street {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    String name;
    Intersection startIntersection;
    Intersection endIntersection;
    Integer length;
    Integer carCounts = 0;
    Integer initialCarCounts = 0;
    Integer score;
}

class Car {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    LinkedHashMap<String, Street> path = new LinkedHashMap<>();
    Integer totalPathLength = 0;
}

public class Driver {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Static fields/initializers 
    //~ ----------------------------------------------------------------------------------------------------------------

    static String streetNamePattern = "([a-z-])+";

    static HashMap<Integer, Intersection> intersections = new HashMap<>();
    static HashMap<String, Street> streets = new HashMap<>();
    static List<Car> cars = new ArrayList<>();
    static Integer simulationDuration;
    static Integer bonus;
    static Integer numberOfIntersections;
    static Integer numberOfStreets;
    static Integer numberOfCars;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        List<String> files = Arrays.asList("a.txt", "b.txt", "c.txt", "d.txt", "e.txt", "f.txt");

        for (String f : files) {
            String output = "";
            InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream(f);
            assert inputStream != null;
            Scanner scanner = new Scanner(inputStream);
            parseInput(scanner);

            List<Intersection> oneStreetIntersections = new ArrayList<>();
            List<Intersection> multipleStreetsIntersections = new ArrayList<>();
            for (var intersectionEntry : intersections.entrySet()) {
                Intersection intersection = intersectionEntry.getValue();
                int size = intersection.streetsLeadingToTheIntersection.size();
                if (size == 1) {
                    oneStreetIntersections.add(intersection);
                } else if (size > 1) {
                    multipleStreetsIntersections.add(intersection);
                }
            }
            int totalIntersections = oneStreetIntersections.size() + multipleStreetsIntersections.size();
            for (Intersection intersection : oneStreetIntersections) {
                output += intersection.id + "\n";
                output += "1" + "\n";
                Street street = intersection.streetsLeadingToTheIntersection.entrySet().iterator().next().getValue();
                output += street.name + " 1" + "\n";
            }
            for (Intersection intersection : multipleStreetsIntersections) {
                HashMap<String, Street> intersectionStreets = intersection.streetsLeadingToTheIntersection;
                List<Street> interStreets = new ArrayList<Street>(intersectionStreets.values());
//                interStreets.sort((o1, o2) -> { return o2.initialCarCounts - o1.initialCarCounts; });
//                int score = 1;
//                for (int i = 0; i < interStreets.size(); i++) {
//                    if (interStreets.get(i).carCounts == 0) {
//                        continue;
//                    }
//                    interStreets.get(i).score = score++;
//                }
                interStreets.sort((o1, o2) -> { return o2.initialCarCounts - o1.initialCarCounts; });

                String tempOutput = "";
                int totalOpenedStreetsInIntersection = 0;
                for (Street street : interStreets) {
                    if (street.carCounts == 0) {
                        continue;
                    }
                    totalOpenedStreetsInIntersection++;
//                    int count = (street.initialCarCounts > 1) ? street.initialCarCounts : 1;
                    double totalCarsRatio = .3;
                    double initialCarsRatio = 1 - totalCarsRatio;
                    double totalCarsQuotient = street.carCounts * totalCarsRatio;
                    double initialCarsQuotient = street.initialCarCounts * initialCarsRatio;
                    int count = (int) Math.ceil(totalCarsQuotient + initialCarsQuotient);
                    count = Math.max(1, count);
                    tempOutput += street.name + " " + count + "\n";
                }
                if (totalOpenedStreetsInIntersection == 0) {
                    totalIntersections--;
                    continue;
                }
                output += intersection.id + "\n";
                output += totalOpenedStreetsInIntersection + "\n";
                output += tempOutput;
            }

            output = totalIntersections + "\n" + output;

            FileWriter outFile = new FileWriter(f + "out.txt");
            System.out.println(output);
            outFile.write(output);
            outFile.close();
            clearEverything();
        }

    }

    static void clearEverything() {
        intersections = new HashMap<>();
        streets = new HashMap<>();
        cars = new ArrayList<>();
    }

    static void parseInput(Scanner scanner) {
        simulationDuration = scanner.nextInt();
        numberOfIntersections = scanner.nextInt();
        numberOfStreets = scanner.nextInt();
        numberOfCars = scanner.nextInt();
        bonus = scanner.nextInt();
        parseStreets(scanner);
        parseCars(scanner);
    }

    static void parseCars(Scanner scanner) {
        for (int i = 0; i < numberOfCars; i++) {
            parseCar(scanner);
        }
    }

    static void parseCar(Scanner scanner) {
        int paths = scanner.nextInt();
        Car car = new Car();
        for (int i = 0; i < paths; i++) {
            Street street = streets.get(scanner.next(streetNamePattern));
            car.totalPathLength += street.length;
            car.path.put(street.name, street);
            street.carCounts++;
            if (i == 0) {
                street.initialCarCounts++;
                street.endIntersection.totalInitialCarCounts++;
            }
            street.endIntersection.totalCarsPassing++;
        }
        cars.add(car);
    }

    static void parseStreets(Scanner scanner) {
        for (int i = 0; i < numberOfStreets; i++) {
            parseStreet(scanner);
        }
    }

    static void parseStreet(Scanner scanner) {
        Street street = new Street();
        int startIntersectionInt = scanner.nextInt();
        int endIntersectionInt = scanner.nextInt();
        street.name = scanner.next(streetNamePattern);

        Intersection startIntersection;
        if (!intersections.containsKey(startIntersectionInt)) {
            startIntersection = new Intersection();
            startIntersection.id = startIntersectionInt;
            intersections.put(startIntersectionInt, startIntersection);
        } else {
            startIntersection = intersections.get(startIntersectionInt);
        }
        street.startIntersection = startIntersection;

        Intersection endIntersection;
        if (!intersections.containsKey(endIntersectionInt)) {
            endIntersection = new Intersection();
            endIntersection.id = endIntersectionInt;
            intersections.put(endIntersectionInt, endIntersection);
        } else {
            endIntersection = intersections.get(endIntersectionInt);
        }
        street.endIntersection = endIntersection;
        endIntersection.streetsLeadingToTheIntersection.put(street.name, street);
        street.length = scanner.nextInt();
        streets.put(street.name, street);
    }
}
