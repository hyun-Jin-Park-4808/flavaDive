package fd.flavadive.entities

import jakarta.persistence.*

@Entity
@Table(name = "CHAT_ROOM_MEMBER")
class ChatRoomMember (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: ChatRoom
): BaseEntity()
