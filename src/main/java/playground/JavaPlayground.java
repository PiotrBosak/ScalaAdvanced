package playground;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaPlayground {
    public static void main(String... args) {
        System.out.println("java works");
        List<Integer> list = List.of(1,3,5,6);
        list = list.stream().flatMap(x -> List.of(x,x+1).stream()).collect(Collectors.toList());
        for(Integer i: list)
            System.out.println(i);
        System.out.println(fun(1,2,3,5,5,4));

    }

    static int fun(int... integers){
        return integers.length;
    }

}

