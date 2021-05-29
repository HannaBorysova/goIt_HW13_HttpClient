package ua.goit.http;

import com.google.gson.Gson;
import ua.goit.user.User;
import ua.goit.user.task.ToDoList;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static ua.goit.http.UserCreation.createDefaultUser;

public class HttpClientDemo {
    public static final Gson GSON = new Gson();
    private static final String CREATE_USER_URL = "https://jsonplaceholder.typicode.com/users";
    private static final int ID_TO_CHANGE = 1;
    private static final String USER_NAME_TO_GET = "Leanne Graham";

    public static void main(String[] args) throws IOException, InterruptedException {
        //task 1.1
        System.out.println("task 1.1");
        User user = createDefaultUser();
        final User createdUser = HttpUtil.sendPost(URI.create(CREATE_USER_URL), user);
        System.out.println(createdUser);

        //task 1.2
        System.out.println("\ntask 1.2");
        User userNewName = HttpUtil.sendGet(ID_TO_CHANGE);
        System.out.println("User before update : " + userNewName);
        User updatedUser = new User();
        updatedUser.setName("Liana Liana");
        updatedUser.setUsername(createdUser.getUsername());
        updatedUser.setEmail(createdUser.getEmail());
        updatedUser.setAddress(createdUser.getAddress());
        updatedUser.setWebsite(createdUser.getWebsite());
        updatedUser.setPhone(createdUser.getPhone());
        updatedUser.setCompany(createdUser.getCompany());
        String sendPut = HttpUtil.sendPut(ID_TO_CHANGE, updatedUser);
        User newUpdatedUser = GSON.fromJson(sendPut, User.class);
        System.out.println("User after update : " + newUpdatedUser);

        //task 1.3
        System.out.println("\ntask 1.3");
        createdUser.setId(ID_TO_CHANGE);
        System.out.println("Status after delete user: " + HttpUtil.sendDelete(createdUser));

        //task 1.4
        System.out.println("\ntask 1.4");
        System.out.println("ALL USERS LIST:");
        List<User> allUsers = HttpUtil.sendGetAllUsers();
        allUsers.forEach(System.out::println);

        //task 1.5
        System.out.println("\ntask 1.5");
        System.out.println("User by Id = 1: " + HttpUtil.sendGetUserById(ID_TO_CHANGE));

        //task 1.6
        System.out.println("\ntask 1.6");
        System.out.println("User by Name Leanne Graham: "
                + HttpUtil.sendGetUserByName(USER_NAME_TO_GET));

        //Task 2
        System.out.println("\nTask 2");
        System.out.println(HttpUtil.sendGetAllCommentsForLastPostOfUser(createdUser));

        //Task 3
        System.out.println("\nTask 3\n" + "Uncompleted tasks: ");
        List<ToDoList> allOpenTasks = HttpUtil.sendGetListOfOpenTasksForUser(createdUser);
        allOpenTasks.forEach(System.out::println);
    }
}