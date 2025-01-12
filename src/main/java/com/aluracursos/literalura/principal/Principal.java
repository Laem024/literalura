package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.servicios.ConsumoApi;
import com.aluracursos.literalura.servicios.JsonToJava;

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
                    
                    """;
    
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;

                case 2:
                    System.out.println("Opción 2");
                    break;

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

    private void imprimirDatosGutendex(DatosLibro datosLibro) {
            System.out.println("Libro: " + datosLibro.getTitulo());
            System.out.println("idiomas: " + datosLibro.getIdiomas());
            System.out.println("Total de descargas: " + datosLibro.getCantidadDeDescargas());

            List<DatosAutor> datosAutor = datosLibro.getAutores();
            if (datosAutor != null && !datosAutor.isEmpty()) {
                System.out.println("Autor: " + datosLibro.getAutores().get(0).getNombre());
                System.out.println("Año de nacimiento del autor: " + datosLibro.getAutores().get(0).getBirthYear());
            }else{
                System.out.println("Autor: ");
                System.out.println("Año de nacimiento del autor: ");
            }
    }

    private void buscarLibro() {
        DatosGutendex datosGutendex = obtenerDatosGutendex();

        if (datosGutendex != null && datosGutendex.getLibros() != null) {

            System.out.println("Total de libros: " + datosGutendex.getCount());

            for (DatosLibro datosLibro : datosGutendex.getLibros()) {
                imprimirDatosGutendex(datosLibro);
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

}
