package com.courses.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJPAResource {

    @Autowired
    PostRepository postRepository;

    @Autowired 
    private UserRepository userRepository;

    @GetMapping("/jpa/users")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> getUserById(@PathVariable int id){

        Optional<User> user =  userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-"+id);
        EntityModel<User> model = EntityModel.of(user.get());
        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).findAll());
        model.add(linkToUsers.withRel("all-users"));
        WebMvcLinkBuilder deleteUser = linkTo(methodOn(this.getClass()).deleteById(id));
        model.add(deleteUser.withRel("delete-this-user"));
        return model;


    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public User deleteById(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        
        if(!user.isPresent()){
            throw new UserNotFoundException("id-"+id);
        }
        userRepository.deleteById(id);
        return user.get();
    }
    
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrieveAllUsersPost(@PathVariable int id){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
        	throw new UserNotFoundException("id-"+id);
        }
        
        return userOptional.get().getPosts();
    }
    
    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id,@RequestBody Post post){
        Optional<User> savedUser = userRepository.findById(id);

        if(!savedUser.isPresent()) {
        	throw new UserNotFoundException("id-"+id);
        }
        
        User user = savedUser.get();
        post.setUser(user);
        postRepository.save(post);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

}
