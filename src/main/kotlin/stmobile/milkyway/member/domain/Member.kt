package stmobile.milkyway.member.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val coupleId: Long = -1L,

    var userName: String,

    val userId: String,

    var password: String,

    val email: String,

    var profileImg: String?,

    val startDay: String?,

    val code: String,

)