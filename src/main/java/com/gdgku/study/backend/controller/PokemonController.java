package com.gdgku.study.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

    private final List<Pokemon> pokemonList = new ArrayList<>();
    private long nextId = 1L;

    public static class Pokemon {
        private Long id;
        private String name;
        private String type;

        public Pokemon() {}
        public Pokemon(Long id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @PostMapping
    public Pokemon createPokemon(@RequestBody Pokemon pokemon) {
        pokemon.setId(nextId++);
        pokemonList.add(pokemon);
        return pokemon;
    }

    @GetMapping
    public List<Pokemon> getPokemons(@RequestParam(required = false) String type) {
        if(type == null){
            return pokemonList;
        }

        List<Pokemon> result = new ArrayList<>();

        for(Pokemon pokemon : pokemonList){
            if(pokemon.getType().equals(type)){
                result.add(pokemon);
            }
        }

        return result;
    }

    @GetMapping("/{id}")
    public Pokemon getPokemonById(@PathVariable Long id) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getId().equals(id)) {
                return pokemon;
            }
        }
        return null;
    }

}