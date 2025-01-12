package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = true)
    private Autor autor;

    private String idioma;

    private Long cantidadDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro, Autor autor) {
        this.id = datosLibro.getId();

        this.titulo = datosLibro.getTitulo();
/*
        if (datosLibro.getAutores() != null && !datosLibro.getAutores().isEmpty()) {
            DatosAutor datosAutor = datosLibro.getAutores().get(0);
            this.autor = new Autor(datosAutor);
        }else {
            this.autor = null;
        }
*/
        this.autor = autor;

        this.idioma = (datosLibro.getIdiomas() != null && !datosLibro.getIdiomas().isEmpty())
                ? String.join(", ", datosLibro.getIdiomas()) : null;

        this.cantidadDeDescargas = datosLibro.getCantidadDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDeDescargas() {
        return cantidadDeDescargas;
    }

    public void setCantidadDeDescargas(Long cantidadDeDescargas) {
        this.cantidadDeDescargas = cantidadDeDescargas;
    }
}
