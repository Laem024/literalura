package com.aluracursos.literalura.servicios;

public interface IJsonToJava {
    <T> T fromJson(String json, Class<T> clase);
}
