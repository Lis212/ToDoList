package main;

import main.model.Todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {
    private static int currentTodoId = 1;
    private static HashMap<Integer, Todo> todoHashMap = new HashMap<Integer, Todo>();

    public static List<Todo> getAllTodo() {
        List<Todo> todoList = new ArrayList<Todo>();
        todoList.addAll(todoHashMap.values());
        return todoList;
    }

    public static int addTodo(Todo todo) {
        int todoId = currentTodoId++;
        todo.setId(todoId);
        todoHashMap.put(todoId, todo);
        return todoId;
    }

    public static Todo getTodoById(int todoId) {
        if (todoHashMap.containsKey(todoId)) {
            return todoHashMap.get(todoId);
        }
        return null;
    }

    public static void updateTodo(int todoId, Todo newTodo) {
        if (todoHashMap.containsKey(todoId)) {
            Todo currentTodo = todoHashMap.get(todoId);
            currentTodo.setDescription(newTodo.getDescription());
            currentTodo.setAuthor(newTodo.getAuthor());
            currentTodo.setDateOfCreation(newTodo.getDateOfCreation());
            LocalDate dateOfChange = LocalDate.now();
            currentTodo.setDateOfChange(dateOfChange);
        }
    }

    public static void updateFieldTodo(int todoId, Todo newTodo) {
        if (todoHashMap.containsKey(todoId)) {
            Todo currentTodo = todoHashMap.get(todoId);
            if (newTodo.getDescription() != null) {
                currentTodo.setDescription(newTodo.getDescription());
            }
            if (newTodo.getAuthor() != null) {
                currentTodo.setAuthor(newTodo.getAuthor());
            }
            if (newTodo.getDateOfCreation() != null) {
                currentTodo.setDateOfCreation(newTodo.getDateOfCreation());
            }
            LocalDate dateOfChange = LocalDate.now();
            currentTodo.setDateOfChange(dateOfChange);
        }
    }

    public static void deleteTodo(int id) {
        if (todoHashMap.containsKey(id)) {
            todoHashMap.remove(id);
        }
    }
}
