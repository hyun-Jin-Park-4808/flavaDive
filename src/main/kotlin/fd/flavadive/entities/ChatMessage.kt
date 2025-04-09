package fd.flavadive.entities

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "CHAT_MESSAGE")
class ChatMessage (

    @ManyToOne
    @JoinColumn(name = "SENDER_ID")
    val member: Member,

    @ManyToOne
    val chatRoom: ChatRoom,

    var content: String,
    var likeCount: Long,
    var isReported: Boolean
): BaseEntity()
