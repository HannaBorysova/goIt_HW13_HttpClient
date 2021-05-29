package ua.goit.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ua.goit.user.post.Post;
import ua.goit.user.User;
import ua.goit.user.task.ToDoList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final Gson GSON = new Gson();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String HOST = "https://jsonplaceholder.typicode.com";
    private static final String END_POINT_USERS = "/users";
    private static final String END_POINT_POSTS = "/posts";
    private static final String END_POINT_COMMENTS = "/comments";
    private static final String T0_DO = "/todos";

    public static User sendGet(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        response.body();
        final User user = GSON.fromJson(response.body(), User.class);
        return user;
    }

    public static User sendPost(URI uri, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static String sendPut(int id, User updatedUser) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(updatedUser);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .header("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static int sendDelete(User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, user.getId())))
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> sendGetAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + END_POINT_USERS))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<User>>() {}.getType());
    }

    public static User sendGetUserById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User sendGetUserByName(String name) throws IOException, InterruptedException {
        List<User> allUsers = sendGetAllUsers();
        return allUsers.stream().filter(user -> user.getName().equals(name)).findAny().orElse(new User());
    }

    public static String sendGetAllCommentsForLastPostOfUser(User user) throws IOException, InterruptedException {
        Post lastPost = sendGetLastPostOfUser(user);
        String fileName = "user-" + user.getId() + "-post-" + lastPost.getId() + "-comments.json";
        HttpRequest requestForComments = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", HOST, END_POINT_POSTS, lastPost.getId(), END_POINT_COMMENTS)))
                .GET()
                .build();
        HttpResponse<Path> responseComments = CLIENT.send(requestForComments, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
        return "Comments written to file " + responseComments.body();
    }

    private static Post sendGetLastPostOfUser(User user) throws IOException, InterruptedException {
        HttpRequest requestForPosts = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d/%s", HOST, END_POINT_USERS, user.getId(), "posts")))
                .GET()
                .build();
        HttpResponse<String> responsePosts = CLIENT.send(requestForPosts, HttpResponse.BodyHandlers.ofString());
        List<Post> allUserPosts = GSON.fromJson(responsePosts.body(), new TypeToken<List<Post>>() {
        }.getType());
        return Collections.max(allUserPosts, Comparator.comparingInt(Post::getId));
    }

    public static List<ToDoList> sendGetListOfOpenTasksForUser(User user)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", HOST, END_POINT_USERS, user.getId(), T0_DO)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<ToDoList> allTasks = GSON.fromJson(response.body(), new TypeToken<List<ToDoList>>() {
        }.getType());
        return allTasks.stream().filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
}
