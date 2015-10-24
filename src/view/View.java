package view;

public interface View {
 void start();
 void display(Object obj);
 void stop();
 String[] getUserCommand();
}
 