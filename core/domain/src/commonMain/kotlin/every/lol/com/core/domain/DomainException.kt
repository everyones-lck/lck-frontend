package every.lol.com.core.domain

sealed class DomainException(
    override val message: String? = null
) : Throwable(message) {
    data class BadRequestException(override val message: String) : DomainException(message)
    data class InvalidJwtTokenException(override val message: String) : DomainException(message)
    data class NoPermissionException(override val message: String) : DomainException(message)
    data class NotFoundException(override val message: String) : DomainException(message)
    data class ServerErrorException(override val message: String) : DomainException(message)
    data class BadGatewayException(override val message: String) : DomainException(message)
    data class NetworkException(override val message: String) : DomainException(message)
    data class UnknownException(override val message: String) : DomainException(message)
}