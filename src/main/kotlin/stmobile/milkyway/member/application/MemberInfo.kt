package stmobile.milkyway.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import stmobile.milkyway.aws.application.S3Uploader
import stmobile.milkyway.member.adapter.infra.dto.HomeInfo
import stmobile.milkyway.member.adapter.infra.dto.MemberInformation
import stmobile.milkyway.member.adapter.infra.dto.ProfileAndNickname
import stmobile.milkyway.member.domain.MemberRepository
import stmobile.milkyway.response.adapter.dto.DefaultResponseDto
import java.util.*

@Service
class MemberInfo (
    private val memberRepository: MemberRepository,
    private val s3Uploader: S3Uploader
){

    fun registerImage(multipartFile: MultipartFile): String {
        return s3Uploader.upload(multipartFile)
    }

    fun getMemberInfo(): MemberInformation {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)
        return MemberInformation(
            userName = member.userName,
            userId = member.userId,
        )
    }

    fun showProfile() : String? {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)

        return member.profileImg
    }

    fun editProfile(profileAndNickname: ProfileAndNickname) : DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.findMemberById(id)
        member.profileImg = profileAndNickname.profileImg
        memberRepository.save(member)
        return DefaultResponseDto(true, "프로필 수정이 완료됐습니다.")
    }

    fun getHomeInfo(): HomeInfo {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.findMemberById(id)
        val couple = memberRepository.findMemberByCoupleIdByIdNot(member.coupleId, id)
        return HomeInfo(
            myProfile = member.profileImg,
            coupleProfile = couple?.profileImg,
            startDay = member.startDay
        )
    }

    fun makeCouple(code: String): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val originMember = memberRepository.findMemberByCode(code)
        if (originMember != null) {
            var member = memberRepository.findMemberById(id)
            member.coupleId = originMember.coupleId
            memberRepository.save(member)
            return DefaultResponseDto(true, "커플이 연결되었습니다.")
        }
        return DefaultResponseDto(false, "일치하지 않는 코드입니다.")
    }

    fun editPassword(): DefaultResponseDto {
        return DefaultResponseDto(true, "비밀번호가 변경되었습니다.")
    }

    fun breakOffCouple(): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.findMemberById(id)
        member.coupleId = UUID.randomUUID().toString()
        memberRepository.save(member)
        return DefaultResponseDto(true, "커플 연결을 끊었습니다.")
    }
}