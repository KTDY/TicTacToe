import view.MainWindow;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Using terminal or application (t/a)? ");
        Scanner sc = new Scanner(System.in);
        String m;
        m = sc.next();
        if(m.equals("t")){
            System.out.println("Terminal");
            MainWindow mw = new MainWindow(false);
            mw.Menu();
        }
        if(m.equals("a")){
            System.out.println("Application");
            MainWindow mw = new MainWindow(true);
            mw.Menu();
        }

    }
}
