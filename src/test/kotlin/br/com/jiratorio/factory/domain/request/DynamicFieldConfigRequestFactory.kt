package br.com.jiratorio.factory.domain.request

import br.com.jiratorio.domain.request.DynamicFieldConfigRequest
import br.com.jiratorio.extension.faker.jira
import br.com.jiratorio.factory.KBacon
import com.github.javafaker.Faker
import org.springframework.stereotype.Component

@Component
class DynamicFieldConfigRequestFactory(
    private val faker: Faker
) : KBacon<DynamicFieldConfigRequest>() {

    override fun builder(): DynamicFieldConfigRequest {
        return DynamicFieldConfigRequest(
            name = faker.lorem().word(),
            field = faker.jira().customField()
        )
    }

}
