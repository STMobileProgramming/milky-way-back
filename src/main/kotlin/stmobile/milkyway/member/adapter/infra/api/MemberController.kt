package stmobile.milkyway.member.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import stmobile.milkyway.member.adapter.infra.dto.CoupleInfo
import stmobile.milkyway.member.adapter.infra.dto.HomeInfo
import stmobile.milkyway.response.adapter.dto.DefaultResponseDto
import stmobile.milkyway.member.adapter.infra.dto.MemberInformation
import stmobile.milkyway.member.adapter.infra.dto.ProfileAndNickname
import stmobile.milkyway.response.adapter.api.ApiResponse
import stmobile.milkyway.member.application.MemberInfo


@RestController
@RequestMapping("/api/v1")
class MemberController (
    private val memberInfo: MemberInfo,
){

    @Operation(summary = "사용자 정보")
    @GetMapping("/member")
    fun getMemberInfo(): ResponseEntity<MemberInformation> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.getMemberInfo())
    }

    @Operation(summary = "프로필 수정전 프로필,닉네임 넘겨받는 api")
    @GetMapping("/member/profile")
    fun showProfile(): ResponseEntity<String?> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.showProfile())
    }

    @Operation(summary = "프로필 수정하기")
    @PutMapping("/member/profile")
    fun editProfile(profileAndNickname: ProfileAndNickname): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.editProfile(profileAndNickname))
    }

    @PostMapping("/couple")
    fun makeCouple(coupleInfo: CoupleInfo): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.makeCouple(coupleInfo))
    }

    @PostMapping("/member/password")
    fun editPassword(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.editPassword())
    }

    @PostMapping("/couple/break")
    fun breakOffCouple(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.breakOffCouple())
    }

    @GetMapping("/home")
    fun getHome() : ResponseEntity<HomeInfo>{
        return ApiResponse.success(HttpStatus.OK, memberInfo.getHomeInfo())
    }

    @GetMapping("code")
    fun getMyCoupleCode(): ResponseEntity<String>{
        return ApiResponse.success(HttpStatus.OK, memberInfo.getMyCoupleCode())
    }

}