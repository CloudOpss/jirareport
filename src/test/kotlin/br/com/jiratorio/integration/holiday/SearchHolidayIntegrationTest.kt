package br.com.jiratorio.integration.holiday

import br.com.jiratorio.testlibrary.Authenticator
import br.com.jiratorio.testlibrary.restassured.specification.notFound
import br.com.jiratorio.testlibrary.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.testlibrary.dsl.restAssured
import br.com.jiratorio.testlibrary.factory.domain.entity.BoardFactory
import br.com.jiratorio.testlibrary.factory.domain.entity.HolidayFactory
import br.com.jiratorio.testlibrary.matcher.IdMatcher
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

@IntegrationTest
class SearchHolidayIntegrationTest(
    private val holidayFactory: HolidayFactory,
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `find all holidays`() {
        val (id) = authenticator.withDefaultUser {
            val boardExample = boardFactory.create()
            holidayFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    HolidayEntity::board to boardExample
                )
            )

            boardExample
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/{id}/holidays", id)
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("numberOfElements", equalTo(10))
                body("totalPages", equalTo(1))
                body("content[0].id", notNullValue())
                body("content[0].date", notNullValue())
                body("content[0].description", notNullValue())
                body("content[0].boardId", IdMatcher(id))
                body("content.findAll { it.boardId == 1 }", hasSize<Any>(10))
            }
        }
    }

    @Test
    fun `find by id`() {
        val holiday = authenticator.withDefaultUser { holidayFactory.create() }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/holidays/1")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("id", IdMatcher(holiday.id))
                body("date", equalTo(holiday.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                body("description", equalTo(holiday.description))
                body("boardId", IdMatcher(holiday.board.id))
            }
        }
    }

    @Test
    fun `find by id not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/holidays/999")
            }
            then {
                spec(notFound())
            }
        }
    }

}
