package stmobile.milkyway.calendar.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CalendarRepository : JpaRepository<Calendar, Long> {

    @Query(nativeQuery = true, value = """
        select *
        from calendar c
        where c.couple_id = ?1 and c.date like ?2%
    """)
    fun findAllByDate(coupleId: String, month: String): List<Calendar>

    fun findByDate(date: String): Calendar
}