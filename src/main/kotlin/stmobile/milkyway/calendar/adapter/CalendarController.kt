package stmobile.milkyway.calendar.adapter

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import stmobile.milkyway.calendar.adapter.dto.CalendarUpload
import stmobile.milkyway.calendar.adapter.dto.DateInfo
import stmobile.milkyway.calendar.adapter.dto.MonthImages
import stmobile.milkyway.calendar.application.CalendarService
import stmobile.milkyway.member.adapter.infra.dto.SignUpRequestDto
import stmobile.milkyway.member.adapter.infra.dto.SignUpResponseDto
import stmobile.milkyway.response.adapter.api.ApiResponse
import stmobile.milkyway.response.adapter.dto.DefaultResponseDto

@RequestMapping("/api/v1/calendar")
@RestController
class CalendarController(
    private val calendarService: CalendarService
) {

    @GetMapping("/{month}")
    fun getCalendars(@PathVariable month: String): ResponseEntity<List<MonthImages>> {
        return ApiResponse.success(HttpStatus.OK, calendarService.getMonthImages(month))
    }

    @PostMapping("")
    fun postCalendar(calendarUpload: CalendarUpload): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, calendarService.uploadImage(calendarUpload))
    }

    @GetMapping("/date/{date}")
    fun getDateInfo(@PathVariable date: String): ResponseEntity<DateInfo> {
        return ApiResponse.success(HttpStatus.OK, calendarService.getDateInfo(date))
    }
}