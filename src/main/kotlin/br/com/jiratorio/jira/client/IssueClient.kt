package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.request.SearchJiraIssueRequest
import br.com.jiratorio.jira.client.config.JiraClientConfiguration
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    name = "issue-client",
    url = "\${jira.url}",
    configuration = [
        JiraClientConfiguration::class
    ]
)
interface IssueClient {

    @PostMapping("/rest/api/2/search")
    fun findByJql(filter: SearchJiraIssueRequest): JsonNode

}
