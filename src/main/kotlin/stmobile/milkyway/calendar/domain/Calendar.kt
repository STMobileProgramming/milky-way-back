package stmobile.milkyway.calendar.domain

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
class Calendar (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val coupleId: String,

    val date: String,

    var image: String?,

    var place: String?,

    var description: String?

)