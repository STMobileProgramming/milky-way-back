package stmobile.milkyway.calendar.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Calendar (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val coupleId: Long,

    val date: String,

    val image: String?,

    val place: String?,

    val description: String?

)