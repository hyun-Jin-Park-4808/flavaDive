package fd.flavadive.entities

import fd.flavadive.common.enums.ReservationStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "RESERVATION")
class Reservation (

    @ManyToOne
    val restaurant: Restaurant,

    @ManyToOne
    val member: Member,

    var reservationDate: LocalDate,
    var reservationTime: LocalTime,
    var numberOfReservations: Long,

    @Enumerated(EnumType.STRING)
    var reservationStatus: ReservationStatus,

    var isAdvancePaymentPaid: Boolean,
    var requirements: String,
    var preorderMenus: String
): BaseEntity()