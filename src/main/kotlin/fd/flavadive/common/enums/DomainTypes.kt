package fd.flavadive.common.enums

enum class Role {
    ADMIN,
    GENERAL
}

enum class NotificationType {
    MONTHLY_EVALUATOR,
    CHAT,
    DIARY,
    RESTAURANT,
    RESERVATION
}

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    PAID,
    REFUND
}

enum class PayMethod {
    CARD,
    TRANSFER,
    GIFT_CERTIFICATE
}

enum class PaymentStatus {
    FAILED,
    CANCELED,
    PAID,
    PARTIAL_CANCELED,
    PAY_PENDING,
    READY,
    VIRTUAL_ACCOUNT_ISSUED,
    FORGERY
}

enum class PaymentReversalType {
    FULL_REFUNDED,
    PARTIAL_REFUNDED,
    FULL_CANCELED,
    PARTIAL_CANCELED
}





