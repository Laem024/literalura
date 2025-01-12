package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosGutendex {
    private int count;

    @JsonProperty("results")
    private List<DatosLibro> libros;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DatosLibro> getLibros() {
        return libros;
    }

    public void setLibros(List<DatosLibro> libros) {
        this.libros = libros;
    }
}
