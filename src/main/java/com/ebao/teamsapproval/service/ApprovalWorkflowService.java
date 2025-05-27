package com.ebao.teamsapproval.service;

import com.ebao.teamsapproval.config.ApprovalWorkflowConfig;
import com.ebao.teamsapproval.dto.ClaimApprovalRequestDTO;
import com.ebao.teamsapproval.dto.InsuredProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApprovalWorkflowService {

    private static final Logger log = LoggerFactory.getLogger(ApprovalWorkflowService.class);

    private final ApprovalWorkflowConfig webhookConfig;
    private final WebClient webClient; // Declare WebClient

    // Inject WebClient.Builder instead of creating RestTemplate directly
    public ApprovalWorkflowService(ApprovalWorkflowConfig webhookConfig, WebClient.Builder webClientBuilder) {
        this.webhookConfig = webhookConfig;
        this.webClient = webClientBuilder.build(); // Build WebClient from the injected builder
    }

    public Mono<ResponseEntity<String>> sendNewClaimAssignment(ClaimApprovalRequestDTO dto) {
        String webhookUrl = webhookConfig.getUrl();
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Webhook URL is not configured."));
        }

        String payload = createClaimAdaptiveCardTemplate(
                dto.getPolicyNumber(),
                dto.getProductCode(),
                dto.getProductDescription(),
                dto.getReasons(),
                dto.getProfile(),
                dto.getClaimNumber(),
                dto.getClaimDetails()
        );

        log.info("URL: {}", webhookUrl);
        log.info("Payload: {}", payload);

        return webClient.post()
                .uri(java.net.URI.create(webhookUrl)) // avoid double-encoding
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(responseEntity -> log.info("Webhook call successful. Status: {}, Response: {}", responseEntity.getStatusCode(), responseEntity.getBody()))
                .doOnError(ex -> log.error("Error calling webhook: {}", ex.getMessage()));
    }

    private String createClaimAdaptiveCardTemplate(
            String policyNumber,
            String productCode,
            String productDescription,
            java.util.List<String> reasons,
            InsuredProfileDTO profile,
            String claimNumber,
            String claimDetails
    ) {
        StringBuilder reasonsDynamicBlock = new StringBuilder();
        reasonsDynamicBlock.append("{\"type\": \"RichTextBlock\",\"inlines\": [{\"type\": \"TextRun\",\"text\": \"• The case violates below auto-approval rules:\"}]},");
        for (String reason : reasons) {
            reasonsDynamicBlock.append("{\"type\": \"RichTextBlock\",\"inlines\": [{\"type\": \"TextRun\",\"text\": \"    ")
                    .append("• ")
                    .append(reason.replace("\"", "\\\""))
                    .append("\"}]},");
        }

        // Customer profile
        String profileLine1 = String.format(
                "• Insured: (%d, %s, %s, %s, %.2f, %s)",
                profile.getAge(),
                profile.getGender(),
                profile.getMaritalStatus(),
                profile.getOccupation(),
                profile.getInsuredAmount(),
                profile.getCountry()
        );

        // customerGrade
        String profileLine2 = String.format("• Customer grade: %s", profile.getCustomerGrade());

        // Current date and time
        String now = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));


        String json = "{"
                + "\"type\": \"message\","
                + "\"attachments\": ["
                + "  {"
                + "    \"contentType\": \"application/vnd.microsoft.card.adaptive\","
                + "    \"contentUrl\": null,"
                + "    \"content\": {"
                + "      \"$schema\": \"http://adaptivecards.io/schemas/adaptive-card.json\","
                + "      \"type\": \"AdaptiveCard\","
                + "      \"version\": \"1.3\","
                + "      \"body\": ["
                + "        {\"type\": \"TextBlock\",\"text\": \"Requested\",\"color\": \"Good\",\"weight\": \"Bolder\",\"spacing\": \"Default\"},"
                + "        {\"type\": \"TextBlock\",\"text\": \"For Claim Approval: "+ claimNumber + " on "+ policyNumber +"\",\"size\": \"Large\",\"weight\": \"Bolder\",\"wrap\": true,\"spacing\": \"Default\"},"
                +          reasonsDynamicBlock
                + "        {\"type\": \"TextBlock\",\"text\": \"Case Summary\",\"size\": \"Medium\",\"weight\": \"Bolder\",\"spacing\": \"Large\"},"
                + "        {\"type\": \"TextBlock\",\"text\": \"Insured's profile\",\"weight\": \"Bolder\",\"spacing\": \"Default\"},"
                + "        {\"type\": \"RichTextBlock\",\"inlines\": [{\"type\": \"TextRun\",\"text\": \"" + profileLine1 + "\"}]},"
                + "        {\"type\": \"RichTextBlock\",\"inlines\": [{\"type\": \"TextRun\",\"text\": \"" + profileLine2 + "\"}]},"
                + "        {\"type\": \"TextBlock\",\"text\": \"Case information\",\"weight\": \"Bolder\",\"spacing\": \"Default\"},"
                + "        {\"type\": \"RichTextBlock\",\"inlines\": [{\"type\": \"TextRun\",\"text\": \"" + claimDetails.replace("\"", "\\\"") + "\"}]},"
                + "        {\"type\": \"TextBlock\",\"text\": \"• **CW Requested by**\",\"wrap\": true,\"spacing\": \"Default\"},"
                + "        {\"type\": \"ColumnSet\",\"columns\": ["
                + "          {\"type\": \"Column\",\"width\": \"stretch\",\"items\": ["
                + "            {\"type\": \"TextBlock\",\"text\": \"**[USERNAME]** - " + now + "\",\"wrap\": true}"
                + "          ]}"
                + "        ]}"
                + "      ]"
                + "    }"
                + "  }"
                + "]"
                + "}";


        log.info("createClaimAdaptiveCardTemplate > AdaptiveCard JSON (RAW): {}", json);
        return json;
    }

}