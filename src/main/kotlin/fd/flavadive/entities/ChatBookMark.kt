package fd.flavadive.entities

import fd.flavadive.common.enums.NotificationType
import fd.flavadive.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "CHAT_BOOK_MARK")
class ChatBookMark (

    @ManyToOne
    val chatMessage: ChatMessage,

    @ManyToOne
    val member: Member
): BaseEntity()