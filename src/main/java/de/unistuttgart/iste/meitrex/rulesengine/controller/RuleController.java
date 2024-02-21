package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.dto.rule.RuleDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.SpringErrorPayload;
import de.unistuttgart.iste.meitrex.rulesengine.service.GameRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rule")
@RequiredArgsConstructor
public class RuleController {

    private final GameRuleService gameRuleService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new rule",
            description = "Register a new rule in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Rule created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public RuleDto createRule(
            @RequestBody @Valid
            RuleDto ruleDto
    ) {
        return gameRuleService.createRule(ruleDto);
    }

    @PutMapping(value = "/{ruleId}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Update a rule",
            description = "Update an existing rule in the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Rule updated successfully"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Rule created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public ResponseEntity<RuleDto> updateRule(
            @PathVariable("ruleId") @Parameter(description = "The id of the rule")
            UUID ruleId,

            @RequestBody @Valid
            RuleDto ruleDto
    ) {
        var responseStatus = gameRuleService.existsRule(ruleDto.getId())
                ? HttpStatus.OK
                : HttpStatus.CREATED;

        return new ResponseEntity<>(gameRuleService.updateRule(ruleId, ruleDto), responseStatus);
    }

    @DeleteMapping("/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a rule",
            description = "Delete an existing rule from the system."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Rule deleted successfully or rule did not exist"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public void deleteRule(
            @PathVariable("ruleId") @Parameter(description = "The id of the rule")
            UUID ruleId
    ) {
        gameRuleService.deleteRule(ruleId);
    }

    @GetMapping(value = "/{ruleId}", produces = "application/json")
    @Operation(
            summary = "Get a rule by its ID",
            description = "Get an existing rule from the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Rule not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public RuleDto getRule(
            @PathVariable("ruleId") @Parameter(description = "The id of the rule")
            UUID ruleId
    ) {
        return gameRuleService.getRule(ruleId);
    }

    @GetMapping(produces = "application/json")
    @Operation(
            summary = "Get all rules",
            description = "Get all existing rules from the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public List<RuleDto> getRules() {
        return gameRuleService.getAllRules();
    }

}
