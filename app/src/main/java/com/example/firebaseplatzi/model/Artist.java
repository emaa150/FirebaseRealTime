package com.example.firebaseplatzi.model;

public class Artist {

    private String id;
    private String name;
    private String genero;

    public Artist() {
    }

    public Artist(String id, String name, String genero) {
        this.id = id;
        this.name = name;
        this.genero = genero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
