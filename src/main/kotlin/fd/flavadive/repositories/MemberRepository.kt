package fd.flavadive.repositories

import fd.flavadive.entities.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface MemberEmailOnly {
    fun getEmail(): String
}

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun existsByEmail(username: String): Boolean?
    fun findByPhoneNumber(phoneNumber: String): MemberEmailOnly?
}