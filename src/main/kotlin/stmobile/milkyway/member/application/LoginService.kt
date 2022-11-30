package stmobile.milkyway.member.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import stmobile.milkyway.jwt.TokenProvider
import stmobile.milkyway.member.adapter.infra.dto.*
import stmobile.milkyway.member.domain.Member
import stmobile.milkyway.member.domain.MemberRepository
import java.util.*
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional


@Service
class LoginService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7

    @Transactional
    fun signup(signUpRequestDto: SignUpRequestDto): SignUpResponseDto {
        if (memberRepository.existsByUserId(signUpRequestDto.userId)) {
            return SignUpResponseDto(false, "중복된 아이디 입니다.")
        }
        val member = memberRepository.save(
            Member(
                userId = signUpRequestDto.userId,
                coupleId = UUID.randomUUID().toString(),
                userName = signUpRequestDto.name,
                password = passwordEncoder.encode(signUpRequestDto.password),
                email = signUpRequestDto.email,
                profileImg = null,
                startDay = null,
                code = makeCode()
            )
        )
        return SignUpResponseDto(true, "회원가입을 성공했습니다.")
    }

    fun makeCode() : String{
        val random = Random()
        return random.ints(48, 123)
            .filter { i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
            .limit(10)
            .collect({ StringBuilder() }, java.lang.StringBuilder::appendCodePoint, java.lang.StringBuilder::append)
            .toString()
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto): TokenDto {

        val member = memberRepository.findByUserId(loginRequestDto.userId)

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequestDto.userId, loginRequestDto.password)

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)

        val tokenRoleDto = TokenDto(
            grantType = tokenDto.grantType,
            accessToken = tokenDto.accessToken,
            refreshToken = tokenDto.refreshToken,
            accessTokenExpiresIn = tokenDto.accessTokenExpiresIn,
        )

        redisTemplate.opsForValue()
            .set(
                "RT:" + authentication.name,
                tokenRoleDto.refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME.toLong(),
                TimeUnit.MILLISECONDS
            )

        return tokenRoleDto
    }

    @Transactional
    fun reissue(tokenRequestDto: TokenRequestDto): TokenDto? {

        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
            return null
        }

        val authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)

        val refreshToken: Any? = redisTemplate.opsForValue().get("RT:" + authentication.name)
        if (refreshToken == null || refreshToken != tokenRequestDto.refreshToken) {
            return null
        }

        val tokenDto = tokenProvider.generateTokenDto(authentication)

        redisTemplate.delete("RT:" + authentication.name)
        redisTemplate.opsForValue().set(
            "RT:" + authentication.name,
            tokenDto.refreshToken,
            REFRESH_TOKEN_EXPIRE_TIME.toLong(),
            TimeUnit.MILLISECONDS
        )

        return tokenDto
    }
}