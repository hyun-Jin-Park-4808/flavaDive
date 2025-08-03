package fd.flavadive.entities

import fd.flavadive.common.enums.ReservationStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "RESERVATION")
class Reservation (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    val restaurant: Restaurant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @OneToMany(mappedBy = "reservation")
    var payments: MutableList<Payment> = mutableListOf(),

    var reservationDate: LocalDate,
    var reservationTime: LocalTime,
    var numberOfReservations: Long,

    @Enumerated(EnumType.STRING)
    var reservationStatus: ReservationStatus,

    var isAdvancePaymentPaid: Boolean,
    var requirements: String,
    var preorderMenus: String
): BaseEntity()