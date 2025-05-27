package com.ebao.teamsapproval.controller;

import com.ebao.teamsapproval.dto.ClaimApprovalRequestDTO;
import com.ebao.teamsapproval.service.ApprovalWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/teams-channel")
public class ApprovalWorkflowController {

    private final ApprovalWorkflowService webhookService;

    public ApprovalWorkflowController(ApprovalWorkflowService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/new-claim-assigment")
    public Mono<ResponseEntity<String>> sendNewClaimAssignment(@RequestBody ClaimApprovalRequestDTO requestDTO) {
        return webhookService.sendNewClaimAssignment(requestDTO);
    }
}