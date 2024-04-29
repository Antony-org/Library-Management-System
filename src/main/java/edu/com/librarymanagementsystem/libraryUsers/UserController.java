package edu.com.librarymanagementsystem.libraryUsers;

import edu.com.librarymanagementsystem.libraryUsers.Dto.UserDto;
import edu.com.librarymanagementsystem.libraryUsers.converter.LibraryUserToUserDtoConverter;
import edu.com.librarymanagementsystem.libraryUsers.converter.UserDtoToLibraryUserConverter;
import edu.com.librarymanagementsystem.system.Result;
import edu.com.librarymanagementsystem.system.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;

    private final LibraryUserToUserDtoConverter libraryUserToUserDtoConverter;

    private final UserDtoToLibraryUserConverter userDtoToLibraryUserConverter;

    public UserController(UserService userService, LibraryUserToUserDtoConverter libraryUserToUserDtoConverter, UserDtoToLibraryUserConverter userDtoToLibraryUserConverter) {
        this.userService = userService;
        this.libraryUserToUserDtoConverter = libraryUserToUserDtoConverter;
        this.userDtoToLibraryUserConverter = userDtoToLibraryUserConverter;
    }

    @Operation(summary = "Get a User by ID", description = "Retrieves a User with the given ID.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "User not found with the specified ID.")
    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        LibraryUser user = this.userService.findById(userId);
        UserDto userDto = this.libraryUserToUserDtoConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "All users retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No Users were found.")
    @GetMapping
    public Result findAllUsers(){
        List<LibraryUser> userList = this.userService.findAll();
        // loop on all list of users and convert them to a list of Dtos return to the client.
        List<UserDto> userDtos = userList.stream().
                map(foundUser -> this.libraryUserToUserDtoConverter.convert(foundUser))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @Operation(summary = "Add a new user", description = "Creates a new user")
    @ApiResponse(responseCode = "200", description = "User added successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @PostMapping
    public Result addUser(@Valid @RequestBody LibraryUser user){
        LibraryUser savedUser = this.userService.save(user);
        UserDto userDto = this.libraryUserToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
    }

    @Operation(summary = "Update an existing user", description = "Updates an existing user based on ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto){
        // takes users as dto then convert them to LibraryUser class to process.
        LibraryUser user = this.userDtoToLibraryUserConverter.convert(userDto);
        LibraryUser updatedUser = this.userService.update(userId, user);
        // return them to userDto to parse as Json.
        UserDto savedUserDto = this.libraryUserToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", savedUserDto);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user based on ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }
}