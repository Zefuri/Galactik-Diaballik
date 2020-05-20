package view;

public class Repainter {
    public boolean isRepainted;

    private static Repainter instance;

    public static Repainter getInstance() {
        if (instance == null) {
            instance = new Repainter();
        }
        return instance;
    }

    private Repainter() {
        isRepainted = false;
    }
}
