package com.frico.usct.widget.gridpager;

/**
 * Created on 2018/9/12.
 */
public class Model {
    public String name;
    public String title;

    public Model(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}