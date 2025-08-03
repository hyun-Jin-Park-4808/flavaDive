package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "CHAT_MESSAGE")
class ChatMessage (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: ChatRoom,

    @OneToMany(mappedBy = "chatMessage", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatBookMarks: MutableList<ChatBookMark> = mutableListOf(),

    var content: String,
    var likeCount: Long,
    var isReported: Boolean
): BaseEntity()
