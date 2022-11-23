package stmobile.milkyway.calendar.application

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import stmobile.milkyway.calendar.adapter.dto.CalendarUpload
import stmobile.milkyway.calendar.adapter.dto.MonthImages
import stmobile.milkyway.calendar.domain.Calendar
import stmobile.milkyway.calendar.domain.CalendarRepository
import stmobile.milkyway.member.application.SecurityUtil
import stmobile.milkyway.member.domain.MemberRepository
import stmobile.milkyway.response.adapter.dto.DefaultResponseDto
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Service
class CalendarService(
    private val memberRepository: MemberRepository,
    private val modelMapper: ModelMapper,
    private val calendarRepository: CalendarRepository
) {

    fun getMonthImages(month: String) : List<MonthImages>{
        val id = SecurityUtil.getCurrentMemberId()
        val coupleId = memberRepository.findCoupleIdById(id)
        val month = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM")).toString()
        return calendarRepository.findAllByDate(coupleId, month).map { modelMapper.map(it, MonthImages::class.java) }
    }

    fun uploadImage(calendarUpload: CalendarUpload) : DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val coupleId = memberRepository.findCoupleIdById(id)
        calendarRepository.save(Calendar(coupleId = coupleId,
                                            date = calendarUpload.date,
                                            image = calendarUpload.image,
                                            place = calendarUpload.place,
                                            description = calendarUpload.description))
        return DefaultResponseDto(true, "캘린더 업로드를 성공하였습니다.")
    }

}