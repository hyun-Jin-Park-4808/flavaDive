package fd.flavadive.repositories

import fd.flavadive.entities.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun existsByEmail(username: String): Boolean?
    fun findByPhoneNumber(phoneNumber: String): Member?
}