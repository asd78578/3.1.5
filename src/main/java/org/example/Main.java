package org.example;

import org.example.model.User;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Main {
    private static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        try {

            ResponseEntity<String> getResponse = restTemplate.getForEntity(URL, String.class);
            String cookies = getResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(HttpHeaders.COOKIE, cookies);

            StringBuilder resultCode = new StringBuilder();

            User newUser = new User(3L, "James", "Brown", (byte) 25);
            HttpEntity<User> postEntity = new HttpEntity<>(newUser, headers);
            ResponseEntity<String> postResponse = restTemplate.postForEntity(URL, postEntity, String.class);
            resultCode.append(postResponse.getBody());
            System.out.println("Добавление пользователя " + postResponse.getBody());

            User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 25);
            HttpEntity<User> putEntity = new HttpEntity<>(updatedUser, headers);
            ResponseEntity<String> putResponse = restTemplate.exchange(URL, HttpMethod.PUT, putEntity, String.class);
            resultCode.append(putResponse.getBody());
            System.out.println("Изменение пользователя " + putResponse.getBody());

            ResponseEntity<String> deleteResponse = restTemplate.exchange(
                    URL + "/3",
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers),
                    String.class
            );
            resultCode.append(deleteResponse.getBody());
            System.out.println("Удаление пользователя " + deleteResponse.getBody());

            System.out.println("\nИтоговый ответ: " + resultCode.toString()
            +"\u001B[31m" + "\nEND");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}