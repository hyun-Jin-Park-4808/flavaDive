package fd.flavadive.entities

import fd.flavadive.common.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "MEMBER")
class Member (
    @Enumerated(EnumType.STRING)
    var role: Role,

    var email: String,

    // 아래서 오버라이드한 getPassword()와 코틀린이 자동생성하는 getter 가 충돌하기 때문에
    // JVM 시그니처 이름을 다르게 구분해줘야 한다.
    @get:JvmName("getMemberPassword")
    var password: String,
    var name: String,
    var nickname: String,
    var phoneNumber: String,
    var isNotificationEnabled: Boolean,
    var mannerScore: Long = 0,
    var isMonthlyEvaluator: Boolean = false,
    var businessRegistrationNumber: String? = null,
    var businessRegistrationImagePath: String? = null
): BaseEntity(), UserDetails {

    override fun getUsername(): String = email
    override fun getPassword(): String = password

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}