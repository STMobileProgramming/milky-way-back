package stmobile.milkyway.calendar.application

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import stmobile.milkyway.calendar.adapter.dto.CalendarUpload
import stmobile.milkyway.calendar.adapter.dto.DateInfo
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
        val coupleId = memberRepository.findMemberById(id).coupleId
        val calendars = calendarRepository.findAllByDate(coupleId, month)
        var outputs: MutableList<MonthImages> = mutableListOf()
        for(i in calendars.indices){
            outputs.add(
                MonthImages(
                id = calendars[i].id,
                date = calendars[i].date,
                image = calendars[i].image
            ))
        }
        return outputs
//        return calendarRepository.findAllByDate(coupleId, month).stream().map { it ->  }
//            .map { modelMapper.map(it, MonthImages::class.java) }
    }

    fun uploadImage(calendarUpload: CalendarUpload) : DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val existedCalendar = calendarRepository.findByDate(calendarUpload.date)
        if (existedCalendar != null) {
            calendarRepository.delete(existedCalendar!!)
        }
        val coupleId = memberRepository.findMemberById(id).coupleId
        calendarRepository.save(Calendar(coupleId = coupleId,
                                            date = calendarUpload.date,
                                            image = calendarUpload.image,
                                            place = calendarUpload.place,
                                            description = calendarUpload.description))
        return DefaultResponseDto(true, "캘린더 업로드를 성공하였습니다.")
    }

    fun getDateInfo(date: String) : DateInfo {
        val calendar = calendarRepository.findByDate(date)
        return DateInfo(
            date = calendar?.date,
            image = calendar?.image,
            place = calendar?.place,
            description = calendar?.description
        )
    }

//    fun putCalendar(calendarUpload: CalendarUpload): DefaultResponseDto {
//        val calendar = calendarRepository.findByDate(calendarUpload.date)
//        if(cale)
//        calendar?.image = calendarUpload.image
//        calendar?.description = calendarUpload.description
//        calendar?.place = calendarUpload.place
//        calendarRepository.save(calendar)
//        return DefaultResponseDto(true, "캘린더 수정이 성공적으로 완료되었습니다")
//    }

    fun deleteCalendar(date: String): DefaultResponseDto {
        val calendar = calendarRepository.findByDate(date)
        calendarRepository.delete(calendar!!)
        return DefaultResponseDto(true, "해당 날짜의 캘린더가 삭제되었습니다.")
    }
}