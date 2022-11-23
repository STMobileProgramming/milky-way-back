package stmobile.milkyway.member.adapter.infra.dto

data class SignUpRequestDto(
    val name: String,
    val userId: String,
    val password: String,
    val email: String
)