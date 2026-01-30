package view;

public class ViewMain {

    public int testInt(int f) {

        int j = 1;

        while (f > 0) {

            j *= f;

            f--;
        }

        return j;

    }
}
