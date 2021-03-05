package org.example.demo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class StringTest {

    public static void main(String[] args){
        String value = "=========={0},======{1}";
        List<String> list = new LinkedList<String>();
        list.add("2020");
        list.add("2021");
        for(int i=0;i<list.size();i++){
            value = value.replace("{"+i+"}", list.get(i));
        }
        System.out.println(value);
    }
}
