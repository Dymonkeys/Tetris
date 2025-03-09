public class MultithreadingExample {

    // define a method that prints a message to the console
    public static void printMessage(String message) {
      System.out.println(message);
    }
  
    public static void main(String[] args) {
      // create two threads, each running the printMessage method
      Thread thread1 = new Thread(() -> printMessage("Hello from thread 1"));
      Thread thread2 = new Thread(() -> printMessage("Hello from thread 2"));
      // start the threads
      thread1.start();
      thread2.start();
    }
  
  }
  