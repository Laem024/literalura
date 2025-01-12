package com.aluracursos.literalura.servicios;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToJava implements IJsonToJava {

    @Override
    public <T> T fromJson(String json, Class<T> clase) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, clase);

        }catch (Exception e){
            System.out.println("Error al convertir el json " + e.getMessage());
            return null;
        }
    }
}
