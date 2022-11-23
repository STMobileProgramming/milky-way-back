package stmobile.milkyway.member.application

import org.springframework.stereotype.Service
import stmobile.milkyway.member.domain.MemberRepository
import stmobile.milkyway.response.adapter.dto.DefaultResponseDto

@Service
class MatchService(
    private val memberRepository: MemberRepository
) {

//    fun tryMatch(code: String): DefaultResponseDto {
//        val id = SecurityUtil.getCurrentMemberId()
//
//    }
}