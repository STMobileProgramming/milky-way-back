package stmobile.milkyway.member.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findMemberById(id: Long): Member
    fun findByUserId(userId:String): Member
    fun getByUserId(userId:String): Member
    fun existsByUserId(userId:String) : Boolean

    @Query(nativeQuery = true, value = """
        select *
        from member m
        where m.couple_id = ?1
        and m.id != ?2 
    """)
    fun findCouple(coupleId: String?, id:Long): Member?
    fun findMemberByCode(code: String): Member?
}