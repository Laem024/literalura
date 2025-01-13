package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.servicios.ConsumoApi;
import com.aluracursos.literalura.servicios.JsonToJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final String API_URL = "https://gutendex.com/books/";
    private final JsonToJava conversor = new JsonToJava();
    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            
            String menu = """
                    
                    *** Elija la opción que considere ***
                    
                    1- Buscar Libro
                    2- Listar Libros registrados
                    3- Listar Autores registrados
                    4- Listar Autores registrados vivos en un año determinado
                    5- Listar Libros por idioma
                    0- Salir
                    
                    """;
    
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;

                case 2:
                    listarLibrosRegistrados();
                    break;

                case 3:
                    listarAutoresRegistrados();
                    break;

                case 4:
                    listarAutoresVivosEn();
                    break;

                case 5:
                    listarLibrosPorIdioma();

                case 0:
                    System.out.println("Saliendo del menu");
                    break;

                default:
                    System.out.println("Opción no valida");
                    break;

            }
        }
    }

    private DatosGutendex obtenerDatosGutendex() {
        System.out.println("Digite el libro que desea buscar: ");
        String libro = teclado.nextLine();

        var respuestaAPI = consumoApi.obtenerDatos(API_URL + "?search=" + libro.replace(" ", "+"));
        if (respuestaAPI.isEmpty()) {
            System.out.println("No se encontró el libro");
            return null;
        }else {
            return conversor.fromJson(respuestaAPI, DatosGutendex.class);
        }
    }

    private void imprimirLibros(DatosLibro datosLibro, Libro libro) {

        if (datosLibro != null) {

            System.out.println("\r\n********* Libro *********");
            System.out.println("Titulo: " + datosLibro.getTitulo());

            List<DatosAutor> datosAutor = datosLibro.getAutores();
            if (datosAutor != null && !datosAutor.isEmpty()) {
                System.out.println("Autor: " + datosLibro.getAutores().get(0).getNombre());
            } else {
                System.out.println("Autor: ");
            }

            System.out.println("Idioma: " + datosLibro.getIdiomas());
            System.out.println("Cantidad: " + datosLibro.getCantidadDeDescargas());
            System.out.println("************************\r\n");

        }

        if (libro != null) {
            System.out.println("\r\n********* Libro *********");
            System.out.println("Titulo: " + libro.getTitulo());

            if (libro.getAutor() != null) {
                System.out.println("Autor: " + libro.getAutor().getNombre());
            } else {
                System.out.println("Autor: ");
            }

            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Cantidad: " + libro.getCantidadDeDescargas());
            System.out.println("************************\r\n");
        }


    }

    private void imprimirAutores(Autor autor) {
        System.out.println("\r\n********* Autor *********");
        System.out.println("Nombre: " + autor.getNombre());
        System.out.println("Año de nacimiento: " + autor.getBirthYear());
        System.out.println("Año de fallecimiento: " + autor.getDeathYear());

        System.out.println("Libros: ");
        autor.getLibros().forEach(libro -> {
            System.out.println(libro.getTitulo());
        });

        System.out.println("************************\r\n");
    }

    private void buscarLibro() {
        DatosGutendex datosGutendex = obtenerDatosGutendex();

        if (datosGutendex != null && datosGutendex.getLibros() != null) {

            System.out.println("Total de libros: " + datosGutendex.getCount());

            for (DatosLibro datosLibro : datosGutendex.getLibros()) {
                imprimirLibros(datosLibro, null);
                guardarLibro(datosLibro);
            }
        }
    }

    private void guardarLibro(DatosLibro datosLibro) {
        Autor autor = new Autor();

        if (datosLibro.getAutores() != null && !datosLibro.getAutores().isEmpty()) {
            autor = autorRepository.findByNombre(datosLibro.getAutores().get(0).getNombre());

            if (autor == null) {
                autor = new Autor(datosLibro.getAutores().get(0));
                try {
                    autorRepository.save(autor);
                }catch (Exception e) {
                    System.out.println("Error al guardar el autor");
                }
            }
        }

        Optional<Libro> libroBuscado = libroRepository.getLibrosById(datosLibro.getId());

        if (libroBuscado.isEmpty()) {
            Libro nuevoLibro = new Libro();
            if (autor.getNombre() != null) {
                nuevoLibro = new Libro(datosLibro, autor);
            }else {
                nuevoLibro = new Libro(datosLibro, null);
            }

            try {
                libroRepository.save(nuevoLibro);
            }catch (Exception e) {
                System.out.println("Error al guardar el libro: " + e.getMessage());
            }
        }else {
            System.out.println("Ya existe el libro con el id: " + datosLibro.getId());
            System.out.println("Libro: " + libroBuscado.get().getTitulo() + " - " + libroBuscado.get().getAutor().getNombre());
        }
    }

    private void listarLibrosRegistrados() {

        List<Libro> libros = new ArrayList<>();

        try {
            libros = libroRepository.findAll();
        }catch (Exception e) {
            System.out.println("Error al listar el libros: " + e.getMessage());
        }

        if (!libros.isEmpty()) {
            System.out.println("Lista de libros registrados:");
            for (Libro libro : libros) {
                imprimirLibros(null, libro);
            }
        }
    }

    private void listarAutoresRegistrados() {

        List<Autor> autores = new ArrayList<>();
        try {
            autores = autorRepository.findAll();
        }catch (Exception e) {
            System.out.println("Error al buscar el autores" + e.getMessage());
        }


        if (!autores.isEmpty()) {
            System.out.println("Lista de autores registrados:");
            for (Autor autor : autores) {
                imprimirAutores(autor);
            }
        }else {
            System.out.println("No hay autores registrados");
        }
    }

    public void listarAutoresVivosEn() {
        System.out.println("Digite el año que desea consultar: ");
        var year = teclado.nextInt();
        List<Autor> autores = new ArrayList<>();
        try {
            autores = autorRepository.findByBirthYearLessThanAndDeathYearGreaterThan(year, year);
        }catch (Exception e) {
            System.out.println("Error al consultar el autores: " + e.getMessage());
        }

        if (!autores.isEmpty()) {
            System.out.println("Lista de autores registrados:");
            for (Autor autor : autores) {
                imprimirAutores(autor);
            }
        }else {
            System.out.println("No hay autores registrados que estuvieran vivos en ese año");
        }
    }

    public void listarLibrosPorIdioma(){
        System.out.println("Indique el idioma que desea consultar: ");
        var idioma = teclado.nextLine();
        List<Libro> libros = new ArrayList<>();
        try {
            libros = libroRepository.findByIdiomaContainingIgnoreCase(idioma);
        }catch (Exception e) {
            System.out.println("Error al consultar el libros por idioma: " + e.getMessage());
        }

        if (!libros.isEmpty()) {
            System.out.println("Hay " + libros.toArray().length + " libros registrados en ese idioma:");
            for (Libro libro : libros) {
                imprimirLibros(null, libro);
            }
        }else {
            System.out.println("No hay libros registrados en ese idioma");
        }
    }

}
