package edu.com.librarymanagementsystem.patron;

import edu.com.librarymanagementsystem.system.Result;
import edu.com.librarymanagementsystem.system.StatusCode;
import edu.com.librarymanagementsystem.patron.converter.PatronDtoToPatronConverter;
import edu.com.librarymanagementsystem.patron.converter.PatronToPatronDTOConverter;
import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/patrons")
@Tag(name = "Patrons")
public class PatronController {

    PatronService patronService;

    PatronToPatronDTOConverter patronToPatronDTOConverter;

    PatronDtoToPatronConverter patronDtoToPatronConverter;

    public PatronController(PatronService patronService,
                            PatronToPatronDTOConverter patronToPatronDTOConverter,
                            PatronDtoToPatronConverter patronDtoToPatronConverter) {
        this.patronService = patronService;
        this.patronToPatronDTOConverter = patronToPatronDTOConverter;
        this.patronDtoToPatronConverter = patronDtoToPatronConverter;
    }

    @Operation(summary = "Get patron by Id", description = "Retrieves a patron that has the given Id.")
    @ApiResponse(responseCode = "200", description = "Patron retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No patron found with the specified Id")
    @GetMapping("/{patronId}")
    public Result findPatronById(@PathVariable Integer patronId){
        Patron patron = this.patronService.findById(patronId);
        PatronDTO patronDTO = patronToPatronDTOConverter.convert(patron);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", patronDTO);
    }

    @Operation(summary = "Get all patrons", description = "Retrieves all patrons.")
    @ApiResponse(responseCode = "200", description = "Patrons retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No patrons found")
    @GetMapping
    public Result findAllPatrons(){
        List<Patron> patronList = this.patronService.findAll();

        List<PatronDTO> patronDTOList = patronList.stream().
                map(foundPatron -> this.patronToPatronDTOConverter.convert(foundPatron))
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", patronDTOList);
    }

    @Operation(summary = "Add a patron", description = "Creates a patron with the given parameters.")
    @ApiResponse(responseCode = "200", description = "Patron added successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping
    public Result addPatron(@Valid @RequestBody PatronDTO patronDTO){
        //Convert PatronDto to Patron to pass to patronService
        Patron patron = this.patronDtoToPatronConverter.convert(patronDTO);
        Patron savedPatron = this.patronService.save(patron);

        //Convert Patron to PatronDto to parse as Json
        PatronDTO savedPatronDTO = this.patronToPatronDTOConverter.convert(savedPatron);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedPatronDTO);
    }

    @Operation(summary = "Update a patron", description = "Updates a patron with the given parameters.")
    @ApiResponse(responseCode = "200", description = "Patron updated successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No patron found with the specified Id")
    @PutMapping("/{patronId}")
    public Result updatePatron(@PathVariable Integer patronId, @Valid @RequestBody PatronDTO patronDTO){
        //Convert PatronDto to Patron to pass to patronService
        Patron patron = this.patronDtoToPatronConverter.convert(patronDTO);

        Patron updatedPatron = this.patronService.update(patronId, patron);

        //Convert Patron to PatronDto to parse as Json
        PatronDTO updatedPatronDTO = this.patronToPatronDTOConverter.convert(updatedPatron);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPatronDTO);
    }

    @Operation(summary = "Delete a patron", description = "Deletes a patron with the given Id.")
    @ApiResponse(responseCode = "200", description = "Patron deleted successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No patron found with the specified Id")
    @DeleteMapping("/{patronId}")
    public Result deletePatron(@PathVariable Integer patronId){
        this.patronService.delete(patronId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }

}
