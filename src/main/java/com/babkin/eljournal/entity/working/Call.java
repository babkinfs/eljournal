package com.babkin.eljournal.entity.working;

import javax.persistence.*;
import java.text.ParseException;

//одно расписание - много планов
@Entity
@Table(name="call")
public class Call {//implements Comparable {

    @Id
    @Column(name = "call_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "name", length = 15, nullable = false)
    private String name;

    public static int compare(Call a, Call b) throws ParseException {
        return compareCall(a, b);
    }
    public static int compareCall(Call firstCall, Call secondCall){
        String[] firstArr = firstCall.getName().split("-");
        String[] secondArr = secondCall.getName().split("-");
        int first = Integer.parseInt(firstArr[0]);
        int second = Integer.parseInt(secondArr[0]);
        return first - second;
    }


    public Call() {
    }
    public Call(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
