import java.util.ArrayList;
import java.util.Scanner;

public class Kosmoloty {
    private final String name;
    private final int v_x;
    private final int v_y;
    private int pos_x;
    private int pos_y;
    private int distance;
    private boolean to_delete;


    public Kosmoloty(String name, int v_x, int v_y, int pos_x, int pos_y) {
        this.name = name;
        this.v_x = v_x;
        this.v_y = v_y;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.distance = 0;
        this.to_delete = false;

    }

    public String getName() {
        return name;
    }

    public int getV_x() {
        return v_x;
    }

    public int getV_y() {
        return v_y;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isTo_delete() {
        return to_delete;
    }

    public void setTo_delete(boolean to_delete) {
        this.to_delete = to_delete;
    }

    public static ArrayList<Kosmoloty> move(ArrayList<Kosmoloty> kosmoloty, int size_x, int size_y) {
        for (Kosmoloty kosmolot : kosmoloty) {
            kosmolot.setPos_x(kosmolot.getPos_x() + kosmolot.getV_x()); //zmiana pozycji kosmolotu na osi x
            kosmolot.setPos_y(kosmolot.getPos_y() + kosmolot.getV_y()); //zmiana pozycji kosmolotu na osi y
            //liczenie dystansu przebytego przez kosmolot:
            kosmolot.setDistance(kosmolot.getDistance() + Math.abs(kosmolot.getV_x()) + Math.abs(kosmolot.getV_y()));
            if (kosmolot.getPos_x() >= size_x) { //jezeli poza osia x
                kosmolot.setPos_x(kosmolot.getPos_x() - size_x);
            }
            if (kosmolot.getPos_y() >= size_y) { //jezeli poza osia y
                kosmolot.setPos_y(kosmolot.getPos_y() - size_y);
            }
        }
        for(int i = 0; i < kosmoloty.size(); i++) { //sprawdzanie czy 2 kosmoloty nie sa na tej samej pozycji
            for (int j = i + 1; j< kosmoloty.size(); j++) {
                if (kosmoloty.get(i).getPos_x() == kosmoloty.get(j).getPos_x())
                    if (kosmoloty.get(i).getPos_y() == kosmoloty.get(j).getPos_y()) {
                        kosmoloty.get(i).setTo_delete(true);
                        kosmoloty.get(j).setTo_delete(true);
                    }
            }
        }
        kosmoloty.removeIf(Kosmoloty::isTo_delete); //usuwanie kosmolotow (anihilacja), ktore sa na tej samej pozycji
        return kosmoloty;

    }

    public static boolean checkKosmolot(Kosmoloty kosmolot, int size_x, int size_y) {
        //poprawnosc nazwy
        if (kosmolot.getName().length() < 1 || kosmolot.getName().length() > 10 || kosmolot.getName().matches("^[0-9a-zA-Z]")) {
            return false;
        }
        //poprawnosc predkosci
        if (kosmolot.getV_x() < -10000 || kosmolot.getV_x() > 10000 || kosmolot.getV_y() < -10000 || kosmolot.getV_y() > 10000) {
            return false;
        }
        //poprawnosc pozycji
        if (kosmolot.getPos_x() < 0 || kosmolot.getPos_x() >= size_x || kosmolot.getPos_y() < 0 || kosmolot.getPos_y() >= size_y) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        boolean wrong_data = false;
        ArrayList<Kosmoloty> kosmoloty = new ArrayList<>();
        int size_x = 0;
        int size_y = 0;
        if (args.length != 2) {
           wrong_data = true;
        }
        else {
            size_x = Integer.parseInt(args[0]);
            size_y = Integer.parseInt(args[1]);
            StringBuilder data = new StringBuilder();
            Scanner sc = new Scanner(System.in);
            while(sc.hasNextLine()) {
                data.append(sc.nextLine());
            }
            String[] data_array = data.toString().split("\\\\n");
            if (size_x < 1 || size_x > 100000 || size_y < 1 || size_y > 100000) {
                wrong_data = true;
            }
            if (!wrong_data) {
                for (int i = 0; i< data_array.length; i++) {
                    String[] line = data_array[i].split(",");
                    Kosmoloty kos = new Kosmoloty(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), Integer.parseInt(line[4]));
                    if (!checkKosmolot(kos, size_x, size_y)) {
                        wrong_data = true;
                    }
                    else {
                        for(int j = 0; j < kosmoloty.size(); j++) {
                            if (kos.getName().equals(kosmoloty.get(j).getName())) {
                                wrong_data = true;
                            }
                            if (kos.getPos_x() == kosmoloty.get(j).getPos_x() && kos.getPos_y() == kosmoloty.get(j).getPos_y()) {
                                wrong_data = true;
                            }
                        }
                        if (!wrong_data) {
                            kosmoloty.add(kos);
                        }
                    }
                }
            }
        }
        if (wrong_data) {
            System.out.println("klops");
        }
        else {
            int moves = 0;
            int times = 60 * 60 * 24;
            String winner = "";
            while((System.nanoTime() - start)/1000000000.0 < 30.0 && moves <= times) {
                //jezeli program dziala mniej niz 30s oraz nie minely 24h wyscigu
                kosmoloty = move(kosmoloty, size_x, size_y);
                int dist = -1;
                for (Kosmoloty value : kosmoloty) {
                    if (value.getDistance() > dist) {
                        winner = value.getName();
                        dist = value.getDistance();
                    }
                    else if (value.getDistance() == dist) {
                        winner = "remis";
                    }
                }
                moves = moves + 1;
            }
            System.out.println(winner);
        }
    }


}
