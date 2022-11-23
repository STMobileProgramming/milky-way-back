package stmobile.milkyway.member.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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

    @Operation(summary = "이미지 업로드")
    @PostMapping("/member/image")
    fun registerStoreImage(@RequestPart("image") image:MultipartFile): String {
        return memberInfo.registerImage(image)
    }

    @Operation(summary = "사용자 정보")
    @GetMapping("/member")
    fun getMemberInfo(): ResponseEntity<MemberInformation> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.getMemberInfo())
    }

    @Operation(summary = "프로필 수정전 프로필,닉네임 넘겨받는 api")
    @GetMapping("/member/profile")
    fun showProfile(): ResponseEntity<ProfileAndNickname> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.showProfile())
    }

    @Operation(summary = "프로필 수정하기")
    @PutMapping("/member/profile")
    fun editProfile(@RequestBody profileAndNickname: ProfileAndNickname): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.editProfile(profileAndNickname))
    }

}