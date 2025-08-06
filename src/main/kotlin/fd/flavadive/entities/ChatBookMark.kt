package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "CHAT_BOOK_MARK")
class ChatBookMark (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    val chatMessage: ChatMessage,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
): BaseEntity()