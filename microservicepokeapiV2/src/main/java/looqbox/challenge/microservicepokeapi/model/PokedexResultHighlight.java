package looqbox.challenge.microservicepokeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PokedexResultHighlight {

    private List<Pokemon> results;

    public PokedexResultHighlight() {
    }

    public PokedexResultHighlight(List<Pokemon> results) {
        this.results = results;
    }



    public List<Pokemon> getResults() {
        return results;
    }

    public void setResults(List<Pokemon> results) {
        this.results = results;
    }

}