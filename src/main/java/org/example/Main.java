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

        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                String.class
        );

        String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);


        User newUser = new User(3L, "James", "Brown", (byte) 25);
        HttpEntity<User> addRequest = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> addResponse = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                addRequest,
                String.class
        );
        System.out.println("Добавление пользователя " + addResponse.getBody());


        User updateUser = new User(3L, "Thomas", "Shelby", (byte) 25);
        HttpEntity<User> updateRequest = new HttpEntity<>(updateUser, headers);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                updateRequest,
                String.class
        );
        System.out.println("Изменение пользователя " + updateResponse.getBody());


        HttpEntity<String> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                URL + "/3",
                HttpMethod.DELETE,
                deleteRequest,
                String.class
        );
        System.out.println("Удаление пользователя " + deleteResponse.getBody());

        System.out.println("\nИТОГОВЫЙ ОТВЕТ: " + addResponse.getBody() + updateResponse.getBody() + deleteResponse.getBody());

    }
}