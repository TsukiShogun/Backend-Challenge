package looqbox.challenge.microservicepokeapi.model;

import java.util.List;

public class SimplePokedexResult {
    private List<String> result;

    public SimplePokedexResult(List<String> result) {
        this.result = result;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

}
