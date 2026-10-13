package com.pawcare.service;
import com.google.gson.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.io.*;
import java.util.*;
/** Optional Dog CEO integration. All operations are called from a SwingWorker. */
public class BreedApiService {
    private final HttpClient client=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
    public List<String> breeds() throws IOException, InterruptedException {
        JsonObject root=json(URI.create("https://dog.ceo/api/breeds/list/all"));
        return parseBreeds(root.toString());
    }
    public List<String> parseBreeds(String body) throws IOException {
        try {
            JsonObject root=JsonParser.parseString(body).getAsJsonObject();
            if(!"success".equals(root.get("status").getAsString())) throw new IOException("Breed service returned an error.");
            List<String> result=new ArrayList<>();
            for(Map.Entry<String,JsonElement> e:root.getAsJsonObject("message").entrySet()) {
                result.add(e.getKey());
                for(JsonElement sub:e.getValue().getAsJsonArray()) result.add(e.getKey()+"/"+sub.getAsString());
            }
            Collections.sort(result); return result;
        } catch(JsonParseException | IllegalStateException | NullPointerException e) { throw new IOException("Breed service returned unreadable JSON.",e); }
    }
    public BufferedImage image(String breed) throws IOException, InterruptedException {
        if(breed==null || !breed.matches("[a-z]+(/[a-z]+)?")) throw new IOException("Select a breed from the loaded list.");
        JsonObject response=json(URI.create("https://dog.ceo/api/breed/"+breed+"/images/random"));
        URI uri;
        try { uri=URI.create(response.get("message").getAsString()); }
        catch(RuntimeException e) { throw new IOException("Breed service did not return an image address.",e); }
        if(!"https".equals(uri.getScheme()) || !"images.dog.ceo".equals(uri.getHost())) throw new IOException("Unexpected image host from breed service.");
        HttpResponse<byte[]> data=client.send(HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(15)).GET().build(),HttpResponse.BodyHandlers.ofByteArray());
        if(data.statusCode()!=200 || data.body().length>10000000) throw new IOException("The breed image could not be downloaded.");
        BufferedImage image=ImageIO.read(new ByteArrayInputStream(data.body()));
        if(image==null) throw new IOException("The API image could not be read."); return image;
    }
    private JsonObject json(URI uri) throws IOException, InterruptedException {
        HttpResponse<String> response=client.send(HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(12)).header("Accept","application/json").GET().build(),HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!=200) throw new IOException("Breed service is unavailable (HTTP "+response.statusCode()+"). Try again later.");
        try {
            JsonObject root=JsonParser.parseString(response.body()).getAsJsonObject();
            if(!"success".equals(root.get("status").getAsString())) throw new IOException("Breed service returned an error."); return root;
        } catch(RuntimeException e) { throw new IOException("Breed service returned unreadable JSON.",e); }
    }
}
