package com.mk.jsongen.model.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DynamicList {

    private List<Object> list;

    public DynamicList() {
        this.list = new ArrayList<>();
    }

    public static DynamicList of(List<String> stringList) {
        DynamicList dynamicList = new DynamicList();
        stringList.forEach(dynamicList::addDynamicValue);
        return dynamicList;
    }

    public static DynamicList of(Object... args) {
        DynamicList dynamicList = new DynamicList();
        dynamicList.list = new ArrayList<>(Arrays.asList(args));
        return dynamicList;
    }

    public <E> E get(int index, Class<E> typeToken) {
        if (index <= list.size() && list.get(index).getClass().isAssignableFrom(typeToken)) {
            return typeToken.cast(list.get(index));
        }
        throw new IllegalArgumentException();
    }

    public String getString(int index) {
        return list.get(index).toString();
    }

    public void addDynamicValue(String string) {
        try {
            list.add(Integer.parseInt(string));
        } catch (Exception e) {
            list.add(string);
        }
    }

    public int size() {
        return list.size();
    }
}
