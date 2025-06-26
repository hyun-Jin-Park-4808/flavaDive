package fd.flavadive.auth.dto

import fd.flavadive.common.enums.Role
import fd.flavadive.entities.Member

data class SignUpRequest(
    val email: String,
    var role: Role? = Role.GENERAL,
    var password: String,
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val isNotificationEnabled: Boolean,
    val businessRegistrationNumber: String? = null,
    val businessRegistrationImagePath: String? = null,
)

fun SignUpRequest.toEntity(): Member {
    return Member(
        email = this.email,
        role = this.role ?: Role.GENERAL,
        password = this.password,
        name = this.name,
        nickname = this.nickname,
        phoneNumber = this.phoneNumber,
        isNotificationEnabled = this.isNotificationEnabled,
        businessRegistrationNumber = this.businessRegistrationNumber,
        businessRegistrationImagePath = this.businessRegistrationImagePath,
    )
}