package playground;

public class JavaPlayground {
    public static void main(String... args) {
        System.out.println("java works");
        System.out.println(fun(1,2,3,5,5,4));
    }

    static int fun(int... integers){
        return integers.length;
    }
}
