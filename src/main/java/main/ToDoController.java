package main;

import main.model.Author;
import main.model.AuthorRepository;
import main.model.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import main.model.Todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todoList")
public class ToDoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public String getTodoList(Model model) {
        Iterable<Todo> todoIterable = todoRepository.findAll();
        List<Todo> allTodo = new ArrayList<>();
        for (Todo todo : todoIterable) {
            allTodo.add(todo);
        }
        model.addAttribute("todos", allTodo);
        return "todoList";
    }

    @GetMapping("/new")
    public String newTodo(Model model) {
        model.addAttribute("todo", new Todo());
        return "new";
    }

    @PostMapping
    public String addTodo(@ModelAttribute("todo") Todo todo) {
        LocalDate begin = LocalDate.now();
        todo.setDateOfCreation(begin);
        todo.setDateOfChange(begin);
        if (todo.getAuthor().getId() == 0) {
            Author newAuthor = new Author();
            newAuthor.setName(todo.getAuthor().getName());
            todo.setAuthor(newAuthor);
            authorRepository.save(newAuthor);
        } else {
            todo.setAuthor(authorRepository.findById(todo.getAuthor().getId()).get());
        }
        todoRepository.save(todo);
        return "redirect:/todoList";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (!optionalTodo.isPresent()) {
            return "redirect:/todoList";
        }
        model.addAttribute("todo", optionalTodo.get());
        return "show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("todo", todoRepository.findById(id).get());
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateTodo(@ModelAttribute Todo newTodo, @PathVariable int id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (!optionalTodo.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null).toString();
        }
        Todo currentTodo = optionalTodo.get();
        if (newTodo.getDescription() != null) {
            currentTodo.setDescription(newTodo.getDescription());
        }
        if (newTodo.getAuthor() != null) {
            int idNewTodoAuthor = newTodo.getAuthor().getId();
            if (idNewTodoAuthor != 0) {
                currentTodo.setAuthor(newTodo.getAuthor());
            } else {
                Author author = new Author();
                author.setName(newTodo.getAuthor().getName());
                currentTodo.setAuthor(author);
            }
            currentTodo.setAuthor(newTodo.getAuthor());
        }
        if (newTodo.getDateOfCreation() != null) {
            currentTodo.setDateOfCreation(newTodo.getDateOfCreation());
        }
        LocalDate dateOfChange = LocalDate.now();
        currentTodo.setDateOfChange(dateOfChange);
        todoRepository.save(currentTodo);
        return "redirect:/todoList";
    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable int id) {
        todoRepository.deleteById(id);
        return "redirect:/todoList";
    }

    @DeleteMapping()
    public void deleteAllTodo() {
        todoRepository.deleteAll();
    }


}
