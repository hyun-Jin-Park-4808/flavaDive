package fd.flavadive.entities

import fd.flavadive.common.enums.PayMethod
import fd.flavadive.common.enums.PaymentReversalType
import fd.flavadive.common.enums.PaymentStatus
import fd.flavadive.common.enums.ReservationStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "PAYMENT")
class Payment (

    @ManyToOne
    val restaurant: Restaurant,

    @Enumerated(EnumType.STRING)
    var payMethod: PayMethod,

    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus,

    @Enumerated(EnumType.STRING)
    var paymentReversalType: PaymentReversalType,

    val paymentAmount: Long,

    var reasonForReversal: String,
    var canceledAmount: Long
): BaseEntity()